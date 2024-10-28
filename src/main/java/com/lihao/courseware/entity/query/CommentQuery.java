package com.lihao.courseware.entity.query;

import com.lihao.courseware.entity.po.Page;
import lombok.Data;

import java.util.Date;
@Data
public class CommentQuery {
    private String orderBy;
    private Page page;
    private Long id;
    private Long userId;
    private String content;
    private Date time;
}
