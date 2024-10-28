package com.lihao.courseware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihao.courseware.annotation.Login;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.*;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.service.KnowledgeService;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 章节、知识点、卡片、章节测试
 * @author lihao
 */
@CrossOrigin
@RestController
@RequestMapping("/knowledge")
@SuppressWarnings("unchecked")
public class KnowledgeController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
    @Resource
    private KnowledgeService knowledgeService;
    /**
     * 获取知识点弹窗内容
     * @param id 知识点唯一标识
     * @return
     */
    @RequestMapping("/get/point")
    @Login
    public ResponsePack getPoint(Integer id) throws GlobalException {
        KnowledgePoint knowledgePoint = knowledgeService.getPoint(id);
        knowledgePoint.setId(null);
        return getSuccessResponse(knowledgePoint);
    }
    /**
     * 获取章节学习内容
     * @param id 章节唯一标识id
     * @return
     */
    @RequestMapping("/get")
    @Login
    public ResponsePack get(Integer id) throws GlobalException {
        Knowledge knowledge = knowledgeService.get(id);
        knowledge.setId(null);
        return getSuccessResponse(knowledge);
    }
    /**
     * 更改知识弹窗相关内容
     * @param knowledgePoint 需要更新哪个传哪个，id必须有，接受json
     * @return 空 或者 报错内容
     * @throws GlobalException 异常处理
     */
    @PostMapping("/update/point")
    @Login
    public ResponsePack updatePoint(@RequestBody KnowledgePoint knowledgePoint) throws GlobalException {
        if(knowledgePoint == null || knowledgePoint.getId()==null){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        knowledgePoint.setUserId(CommonTools.getUserId());
        knowledgeService.updateKnowledgePoint(knowledgePoint);
        return getSuccessResponse(null);
    }
    /**
     * 更改章节学习相关内容
     * @param knowledge 需要更新哪个传哪个，id必须有，接受json
     * @return 空 或者 报错内容
     * @throws GlobalException 异常处理
     */
    @PostMapping("/update")
    @Login
    public ResponsePack update(@RequestBody Knowledge knowledge) throws GlobalException {
        if(knowledge == null || knowledge.getId()==null){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        knowledge.setUserId(CommonTools.getUserId());
        knowledgeService.updateKnowledge(knowledge);
        return getSuccessResponse(null);
    }

    /**
     * 获取知识点卡片
     * @return
     * @throws GlobalException
     */
    @PostMapping("/get/card")
    @Login
    public ResponsePack getCard() throws GlobalException {
        return getSuccessResponse(knowledgeService.getCard(CommonTools.getUserId()));
    }

    /**
     * 插入知识点卡片
     * @param knowledgeCard 知识点卡片
     * @return
     * @throws GlobalException
     */
    @PostMapping("/insert/card")
    @Login
    public ResponsePack insertCard(@RequestBody KnowledgeCard knowledgeCard) throws GlobalException {
        if(CommonTools.isBlank(knowledgeCard.getContent(),knowledgeCard.getTitle())){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        knowledgeCard.setUserId(CommonTools.getUserId());
        knowledgeCard.setTime(new Date());
        knowledgeService.insertCard(knowledgeCard);
        return getSuccessResponse(null);
    }

    /**
     * 获取章节测试内容
     * @param judgeTest 章节测试内容
     * @return
     * @throws GlobalException
     */
    @PostMapping("/test")
    @Login
    public ResponsePack getTest(@RequestBody JudgeTest judgeTest) throws GlobalException{
        Long userId = CommonTools.getUserId();
        logger.error("userId: "+userId+" type: "+judgeTest.getType()+" id: "+judgeTest.getId());
        KnowledgeTest knowledgeTest = knowledgeService.getTest(judgeTest.getId(),userId,judgeTest.getType());
        if(knowledgeTest.getDone().equals(0)){
            knowledgeTest.setAnswer(null);
            knowledgeTest.setSheet(null);
            knowledgeTest.setAnalysis(null);
        }
        return getSuccessResponse(knowledgeTest);
    }
    /**
     * 章节测试判题
     * @param judgeTest
     * @return
     * @throws GlobalException
     */
    @PostMapping("/test/judge")
    @Login
    public ResponsePack judgeTest(@RequestBody JudgeTest judgeTest) throws GlobalException, JsonProcessingException {
        return getSuccessResponse(knowledgeService.judgeTest(judgeTest.getId(),CommonTools.getUserId(),judgeTest.getType(),judgeTest.getSheet()));
    }

    /**
     * 获取章节内容pdf图片地址
     * @param id 章节id
     * @return
     */
    @PostMapping("/img")
    @Login
    public ResponsePack img(Integer id){
        return getSuccessResponse(knowledgeService.getImg(id));
    }

    /**
     * 随机获取章节测试题
     * @param size 题目数量
     * @return
     * @throws GlobalException
     */
    @PostMapping("/random/test")
    @Login
    public ResponsePack randomTest(Integer size) throws GlobalException {
        return getSuccessResponse(knowledgeService.getChapter(size));
    }
}
