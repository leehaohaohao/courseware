package com.lihao.courseware.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2025/3/29--19:53
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cjw {
    private Integer id;
    private String answer;
    private Date date;
}
