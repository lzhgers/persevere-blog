package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.TagMapper;
import com.lzh.lzhblog.domain.entity.Tag;
import com.lzh.lzhblog.service.TagService;
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

}

