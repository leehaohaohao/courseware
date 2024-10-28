package com.lihao.courseware.entity.po;

import lombok.Data;

@Data
public class JudgeResult {
    private String answer;
    private String analysis;
    private Boolean[][] judge;
    private Boolean[][] ok;
    private String ans;
}
