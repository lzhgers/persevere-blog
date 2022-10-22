package com.lzh.blog.admin.controller;

import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.enums.AppHttpCodeEnum;
import com.lzh.blog.admin.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Api(tags = "文章管理接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("按条件分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(dataType = "int", name = "pageSize", value = "每页数", required = true),
    })
    @GetMapping("/pageList")
    public ResponseResult pageListAllArticle(@RequestParam String title,
                                             @RequestParam String summary,
                                             @RequestParam Long categoryId,
                                             @RequestParam Integer pageNum,
                                             @RequestParam Integer pageSize) {
        return articleService.pageListAllArticle(title, summary, categoryId, pageNum, pageSize);
    }

    @ApiOperation("根据文章id删除文章")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable Long articleId) {
        return articleService.deleteArticleByArticleId(articleId);
    }

}
