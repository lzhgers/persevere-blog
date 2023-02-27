package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryCommentForm;
import com.lzh.lzhframework.service.SysCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author LZH
 * @date 2023/2/25
 */
@Api(tags = "评论管理接口")
@RestController
@RequestMapping("/comment")
public class SysCommentController {

    @Resource
    private SysCommentService sysCommentService;

    @ApiOperation("分页条件查询评论")
    @PostMapping("/pageList")
    public ResponseResult pageList(@RequestBody QueryCommentForm queryCommentForm) {
        return sysCommentService.pageList(queryCommentForm);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteComment(@PathVariable Long id) {
        return sysCommentService.deleteComment(id);
    }
}
