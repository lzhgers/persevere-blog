package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.common.domain.entity.UserLike;


/**
 * 用户点赞表(UserLike)表服务接口
 *
 * @author makejava
 * @since 2022-10-09 18:25:18
 */
public interface UserLikeService extends IService<UserLike> {

    int isLikedByUserIdAndArticleId(Long userId, Long articleId);


}

