package com.lzh.blog.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.blog.admin.domain.entity.ArticleTag;
import org.springframework.stereotype.Repository;

/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-12 09:00:46
 */
@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}

