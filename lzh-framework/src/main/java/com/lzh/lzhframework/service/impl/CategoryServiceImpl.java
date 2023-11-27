package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.CategoryMapper;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.domain.vo.ArticleVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.TagService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-10-01 09:03:57
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Override
    public List<Category> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, "0");
        return list(queryWrapper);
    }

    @Override
    public Category getCategoryByArticleId(Long articleId) {
        Article article = articleService.getArticleById(articleId);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, article.getCategoryId());
        queryWrapper.eq(Category::getStatus, "0");
        Category category = super.getOne(queryWrapper);
        if (Objects.isNull(category)) {
            return new Category();
        }
        return category;
    }

    @Override
    public List<ArticleVo> getArticleByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId, categoryId);
        queryWrapper.eq(Article::getStatus, "0");
        queryWrapper = articleService.selectExpectContentAndHtmlField(queryWrapper);

        List<Article> articleList = articleService.list(queryWrapper);
        //封装文章对应的标签
        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);
        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    List<Tag> tags = tagService.getTagsByArticleId(articleVo.getId());
                    articleVo.setTags(tags);
                    return articleVo;
                }).collect(Collectors.toList());
        return articleVoList;
    }

    @Override
    public Long getMaxCategorySort() {
        return baseMapper.getMaxSortCategory();
    }

}

