package com.lihao.courseware.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {
    @Value("${file.load.path}")
    private String path;
    @Value("${url}")
    private String url;
    @Value("${img.path}")
    private String imgPath;
    @Value("${ws.port}")
    private int wsPort;
    @Value("${ws.client.port}")
    private int clientPort;
    @Value("${ws.client.host}")
    private String host;
    @Value("${ws.client.path}")
    private String clientPath;
    @Value("${img.uri}")
    private String uri;
}
