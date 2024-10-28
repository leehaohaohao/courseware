package com.lihao.courseware.service.impl;

import com.lihao.courseware.entity.po.Challenge;
import com.lihao.courseware.entity.po.HotPost;
import com.lihao.courseware.mapper.ChallengeMapper;
import com.lihao.courseware.mapper.HotPostMapper;
import com.lihao.courseware.service.ProcedureService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcedureServiceImpl implements ProcedureService {
    @Resource
    private HotPostMapper hotPostMapper;
    @Resource
    private ChallengeMapper challengeMapper;
    @Override
    public List<HotPost> getHotPost() {
        return hotPostMapper.selectList();
    }

    @Override
    public List<Challenge> getChallenge() {
        return challengeMapper.selectList();
    }
}
