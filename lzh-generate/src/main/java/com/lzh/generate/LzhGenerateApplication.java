package com.lzh.generate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 生成数据库表
 */
@MapperScan({"com.gitee.sunchenbin.mybatis.actable.dao.*"})//固定的
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*"})//固定的
@SpringBootApplication
public class LzhGenerateApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LzhGenerateApplication.class, args);
        context.close();
    }

}
