package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/5/12
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/request")
    public ResponseResult testRequest() {
        List<Article> articleList = articleService.getViewCountTopNumArticle(4);
        return ResponseResult.success(articleList);
    }
}
