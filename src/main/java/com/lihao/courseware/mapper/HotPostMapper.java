package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.HotPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotPostMapper {
    List<HotPost> selectList();
}
