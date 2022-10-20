package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.UserStatusMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.UserStatus;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseResult showInfo(Long userId) {
        Long countArticle = countArticle(userId);
        Long countViewCount = countViewCount(userId);
        Long countLiked = countLiked(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("countArticle", countArticle);
        map.put("countViewCount", countViewCount);
        map.put("countLiked", countLiked);

        return ResponseResult.okResult(map);
    }

    private List<Article> getArticleByUserId(Long userId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "0");
       return articleService.list(queryWrapper);
    }
}

