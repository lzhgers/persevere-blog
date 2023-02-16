package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryArticleForm;

/**
 * @author LZH
 * @date 2023/2/16
 */
public interface SysArticleService {
    ResponseResult pageList(QueryArticleForm queryArticleForm);
}
