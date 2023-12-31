package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.dao.CommentMapper;
import com.lzh.lzhframework.dao.UserStatusMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.Comment;
import com.lzh.lzhframework.domain.entity.UserStatus;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.SubscribeService;
import com.lzh.lzhframework.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (UserStatus)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 15:06:10
 */
@Service("userStatusService")
public class UserStatusServiceImpl extends ServiceImpl<UserStatusMapper, UserStatus> implements UserStatusService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private SubscribeService subscribeService;

    @Override
    public Long countArticle(Long userId) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "0");
        return articleService.count(queryWrapper);
    }

    @Override
    public Long countLiked(Long userId) {

        List<Article> articleList = getArticleByUserId(userId);

        Long likedCount = 0L;
        for (Article article : articleList) {
            likedCount += article.getLikedCount();
        }
        return likedCount;
    }

    @Override
    public Long countViewCount(Long userId) {
        List<Article> articleList = getArticleByUserId(userId);

        Long viewCount = 0L;
        for (Article article : articleList) {
            viewCount += article.getViewCount();
        }
        return viewCount;
    }

    @Override
    public ResponseResult showInfo(Long articleId) {

        Article article = articleService.getById(articleId);
        if (Objects.isNull(article)) {
            throw new RuntimeException("文章不存在");
        }
        Long userId = article.getCreateBy();

        Long countArticle = countArticle(userId);
        Long countViewCount = countViewCount(userId);
        Long countLiked = countLiked(userId);
        Long countComment = countComment(userId);
        Long countFans = subscribeService.countFans(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("countArticle", countArticle);
        map.put("countViewCount", countViewCount);
        map.put("countLiked", countLiked);
        map.put("countComment", countComment);
        map.put("countFans", countFans);

        return ResponseResult.okResult(map);
    }

    @Override
    public Long countComment(Long userId) {
        //获取该用户发布的所有文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getCreateBy, userId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "0");
        articleLambdaQueryWrapper.select(Article::getId);
        List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);

        List<Long> idList = articleList.stream().map(Article::getId).collect(Collectors.toList());

        //统计每篇文章的评论数
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getType, "0");
        commentLambdaQueryWrapper.in(Comment::getArticleId, idList);
        Long count = commentMapper.selectCount(commentLambdaQueryWrapper);

        return count;
    }

    private List<Article> getArticleByUserId(Long userId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "0");
       return articleService.list(queryWrapper);
    }
}

