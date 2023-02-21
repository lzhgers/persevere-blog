package com.lzh.blog.admin;

import com.lzh.lzhframework.utils.RegexUtils;
import com.overzealous.remark.Remark;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/21
 */
@SpringBootTest
public class TestHtmlToMarkdown {

    String content = "在局部刷新中，使用异步请求对象，在浏览器内部发起请求，获取数据。\n" +
            "\n" +
            "![image-20201012092218113](images/image-20201012092218113.png)# 四、Pattern类/Matcher类\n" +
            "\n" +
            "- Matcher类方法\n" +
            "\n" +
            "  ![image-20220711152824667](正则表达式.assets/image-20220711152824667.png)";

    @Test
    public void test01() {
//        Remark remark = new Remark();
//        String convert = remark.convert(content);
//        System.out.println(convert);
        List<String> match = RegexUtils.match(content, "!\\[(.*?)\\]\\((.*?)\\)");
        match.forEach(System.out::println);
    }
}
