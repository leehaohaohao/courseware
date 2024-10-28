package com.lihao.courseware.controller;

import com.lihao.courseware.entity.dto.ResponsePack;

/**
 * 基本控制类，用来提取控制类公共代码
 * @param <T>
 */
public class BaseController<T> {
    protected ResponsePack<T> getSuccessResponse(T data){
        ResponsePack<T> responsePack = new ResponsePack<>();
        responsePack.setData(data);
        responsePack.setSuccess(true);
        return responsePack;
    }
    protected ResponsePack<T> getFailResponse(String error){
        ResponsePack<T> responsePack = new ResponsePack<>();
        responsePack.setError(error);
        responsePack.setSuccess(false);
        return responsePack;
    }
}
