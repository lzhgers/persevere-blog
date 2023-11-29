package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.Tag;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


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

    Long getMaxSortTag();

    /**
     * 根据标签查询文章数量
     * @return
     */
    @MapKey(value = "id")
    List<Map<String, Object>> queryArticleNumByTag();
}

