package com.lihao.courseware.service;

import com.lihao.courseware.entity.dto.DiscussDto;
import com.lihao.courseware.entity.po.Discuss;
import com.lihao.courseware.entity.po.PageResult;
import com.lihao.courseware.entity.query.DiscussQuery;

import java.util.List;

public interface DiscussService {
    void publish(Discuss discuss);
    PageResult<DiscussDto> list(DiscussQuery discussQuery);
}
