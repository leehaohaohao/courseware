package com.lihao.courseware.controller;

import com.lihao.courseware.annotation.Login;
import com.lihao.courseware.constants.ExceptionConstants;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.entity.po.Discuss;
import com.lihao.courseware.entity.po.Page;
import com.lihao.courseware.entity.query.DiscussQuery;
import com.lihao.courseware.enums.DiscussEnum;
import com.lihao.courseware.exception.GlobalException;
import com.lihao.courseware.service.DiscussService;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 讨论中心
 */
@RestController
@RequestMapping("/discuss")
@CrossOrigin
public class DiscussController extends BaseController{
    @Resource
    private DiscussService discussService;

    /**
     * 发布讨论
     * @param discuss 只需标题、内容
     * @return
     * @throws GlobalException
     */
    @PostMapping("/publish")
    @Login
    public ResponsePack publish(@RequestBody Discuss discuss) throws GlobalException {
        if(discuss ==null || CommonTools.isBlank(discuss.getTitle(),discuss.getContent())){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        Long userId = CommonTools.getUserId();
        Date curDate = new Date();
        discuss.setId(CommonTools.getId());
        discuss.setUserId(userId);
        discuss.setTime(curDate);
        discuss.setCommentTime(curDate);
        discussService.publish(discuss);
        return getSuccessResponse(null);
    }

    /**
     * 获取列表
     * @param page 分页信息
     * @return
     * @throws GlobalException
     */
    @PostMapping("/list")
    @Login
    public ResponsePack list(@RequestBody Page page) throws GlobalException {
        if(page == null || page.getPageNum()==null || page.getPageSize()==null){
            throw new GlobalException(ExceptionConstants.INVALID_PARAM);
        }
        if(page.getSort()==null){
            page.setSort(0);
        }
        DiscussQuery discussQuery = new DiscussQuery();
        discussQuery.setPage(page);
        discussQuery.setOrderBy(DiscussEnum.getDiscussEnumBySort(page.getSort()).getType());
        return getSuccessResponse(discussService.list(discussQuery));
    }
}
