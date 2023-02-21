package com.lzh.blog.admin;

import com.lzh.lzhframework.utils.MarkdownUtils;
import com.lzh.lzhframework.utils.RegexUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TestMdUploadAndParse {

    @Test
    void contextLoads() {
        String origin = "![img001](Redis.assets/image-20220628102817255.png)> 在满足特定的redis操作条件时，将内存中的数据以数据快照的形式存储到db文件中\n" +
                "![image-20220628134211162](Redis.assets/image-20220628134211162.png)\n" +
                "- 原理：";
        String html = MarkdownUtils.markdownToHtml(origin);
        System.out.println(html);

        System.out.println("==================");

        List<String> imgTags = MarkdownUtils.getAllImgTagFromHtml(html);
        for (String imgTag : imgTags) {
            String[] split = imgTag.split("\"");

            String imgPath = "";
            if (imgTag.indexOf("src") < imgTag.indexOf("alt")) {
                imgPath = split[1];
            } else {
                imgPath = split[3];
            }
        }
    }

    @Test
    public void testInputStream() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\dev-vue-boot\\PERSEVERE-BLOG\\data.txt")));

        StringBuilder sb = new StringBuilder();
        int len = -1;
        char[] chars = new char[2];
        while ((len = br.read(chars)) != -1) {
            sb.append(chars, 0, len);
        }
        System.out.println(sb);

    }

    @Test
    public void testReplaceMdImg() {
        String markdown = "![image-20220727084615917](函数式编程——Stream流.assets/image-20220727084615917.png)";
        List<String> match = RegexUtils.match(markdown, "\\((.*?)\\)");
        String path = match.get(0);
        path = path.substring(1, path.length() - 1);
        System.out.println(path);
    }
}
