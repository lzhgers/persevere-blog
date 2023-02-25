package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.QueryCategoryForm;
import com.lzh.lzhframework.form.SysEditCategoryForm;
import com.lzh.lzhframework.form.SysSaveCategoryForm;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.SysCategoryService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.UnderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LZH
 * @date 2023/2/24
 */
@Service
public class SysCategoryServiceImpl implements SysCategoryService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;

    @Override
    public ResponseResult pageList(QueryCategoryForm form) {
        String name = form.getName();
        List<Map<String, String>> sortArr = form.getSortArr();

        Page<Category> page = new Page<>(form.getPageNum(), form.getPageSize());

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.like(StringUtils.hasText(name), "name", name);

        for (Map<String, String> map : sortArr) {
            String order = map.get("order");
            String prop = map.get("prop");
            prop = UnderUtil.camel2under(prop);
            if ("ascending".equals(order)) {
                categoryQueryWrapper.orderByAsc(prop);
            } else if ("descending".equals(order)) {
                categoryQueryWrapper.orderByDesc(prop);
            }
        }

        categoryService.page(page, categoryQueryWrapper);

        PageVo pageVo = new PageVo(page.getTotal(), page.getRecords());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId, id);
        long count = articleService.count(queryWrapper);
        if (count > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_HAS_BLOG);
        }
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteBatch(List<Long> categoryIds) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getCategoryId, categoryIds);
        long count = articleService.count(queryWrapper);
        if (count > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_HAS_BLOG);
        }

        categoryService.removeBatchByIds(categoryIds);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult saveCategory(SysSaveCategoryForm form) {
        if (Objects.isNull(form.getSort()) || !StringUtils.hasText(form.getName())) {
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_RIGHT);
        }
        Category category = BeanCopyUtils.copyBean(form, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editCategory(SysEditCategoryForm form) {
        if (Objects.isNull(form.getSort()) || !StringUtils.hasText(form.getName())) {
            throw new SystemException(AppHttpCodeEnum.PARAM_NOT_RIGHT);
        }
        Category category = BeanCopyUtils.copyBean(form, Category.class);
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult topCategory(Long id, Long sort) {
        Long maxSort = categoryService.getMaxCategorySort();
        if (sort.equals(maxSort)) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_IS_TOP);
        }
        Category category = Category.builder()
                .id(id)
                .sort(maxSort + 1).build();
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }
}
