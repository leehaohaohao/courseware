package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.KnowledgePoint;
import org.apache.ibatis.annotations.Param;

public interface KnowledgePointMapper {
    KnowledgePoint selectById(Integer id,Long userId);
    Integer updateById(@Param("query") KnowledgePoint knowledgePoint,@Param("id") Integer id);
    Integer insert(@Param("bean")KnowledgePoint knowledgePoint);
    Integer updateP(@Param("query") KnowledgePoint knowledgePoint,@Param("id") Integer id);
}
