package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/2/17
 */
@RestController
@RequestMapping("/tag")
public class SysTagController {

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult listTag() {
        List<Tag> tags = tagService.listAll();
        return ResponseResult.okResult(tags);
    }
}
