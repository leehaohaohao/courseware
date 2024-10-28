package com.lihao.courseware.entity.query;

import lombok.Data;

@Data
public class KnowledgeTestQuery {
    private Integer id;
    private Long userId;
    private Integer done;
    private String answer;
    private String sheet;
    private String analysis;
    private String question;
    private Integer type;
    private String ans;
}
