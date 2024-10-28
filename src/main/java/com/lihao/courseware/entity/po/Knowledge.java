package com.lihao.courseware.entity.po;

import lombok.Data;

@Data
public class Knowledge {
    private Integer id;
    private String content;
    private String init;
    private Long userId;
}
