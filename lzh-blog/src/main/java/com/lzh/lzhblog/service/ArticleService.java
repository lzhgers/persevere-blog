package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-28 10:16:20
 */
public interface ArticleService extends IService<Article> {

    List<ArticleVo> listAll();

    ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId);

    Article getArticleById(Long id);

    ResponseResult updateArticle(Article article);

    ResponseResult addArticle(ArticleVo articleVo);

    Long getLikeCountByArticleId(Long articleId);


    List<Article> getViewCountTopNumArticle(Integer topNum);
}

