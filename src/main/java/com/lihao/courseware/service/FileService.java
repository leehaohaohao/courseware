package com.lihao.courseware.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String[] fileLoad(MultipartFile file,String path);
}
