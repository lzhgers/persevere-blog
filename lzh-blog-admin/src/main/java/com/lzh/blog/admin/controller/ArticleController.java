package com.lzh.blog.admin.controller;

import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.domain.entity.Article;
import com.lzh.blog.admin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/pageListAll")
    public ResponseResult pageListAllArticle(Article article, Integer pageNum, Integer pageSize) {
        return articleService.pageListAllArticle(article, pageNum, pageSize);
    }
}
