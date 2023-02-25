package com.lzh.blog.admin.controller;


import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.form.QueryCategoryForm;
import com.lzh.lzhframework.form.SysEditCategoryForm;
import com.lzh.lzhframework.form.SysSaveCategoryForm;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.SysCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "分类接口")
@RestController
@RequestMapping("/category")
public class SysCategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private SysCategoryService sysCategoryService;

    @ApiOperation("查询所有分类")
    @GetMapping("/list")
    public ResponseResult listCategory() {
        List<Category> categories = categoryService.listAllCategory();
        return ResponseResult.okResult(categories);
    }

    @ApiOperation("分页条件查询分类")
    @PostMapping("/pageList")
    public ResponseResult pageListCategory(@RequestBody QueryCategoryForm queryCategoryForm) {
        return sysCategoryService.pageList(queryCategoryForm);
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id) {
        return sysCategoryService.deleteCategoryById(id);
    }

    @ApiOperation("批量删除分类")
    @DeleteMapping("/deleteBatch")
    public ResponseResult deleteBatchCategory(@RequestBody List<Long> categoryIds) {
        return sysCategoryService.deleteBatch(categoryIds);
    }

    @ApiOperation("添加分类")
    @PostMapping("/saveCategory")
    public ResponseResult saveCategory(@RequestBody SysSaveCategoryForm sysSaveCategoryForm) {
        return sysCategoryService.saveCategory(sysSaveCategoryForm);
    }

    @ApiOperation("编辑分类")
    @PutMapping("/editCategory")
    public ResponseResult editCategory(@RequestBody SysEditCategoryForm sysEditCategoryForm) {
        return sysCategoryService.editCategory(sysEditCategoryForm);
    }

    @ApiOperation("置顶分类")
    @PutMapping("/top/{id}")
    public ResponseResult topCategory(@PathVariable Long id, Long sort) {
        return sysCategoryService.topCategory(id, sort);
    }
}
