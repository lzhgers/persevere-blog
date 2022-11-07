package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.common.domain.entity.Article;
import com.lzh.common.domain.entity.Category;
import com.lzh.common.domain.entity.Tag;
import com.lzh.common.domain.vo.ArticleVo;
import com.lzh.lzhblog.dao.CategoryMapper;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CategoryService;
import com.lzh.lzhblog.service.TagService;
import com.lzh.common.utils.BeanCopyUtils;
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

}

