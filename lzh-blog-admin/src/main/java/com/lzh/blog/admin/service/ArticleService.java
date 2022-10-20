package com.lzh.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.domain.entity.Article;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-28 10:16:20
 */
public interface ArticleService extends IService<Article> {

    ResponseResult pageListAllArticle(Article article, Integer pageNum, Integer pageSize);
}

