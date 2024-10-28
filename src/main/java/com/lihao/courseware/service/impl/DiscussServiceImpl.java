package com.lihao.courseware.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lihao.courseware.entity.dto.DiscussDto;
import com.lihao.courseware.entity.po.Discuss;
import com.lihao.courseware.entity.po.Page;
import com.lihao.courseware.entity.po.PageResult;
import com.lihao.courseware.entity.po.UserInfo;
import com.lihao.courseware.entity.query.DiscussQuery;
import com.lihao.courseware.entity.query.UserInfoQuery;
import com.lihao.courseware.mapper.DiscussMapper;
import com.lihao.courseware.mapper.UserInfoMapper;
import com.lihao.courseware.service.DiscussService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Resource
    private DiscussMapper<Discuss,DiscussQuery> discussMapper;
    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    @Override
    @Transactional
    public void publish(Discuss discuss) {
        discussMapper.insert(discuss);
    }

    @Override
    public PageResult<DiscussDto> list(DiscussQuery discussQuery) {
        Page page = discussQuery.getPage();
        discussQuery.setPage(null);
        PageHelper.startPage(page.getPageSize(), page.getPageNum());
        List<Discuss> discusss = discussMapper.selectList(discussQuery);
        PageInfo<Discuss> pageInfo = new PageInfo<>(discusss);

        List<DiscussDto> discussDtos = discusss.parallelStream().map(discuss -> {
            DiscussDto dto = new DiscussDto();
            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserId(discuss.getUserId());
            UserInfo userInfo = userInfoMapper.selectById(userInfoQuery);
            BeanUtils.copyProperties(discuss, dto);
            dto.setName(userInfo.getName());
            dto.setAvatar(userInfo.getAvatar());
            return dto;
        }).collect(Collectors.toList());

        PageResult<DiscussDto> pageResult = new PageResult<>();
        pageResult.setList(discussDtos);
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }
}
