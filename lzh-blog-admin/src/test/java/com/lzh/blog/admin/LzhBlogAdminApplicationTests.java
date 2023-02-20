package com.lzh.blog.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LzhBlogAdminApplicationTests {

    @Test
    void contextLoads() {
        String originalFilename = "ioawefuioaewhu.png";
        int index = originalFilename.lastIndexOf(".");
        System.out.println(originalFilename.substring(0, index));
    }

}
