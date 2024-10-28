package com.lihao.courseware.entity.query;

import lombok.Data;

@Data
public class UserInfoQuery {
    private Long userId;
    private String name;
    private String classname;
    private Integer identity;
    private String orderBy;
}
