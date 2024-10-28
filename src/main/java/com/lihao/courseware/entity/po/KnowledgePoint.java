package com.lihao.courseware.entity.po;

import lombok.Data;

@Data
public class KnowledgePoint {
    /**
     * 知识点唯一标识
     */
    private Integer id;
    /**
     * 知识点标题
     */
    private String title;
    /**
     * 知识点内容
     */
    private String content;
    /**
     * 判断布尔值
     */
    private Boolean judge;
    /**
     * 用户id
     */
    private Long userId;
}
