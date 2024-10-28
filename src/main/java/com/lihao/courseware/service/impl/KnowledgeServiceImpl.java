package com.lihao.courseware.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.po.*;
import com.lihao.courseware.entity.query.KnowledgeTestQuery;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.mapper.*;
import com.lihao.courseware.service.KnowledgeService;
import com.lihao.courseware.util.Cache;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {
    private static final Logger logger = LoggerFactory.getLogger(KnowledgeServiceImpl.class);
    @Resource
    private KnowledgePointMapper knowledgePointMapper;
    @Resource
    private KnowledgeMapper knowledgeMapper;
    @Resource
    private KnowledgeCardMapper knowledgeCardMapper;
    @Resource
    private KnowledgeTestMapper<KnowledgeTest, KnowledgeTestQuery> knowledgeTestMapper;
    @Resource
    private AppConfig appConfig;
    private List<List<String>> lists = new ArrayList<>();
    private List<String> i0 = new ArrayList<>();
    private List<String> i1 = new ArrayList<>();
    private List<String> i2 = new ArrayList<>();
    private List<String> i3 = new ArrayList<>();
    private List<String> i4 = new ArrayList<>();
    private List<String> i5 = new ArrayList<>();

    @PostConstruct
    private void init(){
        String baseUri = appConfig.getUri();
        for(int i = 0;i<=14;i++){
            i0.add(baseUri+"course1/"+"c"+i+".png");
        }
        lists.add(i0);
        for(int i = 1;i<=15;i++){
            i1.add(baseUri+"course2/"+"c"+i+".png");
        }
        lists.add(i1);
        for(int i = 1;i<=15;i++){
            i2.add(baseUri+"course3/"+i+".png");
        }
        lists.add(i2);
        for(int i = 1;i<=15;i++){
            i3.add(baseUri+"course4/"+i+".png");
        }
        lists.add(i3);
        for(int i = 1;i<=15;i++){
            i4.add(baseUri+"course5/"+i+".png");
        }
        lists.add(i4);
        for(int i = 1;i<=15;i++){
            i5.add(baseUri+"course6/"+i+".png");
        }
        lists.add(i5);
    }
    @Override
    public KnowledgePoint getPoint(Integer id) throws GlobalException {
        Long userId = CommonTools.getUserId();
        KnowledgePoint knowledgePoint = knowledgePointMapper.selectById(id,userId);
        knowledgePoint.setJudge(true);
        updateKnowledgePoint(knowledgePoint);
        return knowledgePoint;
    }

    @Override
    public Knowledge get(Integer id) throws GlobalException {
        Long userId = CommonTools.getUserId();
        return knowledgeMapper.selectById(id,userId);
    }

    @Override
    @Transactional
    public void updateKnowledgePoint(KnowledgePoint knowledgePoint) throws GlobalException {
        if(!knowledgePointMapper.updateById(knowledgePoint,knowledgePoint.getId()).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateKnowledge(Knowledge knowledge) throws GlobalException {
        if(!knowledgeMapper.updateById(knowledge,knowledge.getId()).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
    }

    @Override
    public List<String> getCard(Long userId) {
        List<KnowledgeCard> cards = knowledgeCardMapper.selectListById(userId);
        List<String> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int index = 1;
        for (KnowledgeCard card : cards) {
            result.add(String.valueOf(index));
            result.add(dateFormat.format(new Date(String.valueOf(card.getTime()))));
            result.add(card.getTitle());
            result.add(card.getContent());
            result.add("");
            index++;
        }
        return result;
    }

    @Override
    @Transactional
    public void insertCard(KnowledgeCard knowledgeCard) throws GlobalException {
        if(!knowledgeCardMapper.insert(knowledgeCard).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
    }

    @Override
    public KnowledgeTest getTest(Integer id,Long userId,Integer type) {
        KnowledgeTestQuery knowledgeTestQuery = new KnowledgeTestQuery();
        logger.error("userId: "+userId+" type: "+type+" id: "+id);
        knowledgeTestQuery.setUserId(userId);
        knowledgeTestQuery.setId(id);
        knowledgeTestQuery.setType(type);
        KnowledgeTest knowledgeTest = knowledgeTestMapper.selectById(knowledgeTestQuery);
        return knowledgeTest;
    }

    @Override
    @Transactional
    public JudgeResult judgeTest(Integer id, Long userId, Integer type, String sheet) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        /*Map<Integer,Character> map = new HashMap<>();
        map.put(0,'A');
        map.put(1,'B');
        map.put(2,'C');
        map.put(3,'D');*/
        sheet = sheet.toLowerCase();
        KnowledgeTest knowledgeTest = getTest(id,userId,type);
        Boolean[][] jsonResult = objectMapper.readValue(sheet,Boolean[][].class);
        Boolean[][] jsonAnswer = objectMapper.readValue(knowledgeTest.getAnswer().toLowerCase(),Boolean[][].class);
        /*String answer = knowledgeTest.getAnswer().toUpperCase();
        int n = answer.length();*/
        /*Boolean[][] result = new Boolean[n][4];
        Boolean[][] ok = new Boolean[n][4];*/
        /*for(int i = 0;i<n;i++){
                for(int j = 0;j<4;j++){
                    result[i][j]= map.get(j) == sheet.charAt(i);
                }
        }
        for(int i = 0;i<n;i++){
            for(int j = 0;j<4;j++){
                ok[i][j]= map.get(j) == answer.charAt(i);
            }
        }*/
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setJudge(jsonResult);
        judgeResult.setAnalysis(knowledgeTest.getAnalysis());
        judgeResult.setAnswer(knowledgeTest.getAnswer().toLowerCase());
        judgeResult.setOk(jsonAnswer);
        judgeResult.setAns(knowledgeTest.getAns());
        KnowledgeTest updateTest = new KnowledgeTest();
        updateTest.setDone(1);
        updateTest.setSheet(sheet);
        KnowledgeTestQuery knowledgeTestQuery = new KnowledgeTestQuery();
        knowledgeTestQuery.setUserId(userId);
        knowledgeTestQuery.setId(id);
        knowledgeTestQuery.setType(type);
        knowledgeTestMapper.update(updateTest,knowledgeTestQuery);
        return judgeResult;
    }

    @Override
    public List<String> getImg(Integer id) {
        return lists.get(id);
    }

    @Override
    public List<Chapter> getChapter(Integer size) {
        List<Chapter> chapters = Cache.chapters;
        Set<Integer> set = new HashSet<>();
        Random random = new Random();
        if(size>chapters.size()){
            return chapters;
        }
        while (set.size()<size){
            set.add(random.nextInt(chapters.size()));
        }
        List<Chapter> result = new ArrayList<>();
        for(Integer i : set){
            result.add(chapters.get(i));
        }
        // 打乱结果列表的顺序
        Collections.shuffle(result);
        return result;
    }
}
