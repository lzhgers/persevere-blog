package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.dao.TagMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Tag;
import com.lzh.lzhblog.domain.vo.PageVo;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-29 13:17:56
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @Override
    public List<Tag> listAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getDelFlag, 0);
        List<Tag> tagList = super.list(queryWrapper);
        return tagList;
    }

    @Override
    public List<Tag> getTagsByArticleId(Long articleId) {
        TagMapper tagMapper = getBaseMapper();
        return tagMapper.getTagsByArticleId(articleId);
    }

    @Override
    public ResponseResult getArticlesByTag(Integer pageNum, Integer pageSize, Long tagId) {
        TagMapper tagMapper = getBaseMapper();
        List<Long> articlesId = tagMapper.getArticlesIdByTagId(tagId);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getId, articlesId);
        queryWrapper.eq(Article::getStatus, "0");

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        try {
            articleService.page(page, queryWrapper);
        } catch (Exception e) {
            //产生异常，说明没有查询到任何数据          IN ()
            return ResponseResult.okResult(new PageVo(0L, null));
        }
        PageVo pageVo = new PageVo(page.getTotal(), page.getRecords());
        return ResponseResult.okResult(pageVo);
    }

}

