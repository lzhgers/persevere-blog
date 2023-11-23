package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * 文章表(Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 10:16:18
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> getViewCountTopNumArticle(Integer topNum);

    long getTotalViewCount();

    List<Date> getAllDiffDate();
}

