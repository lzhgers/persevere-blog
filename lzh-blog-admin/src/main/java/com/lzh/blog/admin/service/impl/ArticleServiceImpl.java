package com.lzh.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.blog.admin.dao.*;
import com.lzh.blog.admin.domain.ResponseResult;
import com.lzh.blog.admin.domain.entity.*;
import com.lzh.blog.admin.domain.vo.ArticleIsCommentTopVo;
import com.lzh.blog.admin.domain.vo.ArticleVo;
import com.lzh.blog.admin.domain.vo.PageVo;
import com.lzh.blog.admin.service.ArticleService;
import com.lzh.blog.admin.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ResponseResult pageListAllArticle(String title, String summary, Long categoryId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);
        queryWrapper.like(StringUtils.hasText(summary), Article::getSummary, summary);
        queryWrapper.eq(!categoryId.equals(-1L) && !Objects.isNull(categoryId), Article::getCategoryId, categoryId);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articleList = page.getRecords();
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);
        //分装分类名称
        articleVos = articleVos.stream()
                .map(articleVo -> {
                    articleVo.setCategoryName(getCategoryNameByCategoryId(articleVo.getCategoryId()));
                    return articleVo;
                }).collect(Collectors.toList());


        PageVo pageVo = new PageVo(page.getTotal(), articleVos);

        return ResponseResult.okResult(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult deleteArticleByArticleId(Long articleId) {

        //删除文章标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagMapper.delete(articleTagLambdaQueryWrapper);

        //删除文章点赞
        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLikeLambdaQueryWrapper.eq(UserLike::getLikedId, articleId);
        userLikeMapper.delete(userLikeLambdaQueryWrapper);

        //删除文章收藏
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.eq(Collect::getArticleId, articleId);
        collectMapper.delete(collectLambdaQueryWrapper);

        //删除文章评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        commentMapper.delete(commentLambdaQueryWrapper);

        //删除文章
        removeById(articleId);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticleCommentTop(Long articleId, String isTop, String isComment) {
        Article article = new Article().setId(articleId);

        if (StringUtils.hasText(isTop)) {
            article.setIsTop(isTop);
        }
        if (StringUtils.hasText(isComment)) {
            article.setIsComment(isComment);
        }
        updateById(article);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCommentTopById(Long articleId) {
        Article article = getById(articleId);
        ArticleIsCommentTopVo commentTopVo = BeanCopyUtils.copyBean(article, ArticleIsCommentTopVo.class);
        return ResponseResult.okResult(commentTopVo);
    }

    public String getCategoryNameByCategoryId(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (Objects.isNull(category)) {
            return "";
        }
        return category.getName();
    }


}
