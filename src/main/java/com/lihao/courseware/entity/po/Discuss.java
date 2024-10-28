package com.lihao.courseware.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class Discuss {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Date time;
    private Date commentTime;
}
