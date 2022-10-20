package com.lzh.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.blog.admin.dao.ArticleMapper;
import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.domain.entity.Article;
import com.lzh.blog.admin.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Api()
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public ResponseResult pageListAllArticle(Article article, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQuery());


        return null;
    }
}
