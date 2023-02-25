package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryCategoryForm;
import com.lzh.lzhframework.form.SysEditCategoryForm;
import com.lzh.lzhframework.form.SysSaveCategoryForm;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/24
 */
public interface SysCategoryService {

    ResponseResult pageList(QueryCategoryForm queryCategoryForm);

    ResponseResult deleteCategoryById(Long id);

    ResponseResult deleteBatch(List<Long> categoryIds);

    ResponseResult saveCategory(SysSaveCategoryForm sysSaveCategoryForm);

    ResponseResult editCategory(SysEditCategoryForm sysEditCategoryForm);

    ResponseResult topCategory(Long id, Long sort);
}
