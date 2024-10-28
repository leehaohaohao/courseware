package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.UserAbility;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAbilityMapper {
    UserAbility selectByUserIdByAbilityId(@Param("userId") Long userId,@Param("aId")Long aId);
    Integer insert(@Param("bean")UserAbility userAbility);
}
