package com.lihao.courseware.entity.query;

import lombok.Data;

@Data
public class HistoryResultQuery {
    private Long id;
    private Long userId;
    private Long tId;
    private Long pId;
    private Integer mistake;
    private String orderBy;
}
