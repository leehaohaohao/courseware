package com.lihao.courseware.service;

import com.lihao.courseware.entity.dto.CommentDto;
import com.lihao.courseware.entity.po.Comment;
import com.lihao.courseware.entity.query.CommentQuery;
import com.lihao.courseware.exception.GlobalException;

import java.util.List;

public interface ChatService {
    List<CommentDto> getComment(CommentQuery commentQuery,Long userId);
    void insert(Comment comment) throws GlobalException;
}
