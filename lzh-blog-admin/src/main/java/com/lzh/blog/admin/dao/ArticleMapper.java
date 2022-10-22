package com.lzh.blog.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.blog.admin.domain.entity.Article;
import org.springframework.stereotype.Repository;

/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 10:16:18
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {


}

