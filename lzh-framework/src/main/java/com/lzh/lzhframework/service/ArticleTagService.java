package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.entity.ArticleTag;

import java.util.List;

/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2022-10-12 09:00:55
 */
public interface ArticleTagService extends IService<ArticleTag> {

    List<Long> getArticleIdByTagId(Long tagId);
}

