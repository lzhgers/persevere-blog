package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.UserStatus;


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

    ResponseResult showInfo(Long userId);
}

