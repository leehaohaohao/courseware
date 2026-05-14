package com.lihao.courseware.api.aliyun;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lihao.courseware.config.AppConfig;
import com.lihao.courseware.util.CommonTools;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class BaiDu {
    @Resource
    private AppConfig appConfig;
    private final ChatClient client;
    private HashMap<String,Long> urlMap = new HashMap<>();
    private Random random;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ASR_URL = " https://vop.baidu.com/pro_api";
    //121.40.154.188
    private String ip = "121.40.154.188";
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
        return "http://121.40.154.188:10001/courseware/char.html";
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
    @Async
    public CompletableFuture<String> modelAsync(byte[] fileData) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        String url = "";
        if(CommonTools.isInternetAvailable()){
            //网络不可用处理方式
            logger.info("休眠开始");
            Thread.sleep(10000);
            logger.info("休眠结束");
            //随机返回
            url = getRandomUrl();
        }else{
            logger.info("正在处理图像！");
            String description = imageUnderstandingSync(fileData);
            logger.info("图像理解结果：" + description);
            // description 喂给大模型
            String htmlContent = description;
            logger.info(htmlContent);
            logger.info("正在建模！");
            String in = "根据下面的模型描述给我完整的html代码，" +
                    "下面的描述是关于立体几何的（如圆柱、正方体、圆锥等），描述会有很大偏差，但是你最后一定要给出立体几何的清晰完整的建模（哪怕描述里没有任何几何体信息，如果描述里点位很多，那你就画一个多边体），按照你的理解去用plotly建模，plotly一定要是中国国内的库，给我对应的完整的html代码，" +
                    "不要python脚本,也不要离线python代码，" +
                    "只要html代码," +
                    "html里面的body要设置高度长度为100vh、宽度长度为100vw";
            /*String result = (String) chat(in+"下面是模型描述"+htmlContent).getData();*/
            String result = openAi(in+"下面是模型描述"+htmlContent);
            logger.info("建模完成！");
            File file1 = new File(appConfig.getImgPath());
            try (FileOutputStream fos = new FileOutputStream(file1)) {
                fos.write(CommonTools.charSelect(result).getBytes());
            }
            url = "http://"+ip+":10001/courseware/upload/char.html";
        }
        long endTime = System.currentTimeMillis()-startTime;
        logger.info("处理时间："+endTime);
        return CompletableFuture.completedFuture(url);
    }

    public String imageUnderstandingSync(byte[] imageBytes) throws InterruptedException {
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        String info = "Analyze the 3D geometric image and give key points";
        String taskId = submitImageUnderstanding(
                base64,
                info
        );

        for (int i = 0; i < 30; i++) {
            Thread.sleep(1000);
            String result = getImageUnderstandingResult(taskId);
            logger.info("轮询图片理解结果, 第{}次, task_id={}, result={}", i+1, taskId, result);
            if (result != null) {
                return result;
            }
        }

        throw new RuntimeException("Image understanding timeout");
    }

    public String submitImageUnderstanding(String base64Image, String question) {
        try {
            String accessToken = getAccessToken("Bza3vKDdy9FaPS3oxpPsm2e2",
                    "yuu2arBlwVaYctsVIdVXzuHIaxLxICoR");
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/image-understanding/request"
                    + "?access_token=" + accessToken;

            // JSON body
            Map<String, Object> body = new HashMap<>();
            body.put("image", base64Image);
            body.put("question", question);
            String json = MAPPER.writeValueAsString(body);

            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            String resp;
            try (InputStream is = conn.getInputStream()) {
                resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            JsonNode root = MAPPER.readTree(resp);
            // ---- 错误检查 ----
            if (root.has("error_code") && root.get("error_code").asInt() != 0) {
                String msg = root.has("error_msg") ? root.get("error_msg").asText() : "Unknown error";
                throw new RuntimeException("Baidu Image Understanding API error: " + msg);
            }
            JsonNode resultNode = root.path("result");
            if (!resultNode.has("task_id")) {
                throw new RuntimeException("No task_id returned by Baidu API: " + resp);
            }
            logger.info("提交成功，task_id={}", resultNode.get("task_id").asText());
            return resultNode.get("task_id").asText();
        } catch (IOException e) {
            throw new RuntimeException("Submit image understanding failed", e);
        }
    }

    public String getImageUnderstandingResult(String taskId) {
        try {
            String token = getAccessToken("Bza3vKDdy9FaPS3oxpPsm2e2", "yuu2arBlwVaYctsVIdVXzuHIaxLxICoR");
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/image-understanding/get-result"
                    + "?access_token=" + token;
            Map<String, Object> body = Map.of("task_id", taskId);
            String json = MAPPER.writeValueAsString(body);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            String resp;
            try (InputStream is = conn.getInputStream()) {
                resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
            logger.info("获取图片理解结果：{}", resp);
            JsonNode result = MAPPER.readTree(resp).path("result");

            if (result.get("ret_code").asInt() == 0) {
                return result.get("description").asText();
            }

            return null; // processing

        } catch (Exception e) {
            throw new RuntimeException("Get image understanding result failed", e);
        }
    }

    /*@Async
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
                response = qianfan.image2Text().model("internvl3-38b")
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
            *//*String result = (String) chat(in+"下面是模型描述"+htmlContent).getData();*//*
            String result = openAi(in+"下面是模型描述"+htmlContent);
            logger.error("建模完成！");
            File file1 = new File(appConfig.getImgPath());
            try (FileOutputStream fos = new FileOutputStream(file1)) {
                fos.write(CommonTools.charSelect(result).getBytes());
            }
            url = "http://"+ip+":10001/courseware/upload/char.html";
        }
        long endTime = System.currentTimeMillis()-startTime;
        logger.info("处理时间："+endTime);
        return CompletableFuture.completedFuture(url);
    }*/
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
    private String getAccessToken(String apiKey, String secretKey) {
        try {
            String url = "https://aip.baidubce.com/oauth/2.0/token"
                    + "?grant_type=client_credentials"
                    + "&client_id=" + apiKey
                    + "&client_secret=" + secretKey;

            HttpURLConnection conn =
                    (HttpURLConnection) new URL(url).openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            try (InputStream is = conn.getInputStream()) {
                JsonNode json = MAPPER.readTree(is);
                return json.get("access_token").asText();
            }
        } catch (Exception e) {
            throw new RuntimeException("Get Baidu access_token failed", e);
        }
    }
    public String baiduAsrM4aBase64(String audioBase64) {

        try {
            String apikey= "MHYiiQKGkzryFrw3vwnU3Jru";
            String secretKey = "MNJhqCQ9oSjJ7rMqu9IrESIDGK6IIFQE";
            // 去掉前缀
            if (audioBase64.startsWith("data:")) {
                audioBase64 = audioBase64.substring(audioBase64.indexOf(",") + 1);
            }
            byte[] audioBytes = Base64.getDecoder().decode(audioBase64);
            int audioLen = audioBytes.length;
            // 构造 JSON body
            Map<String, Object> body = new HashMap<>();
            body.put("format", "m4a");
            body.put("rate", 16000);
            body.put("dev_pid", 80001); // 极速版普通话
            body.put("channel", 1);
            body.put("cuid", "springboot_" + System.currentTimeMillis());
            body.put("token", getAccessToken(apikey, secretKey));
            body.put("len", audioLen);
            body.put("speech", audioBase64);

            String jsonBody = MAPPER.writeValueAsString(body);

            HttpURLConnection conn = (HttpURLConnection) new URL(ASR_URL).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            InputStream is = conn.getResponseCode() == 200
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            JsonNode root = MAPPER.readTree(is);

            if (root.has("err_no") && root.get("err_no").asInt() == 0) {
                return root.get("result").get(0).asText();
            }

            throw new RuntimeException(
                    "Baidu ASR error: " + root.path("err_msg").asText()
            );

        } catch (Exception e) {
            throw new RuntimeException("Baidu ASR failed", e);
        }
    }

}
