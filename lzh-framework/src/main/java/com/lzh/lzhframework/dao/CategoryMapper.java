package com.lzh.lzhframework.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类表(Category)表数据库访问层
 *
 * @author luzhiheng
 * @since 2022-10-01 09:03:55
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    Long getMaxSortCategory();
}

