package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.dao.CollectMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Collect;
import com.lzh.lzhblog.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

        return ResponseResult.okResult();
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
}

