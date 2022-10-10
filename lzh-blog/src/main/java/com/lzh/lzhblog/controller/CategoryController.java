package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Category;
import com.lzh.lzhblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<Category> categories = categoryService.listAllCategory();
        return ResponseResult.okResult(categories);
    }

    /**
     * 根据文章id查询类别
     * @param articleId
     * @return
     */
    @GetMapping("/getCategoryByArticleId")
    public ResponseResult getByCategoryId(Long articleId) {
        Category category = categoryService.getCategoryByArticleId(articleId);
        return ResponseResult.okResult(category);
    }

}
