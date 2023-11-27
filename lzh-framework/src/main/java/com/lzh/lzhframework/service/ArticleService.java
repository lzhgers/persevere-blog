package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.vo.ArticleVo;

import java.util.List;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-28 10:16:20
 */
public interface ArticleService extends IService<Article> {

    List<ArticleVo> listAll();

    ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId, String keyword);

    Article getArticleById(Long id);

    ResponseResult updateArticle(ArticleVo articleVo);

    ResponseResult addArticle(ArticleVo articleVo);

    Long getLikeCountByArticleId(Long articleId);


    List<Article> getViewCountTopNumArticle(Integer topNum);

    List<Article> selectByKeyword(String keyword);

    List<String> listDiffDate();

    List<Article> listArticleByDate(String date);

    Long countCollect(Long articleId);

    Integer getCollectStmt(Long articleId, Long userId);

    ResponseResult pageUserPublishArticle(Long userId, Integer pageNum, Integer pageSize);

    ResponseResult pageUserRoughArticle(Long userId, Integer pageNum, Integer pageSize);

    LambdaQueryWrapper<Article> selectExpectContentAndHtmlField(LambdaQueryWrapper<Article> queryWrapper);
}

