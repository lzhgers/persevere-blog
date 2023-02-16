package com.lzh.blog.admin.controller;


import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.service.SysArticleService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "文章管理接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private SysArticleService sysArticleService;

    @PreAuthorize("hasAuthority('系统管理')")
    @GetMapping("/pageList")
    public ResponseResult pageList(QueryArticleForm queryArticleForm) {
        return sysArticleService.pageList(queryArticleForm);
    }
}
