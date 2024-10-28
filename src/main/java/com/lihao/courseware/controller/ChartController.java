package com.lihao.courseware.controller;

import com.lihao.courseware.annotation.Time;
import com.lihao.courseware.api.aliyun.BaiDu;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.entity.dto.ResponsePack;
import com.lihao.courseware.handler.ProgressWebSocketHandler;
import com.lihao.courseware.service.FileService;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping("/chart")
@EnableAsync
public class ChartController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChartController.class);
    @Resource
    private AppConfig appConfig;
    @Resource
    private FileService fileService;
    @Resource
    private CommonTools commonTools;
    @Resource
    private BaiDu baiDu;

    @PostMapping("/upload")
    @Time
    public ResponsePack upload(MultipartFile file) throws IOException, InterruptedException {
        //TODO
        String ss[] = fileService.fileLoad(file, commonTools.getChartPath());
        //String ss[] = fileService.fileLoad(file,"/tools/courseware/model/img/");
        String baseUrl = appConfig.getUrl();
        String fileName = ss[0];
        String filePath = ss[1];

        // 读取文件内容
        Path path = Paths.get(filePath);
        byte[] fileData = Files.readAllBytes(path);

        // 异步调用处理文件
        CompletableFuture<String> htmlPathFuture = baiDu.modelAsync(fileData);
        htmlPathFuture.thenAccept(htmlPath -> {
            ProgressWebSocketHandler.sendProgress(htmlPath);
        });
        //TODO
        return getSuccessResponse(baseUrl + fileName);
        //return getSuccessResponse("http://121.40.154.188:8080/courseware/img/"+fileName);
    }

    @PostMapping("/upload/img")
    @Time
    public ResponsePack uploadImg(MultipartFile file) throws IOException {
        /*String ss[] = fileService.fileLoad(file, commonTools.getChartPath() + "img/");*/
        String ss[] = fileService.fileLoad(file, appConfig.getPath());
        String baseUrl = appConfig.getUrl();
        String fileName = ss[0];
        return getSuccessResponse(baseUrl + fileName);
        //TODO
        //return getSuccessResponse("http://121.40.154.188:8080/img/"+fileName);
    }
}
