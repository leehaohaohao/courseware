package com.lihao.courseware.controller;

import com.lihao.courseware.annotation.Teacher;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.entity.dto.HistoryResultDto;
import com.lihao.courseware.entity.dto.LineDto;
import com.lihao.courseware.entity.dto.RadarDto;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.*;
import com.lihao.courseware.entity.query.HistoryPointQuery;
import com.lihao.courseware.entity.query.HistoryResultQuery;
import com.lihao.courseware.entity.query.HistoryTestQuery;
import com.lihao.courseware.entity.query.UserInfoQuery;
import com.lihao.courseware.entity.vo.UserInfoVo;
import com.lihao.courseware.mapper.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 教师端接口
 */
@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController extends BaseController{
    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
    @Resource
    private HistoryTestMapper<HistoryTest, HistoryTestQuery> historyTestMapper;
    @Resource
    private HistoryPointMapper<HistoryPoint,HistoryPointQuery> historyPointMapper;
    @Resource
    private HistoryResultMapper<HistoryResult, HistoryResultQuery> historyResultMapper;
    @Resource
    private AbilityMapper abilityMapper;
    @Resource
    private UserAbilityMapper userAbilityMapper;
    @Resource
    private AppConfig appConfig;
    @Resource
    private UserTestMapper userTestMapper;
    private List<HistoryPoint> points = new ArrayList<>();
    private List<HistoryTest> tests = new ArrayList<>();
    private List<Ability> abilities = new ArrayList<>();
    @PostConstruct
    private void init(){
        points = historyPointMapper.selectAllId();
        tests = historyTestMapper.selectAllId();
        abilities = abilityMapper.selectList();
    }
    /**
     * 获取学生列表
     * @return
     */
    @RequestMapping("/student/list")
    @Teacher
    public ResponsePack studentList(){
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setIdentity(1);
        userInfoQuery.setOrderBy("task desc");
        List<UserInfo> userInfos = userInfoMapper.selectList(userInfoQuery);
        AtomicInteger index = new AtomicInteger();
        List<UserInfoVo> userInfoVos = userInfos.stream()
                .map(userInfo -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(userInfo,userInfoVo);
                    userInfoVo.setId(String.valueOf(userInfo.getUserId()));
                    String tasks = userInfo.getTask() + "/" + (userInfo.getNtask()+userInfo.getTask());
                    userInfoVo.setTasks(tasks);
                    userInfoVo.setRank(index.incrementAndGet());
                    return userInfoVo;
                }).toList();
        return getSuccessResponse(userInfoVos);
    }

    /**
     * 获取柱状图数据
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/get/histogram")
    public ResponsePack histogram(Long userId){
        HistoryResultQuery historyResultQuery = new HistoryResultQuery();
        historyResultQuery.setUserId(userId);
        List<HistoryResult> historyResults = historyResultMapper.selectByUserId(historyResultQuery);
        int index = historyResults.size();
        int column = index/ points.size()+1;
        int row = index/ tests.size()+1;
        String[][] x = new String[row][column];
        String[] y = new String[column];
        x[0][0]="错误知识点率";
        y[0]="错误知识点率";
        for(int i = 1;i<column;i++){
            String var = tests.get(i-1).getTest();
            x[0][i]=var;
            y[i]=var;
        }
        for(int i = 1;i<row;i++){
            x[i][0]=points.get(i-1).getPoint();
        }
        int pos = 0;
        for(int i = 1;i<column;i++){
            for(int j = 1;j<row;j++){
                x[j][i]= String.valueOf(historyResults.get(pos++).getMistake());
            }
        }
        HistoryResultDto historyResultDto = new HistoryResultDto();
        historyResultDto.setX(x);
        historyResultDto.setY(y);
        return getSuccessResponse(historyResultDto);
    }

    /**
     * 获取雷达图数据
     * @param userId
     * @return
     */
    @GetMapping("/get/radar")
    public ResponsePack radar(Long userId){
        Integer[] y = new Integer[abilities.size()];
        for(int i = 0;i<abilities.size();i++){
            y[i]=userAbilityMapper.selectByUserIdByAbilityId(
                    userId,abilities.get(i).getId()
            ).getScore();
        }
        RadarDto radarDto = new RadarDto();
        radarDto.setAbilities(abilities);
        radarDto.setY(y);
        return getSuccessResponse(radarDto);
    }

    /**
     * 获取折线图数据
     * @param userId 用户id
     * @return
     */
    @GetMapping("/get/line")
    public ResponsePack line(Long userId){
        String[] x = new String[tests.size()];
        Integer[] y = new Integer[tests.size()];
        int index = 0;
        for(HistoryTest test:tests){
            UserTest userTest = userTestMapper.selectByUserIdByTestId(userId,test.getId());
            x[index]=test.getTest();
            y[index++]=userTest.getScore();
        }
        LineDto lineDto = new LineDto();
        lineDto.setX(x);
        lineDto.setY(y);
        return getSuccessResponse(lineDto);
    }

    /**
     * 获取pdf
     * @param id id
     * @return
     */
    @PostMapping("/pdf")
    public ResponsePack pdf(Integer id){
        return getSuccessResponse(appConfig.getUri()+"pdf/"+(id+1)+".pdf");
    }
}
