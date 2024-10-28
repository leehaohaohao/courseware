package com.lihao.courseware.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class KnowledgeCard {
    private Integer id;
    private String title;
    private String content;
    private Date time;
    private Long userId;
}
