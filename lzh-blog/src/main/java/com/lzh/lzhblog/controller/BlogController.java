package com.lzh.lzhblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.*;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.service.TagService;
import com.lzh.lzhframework.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        long articleCount = articleService.count(new LambdaQueryWrapper<Article>().eq(Article::getDelFlag, 0));
        long tagCount = tagService.count(new LambdaQueryWrapper<Tag>().eq(Tag::getDelFlag, 0));
        long categoryCount = categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getDelFlag, 0));
        long commentCount = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getDelFlag, 0));
        long viewCount = articleMapper.getTotalViewCount();

        String runningTime = redisCache.getCacheObject(SysConstants.BLOG_RUN_TIME);
        BlogInfo blogInfo = new BlogInfo()
                .setArticleCount(articleCount)
                .setTagCount(tagCount)
                .setCategoryCount(categoryCount)
                .setCommentCount(commentCount)
                .setViewCount(viewCount)
                .setRunningTime(runningTime);

        return ResponseResult.okResult(blogInfo);
    }
}
