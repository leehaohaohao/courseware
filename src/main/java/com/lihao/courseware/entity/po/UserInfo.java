package com.lihao.courseware.entity.po;

import lombok.Data;

@Data
public class UserInfo {
    private Long userId;
    private String name;
    private String classname;
    private String password;
    private String avatar;
    private Integer identity;
    private Integer task;
    private Integer ntask;
    private Integer discuss;
    private Integer study;
    private Integer day;
}
