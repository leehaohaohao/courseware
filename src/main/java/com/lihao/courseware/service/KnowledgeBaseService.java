package com.lihao.courseware.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class KnowledgeBaseService {
    private Map<String, String> base = new HashMap<>();
    private JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();
    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/base.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // 忽略空白行
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    base.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String search(String query) {
        StringBuilder results = new StringBuilder("根据你的问题，我为你讲解以下知识点：<br>");
        int index = 0;
        for (Map.Entry<String, String> entry : base.entrySet()) {
            double similarity = jaroWinklerSimilarity.apply(query, entry.getKey());
            if (similarity >= 0.5) { // 相似度阈值
                results.append(++index);
                results.append(".");
                results.append(entry.getKey());
                results.append(":");
                results.append(entry.getValue());
                results.append("<br>");
            }
        }
        if(index == 0){
            return "你的问题有点难倒我了呢，已经将问题反馈给技术人员了，会尽快扩展我的知识库的！";
        }
        return results.toString();
    }
}
