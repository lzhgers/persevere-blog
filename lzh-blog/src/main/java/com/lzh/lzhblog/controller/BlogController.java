package com.lzh.lzhblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhblog.annotation.LogAnnotation;
import com.lzh.lzhframework.constants.LogType;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.dao.SysDispositionMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.*;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.service.TagService;
import com.lzh.lzhframework.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.lzh.lzhframework.constants.SysConstants.*;

/**
 * @author luzhiheng
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private ArticleService articleService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private SysDispositionMapper sysDispositionMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询网站信息
     * @return
     */
    @LogAnnotation(message = "查询网站信息", operation = LogType.QUERY)
    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        BlogInfo blogInfo = new BlogInfo();

        String articleCount = redisCache.getCacheObject(ARTICLE_COUNT);
        String tagCount = redisCache.getCacheObject(ARTICLE_COUNT);
        String categoryCount = redisCache.getCacheObject(ARTICLE_COUNT);
        String commentCount = redisCache.getCacheObject(ARTICLE_COUNT);
        String viewCount = redisCache.getCacheObject(ARTICLE_COUNT);
        String runningTime = redisCache.getCacheObject(SysConstants.WEBSITE_RUNTIME);

        if (StringUtils.isEmpty(articleCount)) {
            articleCount = articleService.count(new LambdaQueryWrapper<Article>().eq(Article::getDelFlag, 0)) + "";
            redisCache.setCacheObject(ARTICLE_COUNT, articleCount, 24, TimeUnit.HOURS);
        }
        if (StringUtils.isEmpty(tagCount)) {
            tagCount = tagService.count(new LambdaQueryWrapper<Tag>().eq(Tag::getDelFlag, 0)) + "";
            redisCache.setCacheObject(TAG_COUNT, tagCount, 24, TimeUnit.HOURS);
        }
        if (StringUtils.isEmpty(categoryCount)) {
            categoryCount = categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getDelFlag, 0)) + "";
            redisCache.setCacheObject(CATEGORY_COUNT, categoryCount, 24, TimeUnit.HOURS);
        }
        if (StringUtils.isEmpty(commentCount)) {
            commentCount = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getDelFlag, 0)) + "";
            redisCache.setCacheObject(COMMENT_COUNT, commentCount, 24, TimeUnit.HOURS);
        }
        if (StringUtils.isEmpty(viewCount)) {
            viewCount = articleMapper.getTotalViewCount() + "";
            redisCache.setCacheObject(VIEW_COUNT, viewCount, 24, TimeUnit.HOURS);
        }
        if (StringUtils.isEmpty(runningTime)) {
            SysDisposition sysDisposition = sysDispositionMapper.selectBySetting(WEBSITE_RUNTIME);
            runningTime = sysDisposition != null ? sysDisposition.getSetValue() : "0";
            redisCache.setCacheObject(WEBSITE_RUNTIME, runningTime);
        }
        blogInfo.setArticleCount(articleCount)
                .setTagCount(tagCount)
                .setCategoryCount(categoryCount)
                .setCommentCount(commentCount)
                .setViewCount(viewCount)
                .setRunningTime(runningTime);
        return ResponseResult.success(blogInfo);
    }

}
