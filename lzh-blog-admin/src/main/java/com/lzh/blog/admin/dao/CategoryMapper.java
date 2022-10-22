package com.lzh.blog.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.blog.admin.domain.entity.Category;
import org.springframework.stereotype.Repository;

/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-01 09:03:55
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

}

