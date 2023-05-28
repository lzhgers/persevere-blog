package com.lzh.lzhblog;

import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.ArticleEntity;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/5/28
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongodbTest {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private ArticleService articleService;

    @Test
    public void testCreateCollection() {
        mongoTemplate.createCollection("article");
    }

    @Test
    public void testAdd() {
//        ArticleEntity article = new ArticleEntity();
//        article.setContent("fff");
//        article.setHtml("fff");
//        mongoTemplate.insert(article, "article");

        List<Article> list = articleService.list();
        List<ArticleEntity> entities = BeanCopyUtils.copyBeanList(list, ArticleEntity.class);
        for (ArticleEntity entity : entities) {
            System.out.println(entity.getId());
        }
        mongoTemplate.insertAll(entities);
    }

    @Test
    public void testSelect() {
        long begin = System.currentTimeMillis();
//        List<ArticleEntity> all = mongoTemplate.findAll(ArticleEntity.class);
        ArticleEntity article = mongoTemplate.findById(0, ArticleEntity.class);
        System.out.println(article);
        System.out.println(System.currentTimeMillis() - begin);


    }
}
