package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.dao.CollectMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.Collect;
import com.lzh.lzhframework.domain.vo.ArticleVo;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CollectService;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Collect)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 10:51:28
 */
@Service("collectService")
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Transactional
    @Override
    public ResponseResult addCollection(Long userId, Long articleId) {

        int isCollect = isCollectedByUserIdAndArticleId(userId, articleId);

        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setArticleId(articleId);

        CollectMapper collectMapper = getBaseMapper();

        switch (isCollect) {
            case -1: {
                //未收藏过
                collect.setCollectStatus(1);
                save(collect);
                break;
            }
            case 0: {
                collect.setCollectStatus(0);
                collectMapper.updateCollectStatus(collect);
                break;
            }
            case 1: {
                collect.setCollectStatus(1);
                collectMapper.updateCollectStatus(collect);
                break;
            }
        }

        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId, articleId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "0");
        articleLambdaQueryWrapper.select(Article::getId, Article::getCollectCount);
        Article article = articleMapper.selectOne(articleLambdaQueryWrapper);

        if (Objects.isNull(article)) {
            throw new RuntimeException("文章不存在");
        }

        Long collectCount = article.getCollectCount();

        if (1 == isCollect) {
            //已收藏
            article.setCollectCount(collectCount - 1);
            articleMapper.updateById(article);
        } else {
            //未收藏
            article.setCollectCount(collectCount + 1);
            articleMapper.updateById(article);
        }

        return ResponseResult.success();
    }

    @Override
    public ResponseResult pageArticleByUserId(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId);
        queryWrapper.eq(Collect::getCollectStatus, "1");


        //获取收藏文章id
        List<Collect> collectList = list(queryWrapper);
        List<Long> articleIdList = collectList.stream()
                .map(Collect::getArticleId).collect(Collectors.toList());


        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.in(Article::getId, articleIdList);
        articleLambdaQueryWrapper.eq(Article::getStatus, "0");
        articleService.page(page, articleLambdaQueryWrapper);

        List<Article> articleList = page.getRecords();
        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);

        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId()));
                    return articleVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.success(pageVo);
    }

    private int isCollectedByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getUserId, userId);
        queryWrapper.eq(Collect::getArticleId, articleId);
        Collect collect = getOne(queryWrapper);
        if (Objects.isNull(collect)) {
            return -1;
        }

        return collect.getCollectStatus();
    }

    private Long getCollectCountByArticleId(Long articleId) {
        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collect::getArticleId, articleId);
        queryWrapper.eq(Collect::getCollectStatus, "1");
        return count(queryWrapper);
    }

}

