package com.lihao.courseware.controller;

import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.Challenge;
import com.lihao.courseware.service.ProcedureService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序
 */
@CrossOrigin
@RestController
@RequestMapping("/procedure")
public class ProcedureController extends BaseController{
    @Resource
    private ProcedureService procedureService;

    /**
     * 获取热门帖子列表
     * @return 热门帖子列表
     */
    @GetMapping("/hot/post")
    public ResponsePack getHotPost(){
        return getSuccessResponse(procedureService.getHotPost());
    }

    /**
     * 获取挑战题目图片
     * @return 挑战题目图片列表
     */
    @GetMapping("/challenge")
    public ResponsePack getChallenge(){
        List<Challenge> challenges = procedureService.getChallenge();
        for(Challenge challenge:challenges){
            challenge.setId(null);
        }
        return getSuccessResponse(challenges);
    }
}
