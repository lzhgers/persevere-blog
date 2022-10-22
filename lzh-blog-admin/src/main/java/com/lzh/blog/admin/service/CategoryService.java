package com.lzh.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.blog.admin.domain.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {


    List<Category> listAllTags();
}
