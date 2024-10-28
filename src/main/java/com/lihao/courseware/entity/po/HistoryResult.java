package com.lihao.courseware.entity.po;

import lombok.Data;

@Data
public class HistoryResult {
    private Long id;
    private Long userId;
    private Long tId;
    private Long pId;
    private Integer mistake;
}
