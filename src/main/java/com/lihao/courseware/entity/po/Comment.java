package com.lihao.courseware.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long id;
    private Long userId;
    private String content;
    private Date time;
}
