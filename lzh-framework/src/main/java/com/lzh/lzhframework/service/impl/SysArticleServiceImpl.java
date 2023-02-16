package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.service.SysArticleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Service
public class SysArticleServiceImpl implements SysArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult pageList(QueryArticleForm form) {

        Page<Article> page = new Page<>(form.getPageNum(), form.getPageSize());

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(form.getTitle()), Article::getTitle, form.getTitle());
        queryWrapper.like(StringUtils.hasText(form.getContent()), Article::getContent, form.getContent());
        articleMapper.selectPage(page, queryWrapper);

        PageVo pageVo = new PageVo(page.getTotal(), page.getRecords());
        return ResponseResult.okResult(pageVo);
    }
}
