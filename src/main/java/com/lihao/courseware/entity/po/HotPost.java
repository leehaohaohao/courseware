package com.lihao.courseware.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class HotPost {
    private Integer id;
    private String img;
    private String title;
    private String subTitle;
    private Date time;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentsCount;
    private Boolean isLike;
}
