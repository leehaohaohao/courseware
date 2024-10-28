package com.lihao.courseware.service;

import com.lihao.courseware.entity.po.Log;
import com.lihao.courseware.exception.GlobalException;

public interface LogService {
    Long login(Log log) throws GlobalException;
    Long register(Log log) throws GlobalException;
}
