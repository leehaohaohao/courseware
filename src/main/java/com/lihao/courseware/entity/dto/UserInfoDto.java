package com.lihao.courseware.entity.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private Long userId;
    private String name;
    private String email;
    private Integer task;
    private Integer ntask;
    private Integer discuss;
    private Integer study;
    private Integer day;
}
