package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.ArticleTag;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.QueryTagForm;
import com.lzh.lzhframework.service.ArticleTagService;
import com.lzh.lzhframework.service.SysTagService;
import com.lzh.lzhframework.service.TagService;
import com.lzh.lzhframework.utils.UnderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LZH
 * @date 2023/2/23
 */
@Service
public class SysTagServiceImpl implements SysTagService {

    @Resource
    private TagService tagService;

    @Resource
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult pageList(QueryTagForm form) {

        Page<Tag> page = new Page<>(form.getPageNum(), form.getPageSize());


        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.like(StringUtils.hasText(form.getName()), "name", form.getName());

        //排序
        List<Map<String, String>> sortArr = form.getSortArr();
        for (Map<String, String> map : sortArr) {
            String order = map.get("order");
            String prop = map.get("prop");
            prop = UnderUtil.camel2under(prop);
            if ("ascending".equals(order)) {
                tagQueryWrapper.orderByAsc(prop);
            } else if ("descending".equals(order)) {
                tagQueryWrapper.orderByDesc(prop);
            }
        }
        tagService.page(page, tagQueryWrapper);

        PageVo pageVo = new PageVo(page.getTotal(), page.getRecords());
        return ResponseResult.success(pageVo);
    }

    @Override
    public ResponseResult addTag(String name, Long sort) {
        if (!StringUtils.hasText(name)) {
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        Tag tag = new Tag().setName(name).setSort(sort);
        tagService.save(tag);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, id);
        long count = articleTagService.count(queryWrapper);
        if (count > 0) {
            throw new SystemException(AppHttpCodeEnum.TAG_HAS_BLOG);
        }
        tagService.removeById(id);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteBatch(List<Long> ids) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleTag::getTagId, ids);
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        if (Objects.isNull(articleTagList) || articleTagList.size() == 0) {
            tagService.removeBatchByIds(ids);
            return ResponseResult.success();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_HAS_BLOG);
        }
    }

    @Override
    public ResponseResult editTag(Long id, String name, Long sort) {
        Tag tag = new Tag().setId(id).setName(name).setSort(sort);
        tagService.updateById(tag);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult topTag(Long id, Long sort) {
        Long maxSort = tagService.getMaxSortTag();
        if (sort.equals(maxSort)) {
            throw new SystemException(AppHttpCodeEnum.TAG_IS_TOP);
        }
        Tag tag = new Tag().setId(id).setSort(maxSort + 1);
        tagService.updateById(tag);
        return ResponseResult.success();
    }
}
