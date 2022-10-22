package com.lzh.blog.admin.controller;

import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.domain.entity.Category;
import com.lzh.blog.admin.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "分类接口")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询所有分类")
    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<Category> categoryList = categoryService.listAllTags();
        return ResponseResult.okResult(categoryList);
    }

}
