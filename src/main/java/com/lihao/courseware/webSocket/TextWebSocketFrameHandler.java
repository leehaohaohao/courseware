package com.lihao.courseware.webSocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lihao.courseware.entity.dto.CommentDto;
import com.lihao.courseware.entity.po.Comment;
import com.lihao.courseware.entity.po.UserInfo;
import com.lihao.courseware.entity.query.CommentQuery;
import com.lihao.courseware.entity.query.UserInfoQuery;
import com.lihao.courseware.mapper.CommentMapper;
import com.lihao.courseware.mapper.UserInfoMapper;
import com.lihao.courseware.util.CommonTools;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    @Resource
    private CommentMapper<Comment, CommentQuery> commentMapper;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(TextWebSocketFrameHandler.class);
    private static ConcurrentMap<ChannelHandlerContext, Long> clients = new ConcurrentHashMap<>();
    //rtx是保留共享桌面的用户
    private static ChannelHandlerContext rtx = null;
    private static ConcurrentLinkedQueue<JsonNode> iceQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<JsonNode> messageQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("服务端收到新连接: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (rtx == ctx) {
            //共享用户离开清空消息队列、除去rtx
            rtx = null;
            messageQueue.clear();
        }
        logger.info("服务端收到断开连接: {}", ctx.channel().remoteAddress());
        clients.remove(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String message = msg.text();
        logger.info("服务端收到消息:{}", message);
        Long userId = clients.get(ctx);
        if (userId == null) {
            handleNewConnection(ctx, message);
        } else {
            handleExistingConnection(ctx, message, userId);
        }
    }

    private void handleNewConnection(ChannelHandlerContext ctx, String message) {
        if (message == null || message.trim().isEmpty()) {
            logger.info("未登录用户已被系统强制下线...");
            ctx.close();
        } else {
            message = message.replaceAll("[\\n\\r]", "");
            /*logger.info("新接入用户的token: {}", message);*/
            Long userId = Long.valueOf(message);
            logger.info("用户:{}上线了", userId);
            clients.put(ctx, userId);
            if(messageQueue.size()==2){
                for(JsonNode json:messageQueue){
                    sendMessage(ctx, json);
                }
            }
        }
    }

    private void handleExistingConnection(ChannelHandlerContext ctx, String message, Long userId) throws Exception {
        ChannelHandlerContext ntx = getCtxFromMap(userId);
        if (ntx != ctx) {
            ctx.close();
            logger.info("当前用户:{}被挤下线了", userId);
            clients.remove(ctx);
            clients.put(ntx, userId);
            logger.info("用户:{}重新上线了", userId);
            logger.info("服务端收到消息: {}" + message, ctx.channel().remoteAddress());
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(message);
        } catch (Exception e) {
            logger.error("消息不是有效的JSON: {}", message);
        }
        if (jsonNode != null && jsonNode.has("type")) {
            String type = jsonNode.get("type").asText();
            logger.info("消息类型: {}", type);
            if ("offer".equals(type)) {
                // 只有消息队列里有ice才进行插入 否则直接广播
                if (messageQueue.size() == 1) {
                    for (JsonNode json : messageQueue) {
                        logger.info("此时的queue应该只能有一个ice，实际{}", json.toString());
                    }
                    messageQueue.offer(jsonNode);
                } else {
                    for (JsonNode json : messageQueue) {
                        logger.info("此时的queue应该只能有零个或者二个，实际{}", json.toString());
                    }
                    broadcastMessage(ctx, message);
                }
            } else if ("ice-candidate".equals(type)) {
                // 如果当前用户是rtx，并且消息队列不为空，则清空消息队列
                if (rtx == ctx && !messageQueue.isEmpty()) {
                    messageQueue.clear();
                }
                // 判断如果是连接的用户返回的ice则直接广播
                if (messageQueue.size() == 2) {
                    for (JsonNode json : messageQueue) {
                        logger.info("此时的queue应该只能有二个，实际{}", json.toString());
                    }
                    broadcastMessage(ctx, message);
                } else {
                    for (JsonNode json : messageQueue) {
                        logger.info("此时的queue应该只能有零个，实际{}", json.toString());
                    }
                    rtx = ctx;
                    messageQueue.offer(jsonNode);
                }
            } else {
                broadcastMessage(ctx, message);
            }
            if (!"answer".equals(type) && messageQueue.size() == 2 && rtx == ctx) {
                for (JsonNode json : messageQueue) {
                    broadcastMessage(ctx, json.toString());
                }
            }
        } else {
            // 处理用户聊天消息
            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserId(userId);
            UserInfo userInfo = userInfoMapper.selectById(userInfoQuery);
            Comment comment = new Comment();
            comment.setTime(new Date());
            comment.setUserId(userId);
            comment.setId(CommonTools.getId());
            comment.setContent(message);
            commentMapper.insert(comment);
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            commentDto.setName(userInfo.getName());
            commentDto.setAvatar(userInfo.getAvatar());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(commentDto);
            broadcastMessage(ctx, json);
        }
    }


    private void sendMessage(ChannelHandlerContext ctx, JsonNode message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            for (ChannelHandlerContext ntx : clients.keySet()) {
                if (ntx == ctx) {
                    ntx.channel().writeAndFlush(new TextWebSocketFrame(jsonMessage));
                    logger.info("服务端定向消息: {}" + jsonMessage, ctx.channel().remoteAddress());
                }
            }
        } catch (Exception e) {
            logger.error("消息转换为JSON字符串失败", e);
        }
    }

    private void broadcastMessage(ChannelHandlerContext ctx, String message) {
        for (ChannelHandlerContext ntx : clients.keySet()) {
            if (ctx != ntx) {
                logger.info("服务端向用户:{}发送消息:{}", clients.get(ntx), message);
                ntx.channel().writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            logger.info("握手成功");
        }
    }

    protected ChannelHandlerContext getCtxFromMap(Long userId) {
        for (ChannelHandlerContext ctx : clients.keySet()) {
            if (clients.get(ctx).equals(userId)) {
                return ctx;
            }
        }
        return null;
    }
}
