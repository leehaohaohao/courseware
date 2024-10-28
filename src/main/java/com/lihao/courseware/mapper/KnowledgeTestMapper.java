package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.KnowledgeTest;
import com.lihao.courseware.entity.po.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeTestMapper<T,Q> {
    KnowledgeTest selectById(@Param("query")Q q);
    Integer insert(@Param("bean")T t);
    Integer update(@Param("bean")T t,@Param("query")Q q);
}
