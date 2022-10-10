package com.lzh.lzhblog.aspect;

import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.service.ArticleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {

    @Autowired
    private ArticleService articleService;

    @Pointcut("@annotation(com.lzh.lzhblog.annotation.InvokeAn)")
    public void pt() {

    }

    @Around("pt()")
    public Object calcViewCount(ProceedingJoinPoint pjp) {
        try {
            //根据id查询数据库
            Long articleId = (Long) pjp.getArgs()[0];
            Article article = articleService.getById(articleId);
            //更新访问量
            article.setViewCount(article.getViewCount() + 1);
            articleService.updateById(article);
            return pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
