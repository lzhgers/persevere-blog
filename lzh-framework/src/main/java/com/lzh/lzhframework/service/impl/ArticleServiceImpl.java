package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.dao.CollectMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.*;
import com.lzh.lzhframework.domain.vo.ArticleVo;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.ArticleTagService;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.service.UserLikeService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-28 10:16:21
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLikeService userLikeService;

    @Resource
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public List<ArticleVo> listAll() {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getDelFlag, 0);
        queryWrapper.eq(Article::getStatus, "0");
        List<Article> articleList = super.list(queryWrapper);

        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);

        articleVos.stream()
                .map(articleVo -> {
                    long commentCount = commentService.countCommentsByArticleId(articleVo.getId());
                    articleVo.setCommentCount(commentCount);
                    return articleVo;
                }).collect(Collectors.toList());

        return articleVos;
    }

    @Override
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId, String keyword) {

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Article::getId,
                Article::getTitle,
                Article::getSummary,
                Article::getCategoryId,
                Article::getThumbnail,
                Article::getIsTop,
                Article::getStatus,
                Article::getViewCount,
                Article::getCollectCount,
                Article::getLikedCount,
                Article::getIsComment,
                Article::getCreateBy,
                Article::getCreateTime,
                Article::getLikedCount
        );
        queryWrapper.eq(Article::getStatus, "0")
                .and(StringUtils.hasText(keyword)
                        , i -> i.like(Article::getTitle, keyword)
                                .or()
                                .like(Article::getSummary, keyword)
                );

        queryWrapper.orderByDesc(Article::getIsTop);

        page(page, queryWrapper);

        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);

        articleVoList = articleVoList.stream()
                .peek(articleVo -> articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId())))
                .collect(Collectors.toList());

        //查询当前登陆用户点赞文章情况
        log.info("查询当前登陆用户点赞文章情况----------------------------");
        try {
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
            Long id = loginUser.getUser().getId();
            articleVoList = articleVoList.stream()
                    .peek(articleVo -> {
                        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        userLikeLambdaQueryWrapper.eq(UserLike::getLikedId, articleVo.getId());
                        userLikeLambdaQueryWrapper.eq(UserLike::getUserId, id);
                        UserLike userLike = userLikeService.getOne(userLikeLambdaQueryWrapper);

                        if (Objects.isNull(userLike)) {
                            articleVo.setLikedStatus(0);
                        } else if (0 == userLike.getLikedStatus()) {
                            articleVo.setLikedStatus(0);
                        } else {
                            articleVo.setLikedStatus(1);
                        }
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            articleVoList = articleVoList.stream().peek(articleVo -> articleVo.setLikedStatus(-1)).collect(Collectors.toList());
        }

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.success(pageVo);
    }


    @Override
    public Article getArticleById(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Article::getId,
                Article::getTitle,
                Article::getContent,
                Article::getHtml,
                Article::getSummary,
                Article::getCategoryId,
                Article::getThumbnail,
                Article::getIsTop,
                Article::getStatus,
                Article::getViewCount,
                Article::getCollectCount,
                Article::getLikedCount,
                Article::getIsComment,
                Article::getCreateBy,
                Article::getCreateTime,
                Article::getDelFlag,
                Article::getLikedCount
        );
        queryWrapper.eq(Article::getId, id);
        return getOne(queryWrapper);
    }

    @Transactional
    @Override
    public ResponseResult updateArticle(ArticleVo articleVo) {
        log.info("---------------更新草稿");

        //更新文章
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        updateById(article);

        //获取更新的标签
        List<Long> tagIds = articleVo.getTagIds();

        //查询文章所有标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);

        //删除旧标签
        for (ArticleTag articleTag : articleTags) {
            LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleTag::getTagId, articleTag.getTagId());
            queryWrapper.eq(ArticleTag::getArticleId, articleTag.getArticleId());
            articleTagService.remove(queryWrapper);
        }

        //保存新标签
        for (Long tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag()
                    .setArticleId(article.getId())
                    .setTagId(tagId);
            articleTagService.save(articleTag);
        }

        return ResponseResult.success();
    }

    @Transactional
    @Override
    public ResponseResult addArticle(ArticleVo articleVo) {
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);

        //不是草稿
        //保存文章
        super.save(article);

        if (articleVo.getTagIds().size() > 0) {
            // 更新文章的标签
            List<Long> tagIds = articleVo.getTagIds();
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag(article.getId(), tagId);
                articleTagService.save(articleTag);
            }
        }
        return ResponseResult.success();
    }

    @Override
    public Long getLikeCountByArticleId(Long articleId) {
        try {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getId, articleId);
            queryWrapper.select(Article::getLikedCount);
            Article article = getOne(queryWrapper);
            return article.getLikedCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public List<Article> getViewCountTopNumArticle(Integer topNum) {
        ArticleMapper articleMapper = getBaseMapper();
        return articleMapper.getViewCountTopNumArticle(topNum);
    }

    @Override
    public List<Article> selectByKeyword(String keyword) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(keyword), Article::getTitle, keyword)
                .or()
                .like(StringUtils.hasText(keyword), Article::getSummary, keyword);
        return list(queryWrapper);
    }

    @Override
    public List<String> listDiffDate() {
        ArticleMapper articleMapper = getBaseMapper();
        //获取所有文章
        List<Article> articleList = articleMapper.selectList(null);
        //获取所有文章日期
        List<Date> dates = articleList.stream().map(Article::getCreateTime).collect(Collectors.toList());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        List<String> stringDate = dates.stream()
                .map(sdf::format)
                .distinct()
                .sorted((o1, o2) -> o2.compareTo(o1))
                .collect(Collectors.toList());

        return stringDate;
    }

    @Override
    public List<Article> listArticleByDate(String date) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateTime);
        List<Article> articleList = list(queryWrapper);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

        List<Article> articles = new ArrayList<>();
        for (Article article : articleList) {
            Date createTime = article.getCreateTime();
            String format = sdf.format(createTime);
            if (date.equals(format)) {
                articles.add(article);
            }
        }

        return articles;
    }

    @Override
    public Long countCollect(Long articleId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, articleId);
        queryWrapper.eq(Article::getStatus, "0");
        return getOne(queryWrapper).getCollectCount();
    }

    @Override
    public Integer getCollectStmt(Long articleId, Long userId) {

        Integer collectStatus;
        try {
            LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            collectLambdaQueryWrapper.eq(Collect::getUserId, userId);
            collectLambdaQueryWrapper.eq(Collect::getArticleId, articleId);
            collectLambdaQueryWrapper.select(Collect::getCollectStatus);
            Collect collect = collectMapper.selectOne(collectLambdaQueryWrapper);
            collectStatus = collect.getCollectStatus();
        } catch (Exception e) {
            return -1;
        }

        return collectStatus;
    }

    @Override
    public ResponseResult pageUserPublishArticle(Long userId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "0");

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articleList = page.getRecords();
        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);
        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId()));
                    return articleVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.success(pageVo);
    }

    @Override
    public ResponseResult pageUserRoughArticle(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy, userId);
        queryWrapper.eq(Article::getStatus, "1");

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articleList = page.getRecords();
        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);
        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId()));
                    return articleVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.success(pageVo);
    }

    @Override
    public LambdaQueryWrapper<Article> selectExpectContentAndHtmlField(LambdaQueryWrapper<Article> queryWrapper) {
        return queryWrapper.select(
                Article::getId,
                Article::getTitle,
                Article::getSummary,
                Article::getCategoryId,
                Article::getThumbnail,
                Article::getIsTop,
                Article::getStatus,
                Article::getViewCount,
                Article::getCollectCount,
                Article::getLikedCount,
                Article::getIsComment,
                Article::getCreateBy,
                Article::getCreateTime,
                Article::getUpdateBy,
                Article::getUpdateTime);
    }

}

