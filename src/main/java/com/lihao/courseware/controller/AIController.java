package com.lihao.courseware.controller;

import com.lihao.courseware.annotation.Time;
import com.lihao.courseware.api.aliyun.BaiDu;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.service.KnowledgeBaseService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * ai接口
 */
@RestController
@CrossOrigin
@RequestMapping("/ai")
public class AIController extends BaseController {
    @Resource
    private KnowledgeBaseService knowledgeBaseService;
    /**
     * 聊天
     * @param msg 问题
     * @return
     */
    @Time
    @RequestMapping("/chat")
    public ResponsePack chat(String msg) {
        return getSuccessResponse(knowledgeBaseService.search(msg));
    }
}
