package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;

/**
 * @author LZH
 * @date 2023/2/16
 */
public interface SysArticleService {
    ResponseResult pageList(QueryArticleForm queryArticleForm);

    ResponseResult deleteArticleById(Long articleId);

    int saveUploadArticle(String html, String mdName);

    ResponseResult saveArticle(SysSaveArticleForm sysSaveArticleForm);
}
