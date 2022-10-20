package com.lzh.blog.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.lzh.blog.admin.dao")
@SpringBootApplication
public class LzhBlogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(LzhBlogAdminApplication.class, args);
    }

}
