package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.UserStatus;

/**
 * (UserStatus)表服务接口
 *
 * @author makejava
 * @since 2022-10-19 15:06:10
 */
public interface UserStatusService extends IService<UserStatus> {

    Long countArticle(Long userId);

    Long countLiked(Long userId);

    Long countViewCount(Long userId);

    ResponseResult showInfo(Long articleId);

    Long countComment(Long userId);
}

