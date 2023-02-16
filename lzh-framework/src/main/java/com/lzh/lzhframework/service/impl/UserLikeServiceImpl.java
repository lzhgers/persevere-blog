package com.lzh.lzhframework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.UserLikeMapper;
import com.lzh.lzhframework.domain.entity.UserLike;
import com.lzh.lzhframework.service.UserLikeService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户点赞表(UserLike)表服务实现类
 *
 * @author makejava
 * @since 2022-10-09 18:25:19
 */
@Service("userLikeService")
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

    @Override
    public int isLikedByUserIdAndArticleId(Long userId, Long articleId) {

        LambdaQueryWrapper<UserLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLike::getUserId, userId);
        queryWrapper.eq(UserLike::getLikedId, articleId);

        try {
            UserLike userLike = getOne(queryWrapper);
            Integer likedStatus = userLike.getLikedStatus();
            if (Objects.isNull(likedStatus)) {
                return -1;
            }
            return likedStatus == 0 ? 0 : 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}

