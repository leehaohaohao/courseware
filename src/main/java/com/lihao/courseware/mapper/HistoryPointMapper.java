package com.lihao.courseware.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryPointMapper<T,Q>{
    T selectById(@Param("id")Long id);
    List<T> selectAllId();
}
