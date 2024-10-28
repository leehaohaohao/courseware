package com.lihao.courseware.controller;


import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.Log;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 登陆、注册
 */
@RestController
@CrossOrigin
public class LogController extends BaseController {
    @Resource
    private LogService logService;
    /**
     * 登陆
     * @param log 姓名，班级，密码，身份码
     * @return
     */
    @PostMapping("/login")
    public ResponsePack login(@RequestBody Log log) throws GlobalException {
        Long userId = logService.login(log);
        return getSuccessResponse(String.valueOf(userId));
    }

    /**
     * 注册
     * @param log 姓名，班级，密码，身份码
     * @return
     */
    @PostMapping("/register")
    public ResponsePack register(@RequestBody Log log) throws GlobalException {
        logService.register(log);
        return getSuccessResponse(null);
    }
}
