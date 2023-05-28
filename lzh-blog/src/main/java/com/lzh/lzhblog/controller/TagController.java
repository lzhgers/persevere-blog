package com.lzh.lzhblog.controller;

import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/listAll")
    public ResponseResult listAllTag() {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        String tagJsonStr = (String) opsForValue.get(SysConstants.TAG_CACHE_REDIS);
        if (StringUtils.hasText(tagJsonStr)) {
            List<Tag> tagList = JSON.parseObject(tagJsonStr, List.class);
            return ResponseResult.okResult(tagList);
        }
        List<Tag> tags = tagService.listAll();
        opsForValue.set(SysConstants.TAG_CACHE_REDIS, JSON.toJSONString(tags));
        return ResponseResult.okResult(tags);
    }

    @GetMapping("/getTagsByArticleId/{articleId}")
    public ResponseResult getTagsByArticleId(@PathVariable Long articleId) {
        List<Tag> tags = tagService.getTagsByArticleId(articleId);
        return ResponseResult.okResult(tags);
    }

    @GetMapping("/pageArticlesByTag")
    public ResponseResult getArticlesByTag(Integer pageNum, Integer pageSize, Long userId, Long tagId) {
        return tagService.getArticlesByTag(pageNum, pageSize, userId, tagId);
    }

}
