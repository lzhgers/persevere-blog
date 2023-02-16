package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.vo.ArticleVo;
import com.lzh.lzhframework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     *
     * @param articleId 文章id
     * @return 类别
     */
    @GetMapping("/getCategoryByArticleId")
    public ResponseResult getByCategoryId(Long articleId) {
        Category category = categoryService.getCategoryByArticleId(articleId);
        return ResponseResult.okResult(category);
    }

    /**
     * 根据分类id查询文章
     * @param categoryId 分类id
     * @return 类别
     */
    @GetMapping("/getArticleByCategoryId/{categoryId}")
    public ResponseResult getArticleByCategoryId(@PathVariable Long categoryId) {
        List<ArticleVo> articleVoList = categoryService.getArticleByCategoryId(categoryId);
        return ResponseResult.okResult(articleVoList);
    }
}
