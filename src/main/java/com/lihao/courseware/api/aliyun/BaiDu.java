package com.lihao.courseware.api.aliyun;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.baidubce.qianfan.model.image.Image2TextResponse;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class BaiDu {
    @Resource
    private AppConfig appConfig;
    private final ChatClient client;
    private HashMap<String,Long> urlMap = new HashMap<>();
    private Random random;
    //121.40.154.188
    private String ip = "localhost";
    @PostConstruct
    private void init(){
        String[] urls = {"http://"+ip+":10001/courseware/jiaohu.html",
                "http://"+ip+":10001/courseware/jiaohu2.html",
                "http://"+ip+":10001/courseware/jiaohu3.html",
                "http://"+ip+":10001/courseware/jiaohu4.html",
                "http://"+ip+":10001/courseware/jiaohu5.html",
                "http://"+ip+":10001/courseware/jiaohu6.html",
                "http://"+ip+":10001/courseware/jiaohu7.html",
                "http://"+ip+":10001/courseware/jiaohu8.html",
                "http://"+ip+":10001/courseware/jiaohu9.html",
                "http://"+ip+":10001/courseware/jiaohu10.html"
        };
        for(String s: urls){
            urlMap.put(s,System.currentTimeMillis()-1000*60*3);
        }
        random = new Random();
    }
    private String getRandomUrl(){
        List<String> urls = new ArrayList<>();
        for(Map.Entry<String, Long> entry : urlMap.entrySet()){
            if(System.currentTimeMillis() - entry.getValue() > 1000*60*3){
                urls.add(entry.getKey());
            }
        }
        if(!urls.isEmpty()){
            String url = urls.get(random.nextInt(urls.size()));
            urlMap.put(url,System.currentTimeMillis());
            return url;
        }
        return "http://localhost:8080/courseware/char.html";
    }
    public BaiDu(ChatClient.Builder chatClientBuilder) {
        this.client = chatClientBuilder.build();
    }
    private static final Logger logger = LoggerFactory.getLogger(BaiDu.class);
    private Qianfan qianfan = new Qianfan(Auth.TYPE_OAUTH,
            "aNXakJk2XQvxugcCFkEBqWMn",
            "QK2eZUGIqTIuJsTIriMUQXL9Smm7P1O2");
    public String chat(String msg){
        ChatResponse response = qianfan.chatCompletion()
                .model("ERNIE-Lite-8K-0922")
                .addMessage("user", msg)
                .execute();
        return response.getResult();
    }
    public String model(MultipartFile file) throws IOException {
        String info = "What I'm giving you is a three-dimensional geometric image. Please analyze the image and provide details and key points of the model";
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
        System.out.println(base64Image);
        Image2TextResponse response = qianfan.image2Text().model("Fuyu-8B")
                .image(base64Image)
                .prompt(info)
                .execute();
        String htmlContent = response.getResult();
        String in = "根据下面的模型描述给我完整的html代码，下面的描述是关于立体几何的（如圆柱、正方体、圆锥等），描述会有很大偏差，但是你最后要给出立体几何的清晰完整的建模（哪怕描述里没有任何几何体信息，如果描述里点位很多，那你就画一个多边体），按照你的理解去用plotly建模，给我对应的完整的html代码，不要python脚本,也不要离线python代码，只要html代码";
        String result = maxChat(in+"下面是模型描述"+htmlContent);
        File file1 = new File("/tools/courseware/model/jiaohu2.html");
        try (FileOutputStream fos = new FileOutputStream(file1)) {
            fos.write(CommonTools.charSelect(result).getBytes());
        }
        String Url = "http://121.40.154.188:8080/courseware/char.html";
        return Url;
    }
    @Async
    public CompletableFuture<String> modelAsync(byte[] fileData) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        String url = "";
        if(!CommonTools.isInternetAvailable()){
            //网络不可用处理方式
            logger.info("休眠开始");
            Thread.sleep(10000);
            logger.info("休眠结束");
            //随机返回
            url = getRandomUrl();
        }else{
            String info = "What I'm giving you is a three-dimensional geometric image. Please analyze the image and provide details and key points of the model";

            String base64Image = null;
            base64Image = Base64.getEncoder().encodeToString(fileData);
            logger.error("正在处理图像！");
            Image2TextResponse response = null;
            try {
                response = qianfan.image2Text().model("Fuyu-8B")
                        .image(base64Image)
                        .prompt(info)
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            String htmlContent = response.getResult();
            logger.error("正在建模！");
            String in = "根据下面的模型描述给我完整的html代码，" +
                    "下面的描述是关于立体几何的（如圆柱、正方体、圆锥等），描述会有很大偏差，但是你最后一定要给出立体几何的清晰完整的建模（哪怕描述里没有任何几何体信息，如果描述里点位很多，那你就画一个多边体），按照你的理解去用plotly建模，plotly一定要是中国国内的库，给我对应的完整的html代码，" +
                    "不要python脚本,也不要离线python代码，" +
                    "只要html代码," +
                    "html里面的body要设置高度长度为100vh、宽度长度为100vw";
            /*String result = (String) chat(in+"下面是模型描述"+htmlContent).getData();*/
            String result = openAi(in+"下面是模型描述"+htmlContent);
            logger.error("建模完成！");
            File file1 = new File(appConfig.getImgPath());
            try (FileOutputStream fos = new FileOutputStream(file1)) {
                fos.write(CommonTools.charSelect(result).getBytes());
            }
            url = "http://"+ip+":8080/courseware/char.html";
        }
        long endTime = System.currentTimeMillis()-startTime;
        logger.info("处理时间："+endTime);
        return CompletableFuture.completedFuture(url);
    }
    public String maxChat(String msg){
        ChatResponse response = qianfan.chatCompletion()
                .model("ERNIE-4.0-Turbo-8K")
                .addMessage("user", msg)
                .execute();
        return response.getResult();
    }
    public String openAi(String msg){
        return client.prompt().user(msg).call().content();
    }
}
