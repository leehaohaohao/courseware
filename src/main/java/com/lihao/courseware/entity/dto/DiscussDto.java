package com.lihao.courseware.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DiscussDto {
    private Long id;
    private String title;
    private String content;
    private Date time;
    private String name;
    private String avatar;
}
