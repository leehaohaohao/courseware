package com.lihao.courseware.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lihao.courseware.entity.po.*;
import com.lihao.courseware.exception.GlobalException;

import java.util.List;

public interface KnowledgeService {
    KnowledgePoint getPoint(Integer id) throws GlobalException;
    Knowledge get(Integer id) throws GlobalException;
    void updateKnowledgePoint (KnowledgePoint knowledgePoint) throws GlobalException;
    void updateKnowledge(Knowledge knowledge)throws GlobalException;
    List<String> getCard(Long userId);
    void insertCard(KnowledgeCard knowledgeCard) throws GlobalException;
    KnowledgeTest getTest(Integer id,Long userId,Integer type);
    JudgeResult judgeTest(Integer id, Long userId, Integer type, String sheet) throws JsonProcessingException;
    List<String> getImg(Integer id);
    List<Chapter> getChapter(Integer size);
}
