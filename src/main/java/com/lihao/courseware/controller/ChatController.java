package com.lihao.courseware.controller;

import com.lihao.courseware.annotation.Login;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.Comment;
import com.lihao.courseware.entity.po.Page;
import com.lihao.courseware.entity.query.CommentQuery;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.service.ChatService;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

/**
 * netty聊天视频通讯
 */
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController{
    @Resource
    private ChatService chatServiceImpl;

    /**
     * 获取评论列表
     * @param page 分页信息（可以不传） pageNum页码 pageSize一页显示数量
     * @return
     * @throws GlobalException
     */
    @PostMapping("/get/comment")
    @Login
    public ResponsePack getComment(@RequestBody(required = false) Page page) throws GlobalException {
        page = Optional.ofNullable(page)
                .orElse(new Page(10,1));
        CommentQuery commentQuery = new CommentQuery();
        commentQuery.setPage(page);
        commentQuery.setOrderBy("time desc");
        return getSuccessResponse(chatServiceImpl.getComment(commentQuery, CommonTools.getUserId()));
    }

    /**
     * 发评论
     * @param comment 评论内容 content不能为空
     * @return
     * @throws GlobalException
     */
    @PostMapping("/publish/comment")
    @Login
    public ResponsePack publishComment(@RequestBody Comment comment) throws GlobalException {
        if(CommonTools.isBlank(comment.getContent())){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        comment.setUserId(CommonTools.getUserId());
        comment.setTime(new Date());
        comment.setId(CommonTools.getId());
        chatServiceImpl.insert(comment);
        return getSuccessResponse(null);
    }
}
