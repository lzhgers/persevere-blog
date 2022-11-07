package com.lzh.lzhblog.service;


import com.lzh.common.domain.ResponseResult;

public interface LikeService {

    ResponseResult addUserLikeArticle(Long userId, Long articleId);

    ResponseResult getUserLikeArticle(Long userId, Integer pageNum, Integer pageSize);
}
