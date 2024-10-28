package com.lihao.courseware.util;

import com.lihao.courseware.entity.po.Chapter;
import com.lihao.courseware.mapper.ChapterMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/24--14:52
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class Cache {
    private final ChapterMapper chapterMapper;
    public static List<Chapter> chapters;
    @PostConstruct
    private void init(){
        chapters = chapterMapper.selectList();
    }
}
