package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.UserTest;
import org.apache.ibatis.annotations.Param;

public interface UserTestMapper {
    Integer insert(@Param("bean")UserTest userTest);
    UserTest selectByUserIdByTestId(@Param("userId") Long userId, @Param("tId") Long tId);
}
