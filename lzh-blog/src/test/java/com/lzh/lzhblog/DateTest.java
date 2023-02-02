package com.lzh.lzhblog;

import com.lzh.common.domain.entity.Article;
import com.lzh.common.domain.vo.DiffDateVo;
import com.lzh.lzhblog.dao.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DateTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testDate() {
        System.out.println(System.currentTimeMillis());
//        List<Date> dates = articleMapper.getAllDiffDate();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM日");
//        List<String> stringDate = dates.stream().map(sdf::format).distinct().collect(Collectors.toList());
//
//        List<DiffDateVo> diffDateVos = new ArrayList<>();
//
//        List<Article> articleList = articleMapper.selectList(null);
//
//        for (String s : stringDate) {
//            List<Article> articles = new ArrayList<>();
//            for (Article article : articleList) {
//                Date createTime = article.getCreateTime();
//                String format = sdf.format(createTime);
//                if (s.equals(format)) {
//                    articles.add(article);
//                }
//            }
//            diffDateVos.add(new DiffDateVo(s, articles));
//        }


//        stringDate.stream().map(s -> {
//            List<Article> articles = new ArrayList<>();
//            articleList.stream().map(article -> {
//                Date createTime = article.getCreateTime();
//                String format = sdf.format(createTime);
//                if (s.equals(format)) {
//                    articles.add(article);
//                }
//                return article;
//            }).collect(Collectors.toList());
//        })
    }

}
