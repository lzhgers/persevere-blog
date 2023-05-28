package com.lzh.lzhblog;

import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.vo.ArticleViewRankVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LZH
 * @date 2023/5/20
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ArticleService articleService;

    @Test
    public void testZSet() {
        Set<DefaultTypedTuple<String>> set = new HashSet<>();
        for (int i = 10; i < 15; i++) {
//            redisTemplate.opsForZSet().add("view", "test" + i, i);
            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<String>("test", (double) i);
            set.add(tuple);
        }
        redisTemplate.opsForZSet().add("test1", set);

        Set set1 = redisTemplate.opsForZSet().reverseRange("test1", 0, 4);

        for (Object o : set1) {
            String value = (String) o;
            System.out.println(value);
        }
    }

    @Test
    public void test1() {
        List<Article> articleList = articleService.list();
        List<ArticleViewRankVo> articleViewLists = BeanCopyUtils.copyBeanList(articleList, ArticleViewRankVo.class);

        Set<DefaultTypedTuple<ArticleViewRankVo>> tuples = articleViewLists.stream()
                .map(articleViewRankVo -> new DefaultTypedTuple<>(articleViewRankVo, articleViewRankVo.getViewCount().doubleValue()))
                .collect(Collectors.toSet());

        redisTemplate.opsForZSet().add("blog:view:rank", tuples);

        Set set = redisTemplate.opsForZSet().reverseRange("blog:view:rank", 0, 10);
        for (Object o : set) {
            ArticleViewRankVo articleViewRankVo = (ArticleViewRankVo) o;
            System.out.println(articleViewRankVo.getTitle() + ":" + articleViewRankVo.getViewCount());
        }
    }

}
