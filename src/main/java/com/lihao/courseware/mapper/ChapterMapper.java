package com.lihao.courseware.mapper;

import com.lihao.courseware.entity.po.Chapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/24--14:22
 * @since 1.0
 */
public interface ChapterMapper {
    List<Chapter> selectList();
    Integer insert(String content);
    Integer update(@Param("bean")Chapter chapter);
}
