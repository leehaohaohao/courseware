package com.lihao.courseware.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.exception.GlobalException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class CommonTools {
    @Resource
    private  AppConfig appConfig;
    public static long getId(){
        Snowflake snowflake = IdUtil.getSnowflake();
        return snowflake.nextId();
    }
    /**
     * 判断是否为空或者为null
     * @param vars 判断的字符串数组
     * @return
     */
    public static Boolean isBlank(String...vars){
        for(String var:vars){
            if(var == null || var.isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取请求
     * @return
     * @throws GlobalException
     */
    public static HttpServletRequest getRequest() throws GlobalException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
        return attributes.getRequest();
    }
    public static Long getUserId() throws GlobalException {
        HttpServletRequest request = getRequest();
        String token = request.getHeader("token");
        Long userId = null;
        if(token != null && !token.isEmpty()){
            userId = Long.valueOf(token);
        }
        return userId;
        /*Cookie[] cookies = request.getCookies();
        Long userId = null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(StringConstants.TOKEN)){
                userId = Long.valueOf(cookie.getValue().substring(36));
            }
        }
        return userId;*/
    }
    public  String getChartPath(){
        return appConfig.getPath();
    }
    public static String charSelect(String msg){
        String regex = "<!DOCTYPE html>(.*?)</html>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "No HTML code found.";
        }
    }
    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            return address.isReachable(2000); // 2 seconds timeout
        } catch (IOException e) {
            return false;
        }
    }
}
