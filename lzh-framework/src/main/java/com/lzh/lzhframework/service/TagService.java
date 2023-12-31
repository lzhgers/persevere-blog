package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Tag;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-09-29 13:17:56
 */
public interface TagService extends IService<Tag> {

    List<Tag> listAll();

    List<Tag> getTagsByArticleId(Long articleId);

    ResponseResult getArticlesByTag(Integer pageNum, Integer pageSize, Long userId, Long tagId);

    Long getMaxSortTag();
}

