package com.lihao.courseware.handler;

import com.baidubce.qianfan.model.exception.ApiException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.ResponsePack;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponsePack<String> clasp(Exception e){
        e.printStackTrace();
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setSuccess(false);
        if(e instanceof PersistenceException){
            responsePack.setError(ExceptionConstants.INVALID_PARAM);
        }else if(e instanceof DataIntegrityViolationException){
            responsePack.setError(ExceptionConstants.INVALID_PARAM);
        }else if(e instanceof NullPointerException){
            responsePack.setError(ExceptionConstants.INVALID_PARAM);
        }else if(e instanceof NoResourceFoundException){
            responsePack.setError(ExceptionConstants.INVALID_RESOURCE);
        }else if(e instanceof QueryTimeoutException){
            responsePack.setError(ExceptionConstants.SERVER_ERROR);
        }else if(e instanceof ApiException){
            responsePack.setError(ExceptionConstants.INVALID_FILE);
        } else if(e instanceof JsonMappingException){
            responsePack.setError(ExceptionConstants.SERVER_ERROR);
        }else{
            responsePack.setError(e.getMessage());
        }
        return responsePack;
    }
}
