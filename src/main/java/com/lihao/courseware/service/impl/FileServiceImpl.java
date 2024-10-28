package com.lihao.courseware.service.impl;

import com.lihao.courseware.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private long totalBytesRead = 0;
    private long totalBytes = 0;
    @Override
    public String[] fileLoad(MultipartFile file, String path) {
        String name = file.getOriginalFilename();
        InputStream in = null;
        BufferedOutputStream out = null;
        String filename;
        try {
            in = file.getInputStream();
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            filename = UUID.randomUUID() + name.substring(name.lastIndexOf("."));
            out = new BufferedOutputStream(new FileOutputStream(path + filename));
            byte[] bytes = new byte[1024 * 100];
            int readCount;
            totalBytes = file.getSize();
            totalBytesRead = 0;
            while ((readCount = in.read(bytes)) != -1) {
                out.write(bytes, 0, readCount);
                totalBytesRead += readCount;
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        String[] ss = new String[2];
        ss[0] = filename;
        ss[1] = path + filename;
        return ss;
    }
}
