package com.lzh.blog.admin.controller;


import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "分类接口")
@RestController
@RequestMapping("/category")
public class SysCategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult listCategory() {
        List<Category> categories = categoryService.listAllCategory();
        return ResponseResult.okResult(categories);
    }
}
