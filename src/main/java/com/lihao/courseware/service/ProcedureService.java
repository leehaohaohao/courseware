package com.lihao.courseware.service;

import com.lihao.courseware.entity.po.Challenge;
import com.lihao.courseware.entity.po.HotPost;

import java.util.List;

public interface ProcedureService {
    List<HotPost> getHotPost();
    List<Challenge> getChallenge();
}
