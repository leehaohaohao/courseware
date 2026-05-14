package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.Cjw;
import org.apache.ibatis.annotations.Insert;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2025/3/29--19:54
 * @since 1.0
 */
public interface CjwMapper {
    @Insert("insert into cjw(answer,date) values(#{answer},#{date})")
    void insert(Cjw cjw);
}
