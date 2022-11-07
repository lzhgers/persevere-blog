package com.lzh.lzhblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.common.domain.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-29 11:32:47
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getTagsByArticleId(Long articleId);

    List<Long> getArticlesIdByTagId(Long tagId);
}

