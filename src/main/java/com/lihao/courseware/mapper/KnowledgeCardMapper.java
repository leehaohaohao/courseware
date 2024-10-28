package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.KnowledgeCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KnowledgeCardMapper {
    List<KnowledgeCard> selectListById(Long userId);
    Integer insert(@Param("query")KnowledgeCard knowledgeCard);
}
