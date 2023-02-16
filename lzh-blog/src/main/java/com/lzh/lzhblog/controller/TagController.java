package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/listAll")
    public ResponseResult listAllTag() {
        List<Tag> tags = tagService.listAll();
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
