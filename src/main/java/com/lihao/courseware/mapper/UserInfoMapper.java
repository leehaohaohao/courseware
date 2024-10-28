package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper<T,Q> {
    UserInfo selectById(@Param("query")Q q);
    Integer insert(@Param("bean")T t);
    List<UserInfo> selectAllId();
    List<UserInfo> selectList(@Param("query")Q q);
}
