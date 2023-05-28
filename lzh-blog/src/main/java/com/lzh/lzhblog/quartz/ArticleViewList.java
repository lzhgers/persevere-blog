package com.lzh.lzhblog.quartz;

import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.vo.ArticleViewRankVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleViewList {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateBlogRunningInfo() {
        redisTemplate.delete(SysConstants.ARTICLE_VIEW_RANK);
        List<Article> articleList = articleService.list();
        List<ArticleViewRankVo> articleViewLists = BeanCopyUtils.copyBeanList(articleList, ArticleViewRankVo.class);

        Set<DefaultTypedTuple<ArticleViewRankVo>> tuples = articleViewLists.stream()
                .map(articleViewRankVo -> new DefaultTypedTuple<>(articleViewRankVo, articleViewRankVo.getViewCount().doubleValue()))
                .collect(Collectors.toSet());

        redisTemplate.opsForZSet().add(SysConstants.ARTICLE_VIEW_RANK, tuples);
    }
}
