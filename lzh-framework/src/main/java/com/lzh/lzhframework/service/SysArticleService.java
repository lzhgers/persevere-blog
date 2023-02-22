package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.vo.SysUpdateArticleVo;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    ResponseResult exportArticle(List<Long> articleIds, HttpServletResponse response);

    ResponseResult downLoadFile(String name, HttpServletResponse response);
}
