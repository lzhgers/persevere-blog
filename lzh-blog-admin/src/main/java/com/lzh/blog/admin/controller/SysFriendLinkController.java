package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.AddOrEditFriendLinkForm;
import com.lzh.lzhframework.form.QueryFriendLinkForm;
import com.lzh.lzhframework.service.SysFriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/2/26
 */
@Api(tags = "友链管理接口")
@RestController
@RequestMapping("/friendlink")
public class SysFriendLinkController {

    @Resource
    private SysFriendLinkService sysFriendLinkService;

    @ApiOperation("分页条件查询友链")
    @PostMapping("/pageList")
    public ResponseResult pageList(@RequestBody QueryFriendLinkForm queryFriendLinkForm) {
        return sysFriendLinkService.pageList(queryFriendLinkForm);
    }

    @ApiOperation("删除友链")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteFriendLink(@PathVariable Long id) {
        return sysFriendLinkService.deleteById(id);
    }

    @ApiOperation("置顶友链")
    @PutMapping("/top")
    public ResponseResult topFriendLink(Long id, Integer listorder) {
        return sysFriendLinkService.topFriendLink(id, listorder);
    }

    @ApiOperation("添加或编辑友链")
    @PostMapping("/addOrEdit")
    public ResponseResult addOrEditFriendLink(@RequestBody AddOrEditFriendLinkForm addOrEditFriendLinkForm) {
        return sysFriendLinkService.addOrEdit(addOrEditFriendLinkForm);
    }

    @ApiOperation("批量删除友链")
    @DeleteMapping("/deleteBatch")
    public ResponseResult deleteBatchFriendLink(@RequestBody List<Long> ids) {
        return sysFriendLinkService.deleteBatch(ids);
    }
}
