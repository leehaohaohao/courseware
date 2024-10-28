package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.Knowledge;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeMapper {
    Knowledge selectById(Integer id,Long userId);
    Integer updateById(@Param("query")Knowledge knowledge,@Param("id")Integer id);
    Integer insert(@Param("bean") Knowledge knowledge);
    Integer updateK(@Param("query")Knowledge knowledge);
}
