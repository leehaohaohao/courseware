package com.lihao.courseware.entity.query;

import com.lihao.courseware.entity.po.Page;
import lombok.Data;

import java.util.Date;

@Data
public class DiscussQuery {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Date time;
    private Date commentTime;
    private String orderBy;
    private Page page;
}
