package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.vo.SysUpdateArticleVo;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LZH
 * @date 2023/2/16
 */
public interface SysArticleService {
    ResponseResult pageList(QueryArticleForm queryArticleForm);

    ResponseResult deleteArticleById(Long articleId);

    int saveUploadArticle(String html, String mdName, String markdown);

    ResponseResult saveArticle(SysSaveArticleForm sysSaveArticleForm);

    ResponseResult queryUpdateArticleById(Long id);

    ResponseResult updateArticle(SysUpdateArticleVo sysUpdateArticleVo);

    ResponseResult downLoadFile(Long articleId, HttpServletResponse response);
}
