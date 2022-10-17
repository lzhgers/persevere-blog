package com.lzh.lzhblog.aspect;

import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.LoginUser;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.utils.RedisCache;
import com.lzh.lzhblog.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
public class MyAspect {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Pointcut("@annotation(com.lzh.lzhblog.annotation.InvokeAn)")
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
            if (StringUtils.hasText(flag) || Objects.isNull(userId)) {
                return pjp.proceed();
            }
            //根据id查询数据库
            Article article = articleService.getById(articleId);

            //更新访问量
            article.setViewCount(article.getViewCount() + 1);
            articleService.updateById(article);

            redisCache.setCacheObject(SysConstants.USER_VIEW + articleId + ":" + userId, "true", 24, TimeUnit.HOURS);

            Object object = pjp.proceed();
            return object;
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
