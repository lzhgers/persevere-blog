package com.lzh.lzhblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.lzh.lzhframework.dao")
@ComponentScan("com.lzh")
public class LzhBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(LzhBlogApplication.class, args);
    }
}
