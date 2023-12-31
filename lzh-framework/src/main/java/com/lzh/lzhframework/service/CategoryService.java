package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.vo.ArticleVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-01 09:03:57
 */
public interface CategoryService extends IService<Category> {

    List<Category> listAllCategory();

    Category getCategoryByArticleId(Long articleId);

    List<ArticleVo> getArticleByCategoryId(Long categoryId);

    Long getMaxCategorySort();
}

