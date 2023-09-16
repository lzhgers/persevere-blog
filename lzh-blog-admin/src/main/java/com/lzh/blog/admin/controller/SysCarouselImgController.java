package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.AddOrEditCarouselImgForm;
import com.lzh.lzhframework.form.QueryCarouselImgForm;
import com.lzh.lzhframework.service.SysCarouselImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author luzhiheng
 * @Date 2023/9/15 17:07
 */
@Api(tags = "系统轮播图管理")
@RestController
@RequestMapping("/carouselImg")
public class SysCarouselImgController {

    @Resource
    private SysCarouselImgService sysCarouselImgService;

    @ApiOperation("分页条件查询文章")
    @PostMapping("/pageList")
    public ResponseResult pageList(@RequestBody QueryCarouselImgForm queryCarouselImgForm) {
        return sysCarouselImgService.pageList(queryCarouselImgForm);
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCarouselImg(@PathVariable Long id) {
        return sysCarouselImgService.deleteById(id);
    }

    @ApiOperation("置顶轮播图")
    @PutMapping("/top")
    public ResponseResult topCarouselImg(Long id, Integer sort) {
        return sysCarouselImgService.topCarouselImg(id, sort);
    }

    @ApiOperation("添加或编辑轮播图")
    @PostMapping("/addOrEdit")
    public ResponseResult addOrEditCarouselImg(@RequestBody AddOrEditCarouselImgForm addOrEditCarouselImgForm) {
        return sysCarouselImgService.addOrEditCarouselImg(addOrEditCarouselImgForm);
    }

    @ApiOperation("批量删除轮播图")
    @DeleteMapping("/deleteBatch")
    public ResponseResult deleteBatchCarouselImg(@RequestBody List<Long> ids) {
        return sysCarouselImgService.deleteBatch(ids);
    }

}
