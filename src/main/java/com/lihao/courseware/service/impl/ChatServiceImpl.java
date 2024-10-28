package com.lihao.courseware.service.impl;

import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.CommentDto;
import com.lihao.courseware.entity.po.Comment;
import com.lihao.courseware.entity.po.UserInfo;
import com.lihao.courseware.entity.query.CommentQuery;
import com.lihao.courseware.entity.query.UserInfoQuery;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.mapper.CommentMapper;
import com.lihao.courseware.mapper.UserInfoMapper;
import com.lihao.courseware.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private CommentMapper<Comment, CommentQuery> commentMapper;
    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    @Override
    public List<CommentDto> getComment(CommentQuery commentQuery,Long userId) {
        List<Comment> comments = commentMapper.selectList(commentQuery);
        List<CommentDto> commentDtos = comments.stream().
                map(comment -> {
                    UserInfoQuery userInfoQuery =new UserInfoQuery();
                    userInfoQuery.setUserId(comment.getUserId());
                    UserInfo userInfo = userInfoMapper.selectById(userInfoQuery);
                    CommentDto commentDto = new CommentDto();
                    BeanUtils.copyProperties(comment,commentDto);
                    commentDto.setName(userInfo.getName());
                    commentDto.setAvatar(userInfo.getAvatar());
                    commentDto.setStatus(userId.equals(comment.getUserId())?1:0);
                    return commentDto;
                }).toList();
        return commentDtos;
    }

    @Override
    @Transactional
    public void insert(Comment comment) throws GlobalException {
        if(!commentMapper.insert(comment).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
    }
}
