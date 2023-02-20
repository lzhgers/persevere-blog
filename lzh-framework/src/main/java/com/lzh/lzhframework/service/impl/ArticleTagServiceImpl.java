package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.ArticleTagMapper;
import com.lzh.lzhframework.domain.entity.ArticleTag;
import com.lzh.lzhframework.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-12 09:00:59
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public List<Long> getArticleIdByTagId(Long tagId) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, tagId);
        List<ArticleTag> articleTagList = list(queryWrapper);
        return articleTagList.stream()
                .map(ArticleTag::getArticleId).collect(Collectors.toList());
    }
}

