package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Collect;

/**
 * (Collect)表服务接口
 *
 * @author makejava
 * @since 2022-10-19 10:51:27
 */
public interface CollectService extends IService<Collect> {

    ResponseResult addCollection(Long userId, Long articleId);

    ResponseResult pageArticleByUserId(Long userId, Integer pageNum, Integer pageSize);

}

