package com.lihao.courseware.entity.vo;

import lombok.Data;

@Data
public class UserInfoVo {
    private String id;
    private String name;
    private Integer discuss;
    private Integer study;
    private Integer day;
    private String tasks;
    private Integer rank;
}
