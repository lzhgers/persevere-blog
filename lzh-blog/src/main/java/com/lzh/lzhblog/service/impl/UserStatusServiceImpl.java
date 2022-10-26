package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.dao.CommentMapper;
import com.lzh.lzhblog.dao.UserStatusMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Comment;
import com.lzh.lzhblog.domain.entity.UserStatus;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.service.SubscribeService;
import com.lzh.lzhblog.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        //获取改用户发布的所有文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getCreateBy, userId);
        articleLambdaQueryWrapper.eq(Article::getStatus, "0");
        List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);

        Long commentCount = 0L;
        //统计每篇文章的评论数
        for (Article article : articleList) {
            Long articleId = article.getId();
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
            commentLambdaQueryWrapper.eq(Comment::getType, "0");
            Long count = commentMapper.selectCount(commentLambdaQueryWrapper);
            commentCount += count;
        }

        return commentCount;
    }

    private List<Article> getArticleByUserId(Long userId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "0");
       return articleService.list(queryWrapper);
    }
}

