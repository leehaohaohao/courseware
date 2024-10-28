package com.lihao.courseware.entity.dto;

import com.lihao.courseware.entity.po.Question;
import lombok.Data;

import java.util.List;

@Data
public class KnowledgeTestDto {
    private Integer id;
    private Long userId;
    private Integer done;
    private String answer;
    private String sheet;
    private String analysis;
    private List<Question> question;
    private Integer type;
}
