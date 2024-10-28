package com.lihao.courseware.entity.dto;

import lombok.Data;

@Data
public class ResponsePack<T> {
    /**
     * 请求是否处理成功
     */
    private boolean success;
    /**
     * 请求成功条件下返回封装的数据
     */
    private T data;
    /**
     * 请求失败的条件下返回的错误信息（对用户操作错误的提示，如账号密码错误等）
     */
    private String error;
}
