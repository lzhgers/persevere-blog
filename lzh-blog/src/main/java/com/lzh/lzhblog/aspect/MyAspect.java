package com.lzh.lzhblog.aspect;

import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author luzhiheng
 * @date 2022-10-14
 */
@Slf4j
@Component
@Aspect
public class MyAspect {

    @Resource
    private ArticleService articleService;

    @Resource
    private RedisCache redisCache;

    @Pointcut("@annotation(com.lzh.lzhblog.annotation.ViewAnnotation)")
    public void pt() {
    }

//    @Before("pt()")
//    public void calcViewCount(JoinPoint jp) {
//        log.info("执行切面方法");
//        Long articleId = (Long) jp.getArgs()[0];
//        Article article = articleService.getById(articleId);
//        article.setViewCount(article.getViewCount() + 1);
//        articleService.update(article, null);
//    }

    @Around("pt()")
    public Object calcViewCount(ProceedingJoinPoint pjp) {
        try {
            Long articleId = (Long) pjp.getArgs()[0];
            Long userId = (Long) pjp.getArgs()[1];
            String flag = redisCache.getCacheObject(SysConstants.USER_VIEW + articleId + ":" + userId);
            if (StringUtils.hasText(flag) || "true".equals(flag) || Objects.isNull(userId)) {
                return pjp.proceed();
            }
            //根据id查询数据库
            Article article = articleService.getById(articleId);

            //更新访问量
            article.setViewCount(article.getViewCount() + 1);
            articleService.updateById(article);

            redisCache.setCacheObject(SysConstants.USER_VIEW + articleId + ":" + userId, "true", 24, TimeUnit.HOURS);

            return pjp.proceed();
        } catch (Throwable e) {
            try {
                return pjp.proceed();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
