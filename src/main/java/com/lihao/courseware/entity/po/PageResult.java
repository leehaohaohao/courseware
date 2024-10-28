package com.lihao.courseware.entity.po;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T>{
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;
    private int pages;
}
