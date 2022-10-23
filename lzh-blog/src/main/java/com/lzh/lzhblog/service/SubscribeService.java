package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Subscribe;
import com.lzh.lzhblog.domain.vo.UserVo;

import java.util.List;


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
}

