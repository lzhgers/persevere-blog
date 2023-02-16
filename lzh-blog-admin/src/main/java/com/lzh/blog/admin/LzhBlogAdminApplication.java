package com.lzh.blog.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.lzh")
@MapperScan(basePackages = "com.lzh.lzhframework.dao")
@SpringBootApplication
public class LzhBlogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(LzhBlogAdminApplication.class, args);
    }

}
