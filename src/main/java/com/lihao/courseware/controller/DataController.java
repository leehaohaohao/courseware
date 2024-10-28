package com.lihao.courseware.controller;

import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.*;
import com.lihao.courseware.entity.query.HistoryPointQuery;
import com.lihao.courseware.entity.query.HistoryResultQuery;
import com.lihao.courseware.entity.query.HistoryTestQuery;
import com.lihao.courseware.entity.query.KnowledgeTestQuery;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.mapper.*;
import com.lihao.courseware.util.Cache;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 更改数据接口
 */
@RestController
@RequestMapping("/data")
@CrossOrigin
public class DataController extends BaseController{
    @Resource
    private KnowledgeMapper knowledgeMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private KnowledgePointMapper knowledgePointMapper;
    @Resource
    private KnowledgeTestMapper<KnowledgeTest, KnowledgeTestQuery> knowledgeTestMapper;
    @Resource
    private HistoryTestMapper<HistoryTest, HistoryTestQuery> historyTestMapper;
    @Resource
    private HistoryPointMapper<HistoryPoint, HistoryPointQuery> historyPointMapper;
    @Resource
    private HistoryResultMapper<HistoryResult, HistoryResultQuery> historyResultMapper;
    @Resource
    private UserAbilityMapper userAbilityMapper;
    @Resource
    private AbilityMapper abilityMapper;
    @Resource
    private UserTestMapper userTestMapper;
    @Resource
    private ChapterMapper chapterMapper;
    /**
     * 更改章节内容
     * @param msg 所更改内容
     * @param id 章节内容id
     * @return
     */
    @PostMapping("/update/knowledge")
    public ResponsePack updateK(String msg,Integer id){
        Knowledge knowledge = new Knowledge();
        knowledge.setInit(msg);
        knowledge.setContent(msg);
        knowledge.setId(id);
        return getSuccessResponse(knowledgeMapper.updateK(knowledge));
    }

    /**
     * 插入章节内容
     * @param msg 所插入内容
     * @param id 章节内容id
     * @return
     */
    @PostMapping("/insert/knowledge")
    public ResponsePack insertK(String msg,Integer id){

        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        int count = 0;
        for(UserInfo u :userInfos){
            Knowledge knowledge = new Knowledge();
            knowledge.setId(id);
            knowledge.setUserId(u.getUserId());
            knowledge.setInit(msg);
            knowledge.setContent(msg);
            count+=knowledgeMapper.insert(knowledge);
        }
        return getSuccessResponse(count);
    }

    /**
     * 更改知识点相关内容
     * @param content 内容
     * @param id id
     * @param title 标题
     * @return
     */
    @PostMapping("/update/point")
    public ResponsePack updateP(String content,Integer id,String title){
        KnowledgePoint knowledgePoint = new KnowledgePoint();
        knowledgePoint.setContent(content);
        knowledgePoint.setTitle(title);
        knowledgePoint.setId(id);
        return getSuccessResponse(knowledgePointMapper.updateP(knowledgePoint,id));
    }

    /**
     * 插入知识点相关内容
     * @param content 内容
     * @param id id
     * @param title 标题
     * @return
     */
    @PostMapping("/insert/point")
    public ResponsePack insertP(String content,Integer id,String title){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        int count = 0;
        for(UserInfo u :userInfos){
            KnowledgePoint knowledgePoint = new KnowledgePoint();
            knowledgePoint.setId(id);
            knowledgePoint.setJudge(false);
            knowledgePoint.setUserId(u.getUserId());
            knowledgePoint.setContent(content);
            knowledgePoint.setTitle(title);
            count += knowledgePointMapper.insert(knowledgePoint);
        }
        return getSuccessResponse(count);
    }

    /**
     * 插入章节测试相关内容
     * @param knowledgeTest 章节测试数据
     * @return
     */
    @PostMapping("/insert/test")
    public ResponsePack insertT(KnowledgeTest knowledgeTest){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        int count = 0;
        for(UserInfo u :userInfos){
            KnowledgeTest insert = new KnowledgeTest();
            insert.setDone(0);
            insert.setAnswer(knowledgeTest.getAnswer());
            insert.setId(knowledgeTest.getId());
            insert.setAnalysis(knowledgeTest.getAnalysis());
            insert.setType(knowledgeTest.getType());
            insert.setQuestion(knowledgeTest.getQuestion());
            insert.setUserId(u.getUserId());
            count+=knowledgeTestMapper.insert(insert);
        }
        return getSuccessResponse(count);
    }

    /**
     * 更改章节测试相关内容
     * @param knowledgeTest 章节测试数据
     * @return
     */
    @PostMapping("/update/test")
    public ResponsePack updateT(KnowledgeTest knowledgeTest){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        int count = 0;
        for (UserInfo u : userInfos) {
            KnowledgeTest update = new KnowledgeTest();
            KnowledgeTestQuery updateQ = new KnowledgeTestQuery();
            update.setDone(0);
            update.setAnswer(knowledgeTest.getAnswer().toUpperCase());
            update.setAnalysis(knowledgeTest.getAnalysis());
            update.setType(knowledgeTest.getType());
            update.setQuestion(knowledgeTest.getQuestion());
            updateQ.setUserId(u.getUserId());
            updateQ.setId(knowledgeTest.getId());
            count += knowledgeTestMapper.update(update, updateQ);
        }
        return getSuccessResponse(count);
    }

    /**
     * 为所有用户插入柱状图数据
     * @return
     */
    @PostMapping("/insert/all/hr")
    public ResponsePack insertAllHR(){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        List<HistoryPoint> points = historyPointMapper.selectAllId();
        List<HistoryTest> tests = historyTestMapper.selectAllId();
        int count = 0;
        for(UserInfo userInfo:userInfos){
            for(HistoryTest test:tests){
                for(HistoryPoint point:points){
                    HistoryResult hr = new HistoryResult();
                    hr.setId(CommonTools.getId());
                    hr.setUserId(userInfo.getUserId());
                    hr.setPId(point.getId());
                    hr.setTId(test.getId());
                    count += historyResultMapper.insert(hr);
                }
            }
        }
        return getSuccessResponse(count);
    }
    /**
     * 为单个用户插入柱状图数据
     * @param userId 用户id
     * @return
     */
    @PostMapping("/insert/hr")
    public ResponsePack insertHR(Long userId){
        List<HistoryPoint> points = historyPointMapper.selectAllId();
        List<HistoryTest> tests = historyTestMapper.selectAllId();
        int count = 0;
        for(HistoryTest test:tests){
            for(HistoryPoint point:points){
                HistoryResult hr = new HistoryResult();
                hr.setId(CommonTools.getId());
                hr.setUserId(userId);
                hr.setPId(point.getId());
                hr.setTId(test.getId());
                count += historyResultMapper.insert(hr);
            }
        }
        return getSuccessResponse(count);
    }

    /**
     * 为所有用户插入雷达图数据
     * @return
     */
    @PostMapping("/insert/all/radar")
    public ResponsePack insertAllRadar(){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        List<Ability> abilities = abilityMapper.selectList();
        int count = 0;
        for(UserInfo userInfo:userInfos){
            for(Ability ability:abilities){
                UserAbility userAbility = new UserAbility();
                userAbility.setId(CommonTools.getId());
                userAbility.setUserId(userInfo.getUserId());
                userAbility.setAId(ability.getId());
                userAbility.setScore(0);
                count += userAbilityMapper.insert(userAbility);
            }
        }
        return getSuccessResponse(count);
    }
    /**
     * 为单个用户插入雷达图数据
     * @return
     */
    @PostMapping("/insert/radar")
    public ResponsePack insertRadar(Long userId,Integer score){
        List<Ability> abilities = abilityMapper.selectList();
        int count = 0;
        for(Ability ability:abilities){
            UserAbility userAbility = new UserAbility();
            userAbility.setId(CommonTools.getId());
            userAbility.setUserId(userId);
            userAbility.setAId(ability.getId());
            userAbility.setScore(score);
            count += userAbilityMapper.insert(userAbility);
        }
        return getSuccessResponse(count);
    }

    /**
     * 为所有用户插入折线图数据
     * @return
     */
    @PostMapping("/insert/all/line")
    public ResponsePack insertAllLine(){
        List<UserInfo> userInfos = userInfoMapper.selectAllId();
        List<HistoryTest> tests = historyTestMapper.selectAllId();
        int count = 0;
        for(UserInfo userInfo:userInfos){
            for(HistoryTest test:tests){
                UserTest userTest = new UserTest();
                userTest.setId(CommonTools.getId());
                userTest.setUserId(userInfo.getUserId());
                userTest.setTId(test.getId());
                count+=userTestMapper.insert(userTest);
            }
        }
        return getSuccessResponse(count);
    }

    /**
     * 为单个用户插入折线图数据
     * @param userId
     * @return
     */
    @PostMapping("/insert/line")
    public ResponsePack insertLine(Long userId,Integer score){
        List<HistoryTest> tests = historyTestMapper.selectAllId();
        int count = 0;
        for(HistoryTest test:tests){
            UserTest userTest = new UserTest();
            userTest.setId(CommonTools.getId());
            userTest.setUserId(userId);
            userTest.setTId(test.getId());
            userTest.setScore(score);
            count+=userTestMapper.insert(userTest);
        }
        return getSuccessResponse(count);
    }

    /**
     * 获取全部试卷测试题
     * @return
     */
    @PostMapping("/select/chapter")
    public ResponsePack selectChapter(){
        return getSuccessResponse(Cache.chapters);
    }

    /**
     * 插入试卷测试题内容
     * @param content 章节测试内容
     * @return
     * @throws GlobalException
     */
    @PostMapping("/insert/chapter")
    public ResponsePack insertChapter(String content) throws GlobalException {
        if(!chapterMapper.insert(content).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
        Cache.chapters.clear();
        Cache.chapters=chapterMapper.selectList();
        return getSuccessResponse(null);
    }

    /**
     * 更改试卷试题内容
     * @param id
     * @param content
     * @return
     * @throws GlobalException
     */
    @PostMapping("/update/chapter")
    public ResponsePack updateChapter(Integer id,String content) throws GlobalException {
        Chapter chapter = new Chapter(id,content);
        if(!chapterMapper.update(chapter).equals(1)){
            throw new GlobalException(ExceptionConstants.SERVER_ERROR);
        }
        Cache.chapters.clear();
        Cache.chapters=chapterMapper.selectList();
        return getSuccessResponse(null);
    }
}
