package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.form.EditTagForm;
import com.lzh.lzhframework.form.QueryTagForm;
import com.lzh.lzhframework.service.SysTagService;
import com.lzh.lzhframework.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/2/17
 */
@Api(tags = "标签管理接口")
@RestController
@RequestMapping("/tag")
public class SysTagController {

    @Resource
    private TagService tagService;

    @Resource
    private SysTagService sysTagService;

    @ApiOperation("查询所有标签")
    @GetMapping("/list")
    public ResponseResult listTag() {
        List<Tag> tags = tagService.listAll();
        return ResponseResult.success(tags);
    }

    @ApiOperation("条件分页查询标签")
    @PostMapping("/pageList")
    public ResponseResult pageList(@RequestBody QueryTagForm queryTagForm) {
        return sysTagService.pageList(queryTagForm);
    }

    @ApiOperation("添加标签")
    @PostMapping("/add")
    public ResponseResult addTag(String name, Long sort) {
        return sysTagService.addTag(name, sort);
    }

    @ApiOperation("删除标签")
    @PostMapping("/delet/{id}")
    public ResponseResult deleteTag(@PathVariable Long id) {
        return sysTagService.deleteTag(id);
    }

    @ApiOperation("编辑标签")
    @PutMapping("/edit")
    public ResponseResult editTag(@RequestBody EditTagForm form) {
        return sysTagService.editTag(form.getId(), form.getName(), form.getSort());
    }

    @ApiOperation("批量删除标签")
    @DeleteMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return sysTagService.deleteBatch(ids);
    }

    @ApiOperation("置顶标签")
    @PutMapping("/top/{id}")
    public ResponseResult topTag(@PathVariable Long id, Long sort) {
        return sysTagService.topTag(id, sort);
    }
}
