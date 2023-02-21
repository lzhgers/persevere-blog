package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.*;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.domain.vo.SysArticleVo;
import com.lzh.lzhframework.domain.vo.SysUpdateArticleVo;
import com.lzh.lzhframework.form.QueryArticleForm;
import com.lzh.lzhframework.form.SysSaveArticleForm;
import com.lzh.lzhframework.service.*;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Service
public class SysArticleServiceImpl implements SysArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private CommentService commentService;

    @Override
    public ResponseResult pageList(QueryArticleForm form) {

        String title = form.getTitle();
        String author = form.getAuthor();
        Long categoryId = form.getCategoryId();
        Long tagId = form.getTagId();

        Page<Article> page = new Page<>(form.getPageNum(), form.getPageSize());

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //标题
        queryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);

        //作者
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(author), User::getUserName, author);
        List<User> userList = userService.list(wrapper);
        List<Long> userIds = userList.stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
        if (userIds.size() > 0) {
            queryWrapper.in(Article::getCreateBy, userIds);
        } else {
            queryWrapper.eq(Article::getId, -1);
        }

        //分类
        queryWrapper.eq(!Objects.isNull(categoryId), Article::getCategoryId, categoryId);

        //标签
        if (!Objects.isNull(tagId)) {
            List<Long> articleIds = articleTagService.getArticleIdByTagId(tagId);
            if (articleIds.size() <= 0) {
                queryWrapper.eq(Article::getId, -1L);
            } else {
                queryWrapper.in(Article::getId, articleIds);
            }
        }

        articleMapper.selectPage(page, queryWrapper);

        List<Article> articleList = page.getRecords();


        List<SysArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, SysArticleVo.class);
        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    //设置标签
                    List<Tag> tags = tagService.getTagsByArticleId(articleVo.getId());
                    articleVo.setTags(tags);
                    //设置分类
                    Category category = categoryService.getCategoryByArticleId(articleVo.getId());
                    articleVo.setCategory(category);
                    //设置作者
                    String createBy = userService.getUserNameByUserId(articleVo.getCreateBy());
                    articleVo.setAuthor(createBy);
                    return articleVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.okResult(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult deleteArticleById(Long articleId) {
        //删除文章
        articleMapper.deleteById(articleId);

        //删除文章标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagService.remove(articleTagLambdaQueryWrapper);

        //删除文章评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        commentService.remove(commentLambdaQueryWrapper);

        return ResponseResult.okResult();
    }

    @Override
    public int saveUploadArticle(String markdown, String html, String mdName) {
        Long userId = SecurityUtils.getUserId();
        Article article = new Article().setTitle(mdName)
                .setContent(markdown)
                .setHtml(html)
                .setSummary(mdName)
                .setStatus("2")
                .setCreateBy(userId)
                .setUpdateBy(userId);
        articleMapper.insert(article);
        return 0;
    }

    @Transactional
    @Override
    public ResponseResult saveArticle(SysSaveArticleForm sysSaveArticleForm) {
        Long userId = SecurityUtils.getUserId();
        String isTop = sysSaveArticleForm.getIsTop();
        String status = sysSaveArticleForm.getStatus();
        String isComment = sysSaveArticleForm.getIsComment();

        //保存文章
        Article article = BeanCopyUtils.copyBean(sysSaveArticleForm, Article.class);
        article.setIsTop("true".equals(isTop) ? "1" : "0");
        article.setStatus("true".equals(status) ? "0" : "2");
        article.setIsComment("true".equals(isComment) ? "1" : "0");
        article.setCategoryId(sysSaveArticleForm.getCategory());
        article.setCreateBy(userId);

        articleMapper.insert(article);
        //保存标签
        List<Long> tagIds = sysSaveArticleForm.getTag();
        if (Objects.isNull(tagIds) || tagIds.size() == 0) {
            return ResponseResult.okResult();
        }
        List<ArticleTag> articleTagList = tagIds.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTagList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryUpdateArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        SysUpdateArticleVo vo = BeanCopyUtils.copyBean(article, SysUpdateArticleVo.class);

        Category category = categoryService.getById(article.getCategoryId());
        if (!Objects.isNull(category)) {
            vo.setCategoryId(category.getId());
        }

        List<Tag> tags = tagService.getTagsByArticleId(article.getId());
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());
        vo.setTagIds(tagIds);

        return ResponseResult.okResult(vo);
    }

    @Transactional
    @Override
    public ResponseResult updateArticle(SysUpdateArticleVo vo) {

        //删除原有标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, vo.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);

        //添加修改后的标签
        List<Long> tagIds = vo.getTagIds();
        List<ArticleTag> articleTagList = tagIds.stream()
                .map(tagId -> new ArticleTag(vo.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);

        //修改文章
        Article article = BeanCopyUtils.copyBean(vo, Article.class);
        if (!StringUtils.hasText(article.getThumbnail())) {
            article.setThumbnail(null);
        }
        articleMapper.updateById(article);

        return ResponseResult.okResult();
    }
}
