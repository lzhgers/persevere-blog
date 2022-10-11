package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.annotation.InvokeAn;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Category;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import com.lzh.lzhblog.enums.AppHttpCodeEnum;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<ArticleVo> articleVoList = articleService.listAll();
        return ResponseResult.okResult(articleVoList);
    }

    @GetMapping("/countCommentsByArticleId/{articleId}")
    public ResponseResult countCommentsByArticleId(@PathVariable Long articleId) {
        long count = commentService.countCommentsByArticleId(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("commentCount", count);
        return ResponseResult.okResult(map);
    }

    @GetMapping("/pageListAll")
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize) {
        return articleService.pageListAll(pageNum, pageSize);
    }

    @InvokeAn
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (Objects.isNull(article)) {
            throw new RuntimeException("文章不存在");
        }
        return ResponseResult.okResult(article);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }

    @PostMapping
    public ResponseResult addArticle(@RequestBody Article article) {
        return articleService.addArticle(article);
    }


}
