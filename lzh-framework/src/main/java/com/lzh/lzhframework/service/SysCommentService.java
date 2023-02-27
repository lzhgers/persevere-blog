package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryCommentForm;

/**
 * @author LZH
 * @date 2023/2/25
 */
public interface SysCommentService {
    ResponseResult pageList(QueryCommentForm queryCommentForm);

    ResponseResult deleteComment(Long id);
}
