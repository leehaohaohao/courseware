package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.Discuss;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DiscussMapper<T,P> {
    Integer insert(@Param("bean") T t);
    List<T> selectList(@Param("query")P p);
}
