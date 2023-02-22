package com.lzh.blog.admin.controller;


import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.vo.SysUpdateArticleVo;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;
import com.lzh.lzhframework.service.SysArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "文章管理接口")
@RestController
@RequestMapping("/article")
public class SysArticleController {

    @Resource
    private SysArticleService sysArticleService;

    @ApiOperation("分页条件查询文章")
    //    @PreAuthorize("hasAuthority('系统管理')")
    @GetMapping("/pageList")
    public ResponseResult pageList(QueryArticleForm queryArticleForm) {
        return sysArticleService.pageList(queryArticleForm);
    }

    @ApiOperation("根据文章id删除文章")
    @DeleteMapping("/delete/{articleId}")
    public ResponseResult deleteArticle(@PathVariable Long articleId) {
        return sysArticleService.deleteArticleById(articleId);
    }

    @ApiOperation("添加文章")
    @PostMapping("/save")
    public ResponseResult saveArticle(@RequestBody SysSaveArticleForm form) {
        return sysArticleService.saveArticle(form);
    }

    @ApiOperation("查询文章")
    @GetMapping("/query/{id}")
    public ResponseResult updateArticle(@PathVariable Long id) {
        return sysArticleService.queryUpdateArticleById(id);
    }

    @ApiOperation("修改文章")
    @PutMapping("/update")
    public ResponseResult updateArticle(@RequestBody SysUpdateArticleVo sysUpdateArticleVo) {
        return sysArticleService.updateArticle(sysUpdateArticleVo);
    }

    @ApiOperation("批量导出Markdown文件")
    @GetMapping("/export")
    public ResponseResult downLoadFile(@RequestParam("articleId") Long articleId, HttpServletResponse response) throws Exception {
        return sysArticleService.downLoadFile(articleId, response);
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Long> articleIds) {
        return sysArticleService.deleteBatch(articleIds);
    }

}
