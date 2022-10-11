package com.lzh.lzhblog.aspect;

import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class MyAspect {

    @Autowired
    private ArticleService articleService;

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
            //根据id查询数据库

            Article article = articleService.getById(articleId);

            //更新访问量
            article.setViewCount(article.getViewCount() + 1);
            articleService.updateById(article);

            Object object = pjp.proceed();
            return object;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
