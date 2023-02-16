package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Subscribe;

/**
 * 用户关注关系表(Subscribe)表服务接口
 *
 * @author makejava
 * @since 2022-10-23 12:54:07
 */
public interface SubscribeService extends IService<Subscribe> {

    ResponseResult subscribeUser(Long subscribe, Long beSubscribe);

    ResponseResult pageUserSubscribedByUserId(Long userId, Integer pageNum, Integer pageSize);

    ResponseResult noSubscribe(Long subscribe, Long beSubscribe);

    ResponseResult pageUserFan(Long userId, Integer pageNum, Integer pageSize);

    ResponseResult subscribeFan(Long userId, Long fanId);

    ResponseResult noSubEach(Long userId, Long fanId);

    Long countFans(Long userId);

    ResponseResult countSubscribe(Long userId);
}

