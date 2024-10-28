package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.dto.CommentDto;
import com.lihao.courseware.entity.po.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper<T,Q> {
    List<Comment> selectList(@Param("query")Q q);
    Integer insert(@Param("bean")T t);
}
