package com.lzh.lzhblog.service;

import com.lzh.lzhblog.domain.ResponseResult;

public interface LikeService {

    ResponseResult addUserLikeArticle(Long userId, Long articleId);
}
