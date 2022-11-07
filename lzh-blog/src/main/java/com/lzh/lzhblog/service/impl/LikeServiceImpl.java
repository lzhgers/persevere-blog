package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.common.domain.ResponseResult;
import com.lzh.common.domain.entity.Article;
import com.lzh.common.domain.entity.UserLike;
import com.lzh.common.domain.vo.ArticleVo;
import com.lzh.common.domain.vo.PageVo;
import com.lzh.lzhblog.dao.UserLikeMapper;
import com.lzh.lzhblog.service.*;
import com.lzh.common.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private ArticleService articleService;

    @Transactional
    @Override
    public ResponseResult addUserLikeArticle(Long userId, Long articleId) {
        int isLiked = userLikeService.isLikedByUserIdAndArticleId(userId, articleId);

        UserLike userLike = UserLike.builder()
                .userId(userId)
                .likedId(articleId).build();

        switch (isLiked) {
            case 0: { //该用户未点赞
                userLike.setLikedStatus(0);
                userLikeMapper.updateUserLike(userLike);
                break;
            }
            case 1: { //该用户已点赞
                userLike.setLikedStatus(1);
                userLikeMapper.updateUserLike(userLike);
                break;
            }
            case -1: { //该用户从未点过赞
                userLike.setLikedStatus(1);
                userLikeService.save(userLike);
                break;
            }
        }

        Article article1 = new Article()
                .setLikedCount(1L)
                .setId(articleId);

        //根据文章id查询该文章点赞情况
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId, articleId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "0");
        Article article = articleService.getOne(articleLambdaQueryWrapper);

        if (Objects.isNull(article)) {
            //该文章还没有点赞
            articleService.save(article1);
        } else {
            //该文章有点赞
            Long likedCount = articleService.getLikeCountByArticleId(articleId);
            //根据isLiked进行点赞量的修改
            if (1 == isLiked) {
                //该用户已点赞，点赞-1
                article1.setLikedCount(--likedCount);
                articleService.update(article1, articleLambdaQueryWrapper);
            } else {
                //该用户未点赞，点赞+1
                article1.setLikedCount(++likedCount);
                articleService.update(article1, articleLambdaQueryWrapper);
            }
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserLikeArticle(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLikeLambdaQueryWrapper.eq(UserLike::getUserId, userId);
        userLikeLambdaQueryWrapper.eq(UserLike::getLikedStatus, "1");

        List<UserLike> userLikeList = userLikeService.list(userLikeLambdaQueryWrapper);
        List<Long> articleIdList = userLikeList.stream().map(UserLike::getLikedId).collect(Collectors.toList());

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
        return ResponseResult.okResult(pageVo);
    }

}
