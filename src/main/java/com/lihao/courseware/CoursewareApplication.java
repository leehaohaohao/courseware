package com.lihao.courseware;

import com.lihao.courseware.webSocket.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.lihao.courseware.mapper")
@EnableScheduling
@EnableAsync
public class CoursewareApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoursewareApplication.class, args);
    }
    @Bean
    public CommandLineRunner run(NettyServer nettyServer) {
        return args -> {
            nettyServer.start();
        };
    }
}
