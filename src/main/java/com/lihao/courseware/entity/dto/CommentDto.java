package com.lihao.courseware.entity.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private String name;
    private String avatar;
    private Integer status;
}
