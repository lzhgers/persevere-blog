package com.lzh.blog.admin.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author luzhiheng
 * @date 2023-11-22
 */
@Component
public class DataRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRunner.class);

    @Override
    public void run(String... args) {
        LOGGER.info("---------------------lzh-blog-admin启动成功---------------------");
    }
}
