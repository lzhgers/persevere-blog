package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhblog.dao.LikeStatMapper;
import com.lzh.lzhblog.dao.UserLikeMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.LikeStat;
import com.lzh.lzhblog.domain.entity.UserLike;
import com.lzh.lzhblog.service.LikeService;
import com.lzh.lzhblog.service.LikeStatService;
import com.lzh.lzhblog.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private LikeStatService likeStatService;

    @Autowired
    private LikeStatMapper likeStatMapper;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Transactional
    @Override
    public ResponseResult addUserLikeArticle(Long userId, Long articleId) {
        int isLiked = userLikeService.isLikedByUserIdAndArticleId(userId, articleId);

        UserLike userLike = UserLike.builder()
                .userId(userId)
                .likedId(articleId).build();

        switch (isLiked) {
            case 0: {
                userLike.setLikedStatus(0);
                userLikeMapper.updateUserLike(userLike);
                break;
            }
            case 1: {
                userLike.setLikedStatus(1);
                userLikeMapper.updateUserLike(userLike);
                break;
            }
            case -1: {
                userLike.setLikedStatus(1);
                userLikeService.save(userLike);
                break;
            }
        }

        LikeStat likeStat1 = new LikeStat()
                .setLikedCount(1L)
                .setLikedId(articleId);

        //根据文章id查询该文章点赞情况
        LambdaQueryWrapper<LikeStat> likeStatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        likeStatLambdaQueryWrapper.eq(LikeStat::getLikedId, articleId);
        LikeStat likeStat = likeStatService.getOne(likeStatLambdaQueryWrapper);

        if (Objects.isNull(likeStat)) {
            //该文章还没有点赞
            likeStatService.save(likeStat1);
        } else {
            //该文章有点赞
            Long likedCount = likeStatService.getLikeCountByArticleId(articleId);
            //根据isLiked进行点赞量的修改
            if (1 == isLiked) {
                //该用户已点赞，点赞-1
                likeStat1.setLikedCount(--likedCount);
                likeStatService.update(likeStat1, likeStatLambdaQueryWrapper);
            } else {
                //该用户未点赞，点赞+1
                likeStat1.setLikedCount(++likedCount);
                likeStatService.update(likeStat1, likeStatLambdaQueryWrapper);
            }
        }

        return ResponseResult.okResult();
    }
}
