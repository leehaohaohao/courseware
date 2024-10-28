package com.lihao.courseware.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryResultMapper <T,Q>{
    List<T> selectByUserId(@Param("query")Q q);
    Integer insert(@Param("bean")T t);
}
