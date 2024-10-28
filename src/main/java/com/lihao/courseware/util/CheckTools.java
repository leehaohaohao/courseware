package com.lihao.courseware.util;

import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.constants.StringConstants;
import com.lihao.courseware.entity.po.UserInfo;
import com.lihao.courseware.entity.query.UserInfoQuery;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class CheckTools {
    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    public  void checkPassword(String password) throws GlobalException {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$";
        if(!Pattern.matches(regex, password)){
            throw new GlobalException(ExceptionConstants.INVALID_PASSWORD_FORMAT);
        }
    }
    public void checkLogin() throws GlobalException {
        HttpServletRequest request = CommonTools.getRequest();
        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            throw new GlobalException(ExceptionConstants.EXPIRE_LOG);
        }
    }
    public void checkTeacher() throws GlobalException {
        HttpServletRequest request = CommonTools.getRequest();
        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            throw new GlobalException(ExceptionConstants.EXPIRE_LOG);
        }
        UserInfoQuery userInfoQuery= new UserInfoQuery();
        userInfoQuery.setUserId(Long.valueOf(token));
        UserInfo userInfo = userInfoMapper.selectById(userInfoQuery);
        if(userInfo == null ||userInfo.getIdentity().equals(1)){
            throw new GlobalException(ExceptionConstants.NO_PERMISSION);
        }
    }
}
