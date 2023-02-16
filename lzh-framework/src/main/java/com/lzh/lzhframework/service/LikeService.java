package com.lzh.lzhframework.service;


import com.lzh.lzhframework.domain.ResponseResult;

public interface LikeService {

    ResponseResult addUserLikeArticle(Long userId, Long articleId);

    ResponseResult getUserLikeArticle(Long userId, Integer pageNum, Integer pageSize);
}
