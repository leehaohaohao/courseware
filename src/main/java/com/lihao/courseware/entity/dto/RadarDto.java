package com.lihao.courseware.entity.dto;

import com.lihao.courseware.entity.po.Ability;
import lombok.Data;

import java.util.List;

@Data
public class RadarDto {
    private List<Ability> abilities;
    private Integer[] y;
}
