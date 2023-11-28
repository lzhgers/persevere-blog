package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.annotation.LogAnnotation;
import com.lzh.lzhframework.constants.LogType;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.service.TagService;
import com.lzh.lzhframework.utils.RedisCache;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lzh.lzhframework.constants.SysConstants.TAG_CACHE_REDIS;

/**
 * @author luzhiheng
 * @date 2022-10-11
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private RedisCache redisCache;

    @LogAnnotation(message = "查询所有标签", operation = LogType.QUERY)
    @GetMapping("/listAll")
    public ResponseResult listAllTag() {
        List<Tag> tagList = redisCache.getCacheList(TAG_CACHE_REDIS);
        if (!Collections.isEmpty(tagList)) {
            return ResponseResult.success(tagList);
        }
        List<Tag> tags = tagService.list();
        redisCache.setCacheList(TAG_CACHE_REDIS, tags);
        redisCache.expire(TAG_CACHE_REDIS, 24 * 7, TimeUnit.HOURS);
        return ResponseResult.success(tags);
    }

    @GetMapping("/getTagsByArticleId/{articleId}")
    public ResponseResult getTagsByArticleId(@PathVariable Long articleId) {
        List<Tag> tags = tagService.getTagsByArticleId(articleId);
        return ResponseResult.success(tags);
    }

    @GetMapping("/pageArticlesByTag")
    public ResponseResult getArticlesByTag(Integer pageNum, Integer pageSize, Long userId, Long tagId) {
        return tagService.getArticlesByTag(pageNum, pageSize, userId, tagId);
    }

}
