package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.*;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import com.lzh.lzhblog.domain.vo.PageVo;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.ArticleTagService;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.service.UserLikeService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import com.lzh.lzhblog.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

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
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getDelFlag, 0);
        queryWrapper.eq(Article::getStatus, "0");

        page(page, queryWrapper);

        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);

        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId()));
                    return articleVo;
                }).collect(Collectors.toList());

        //查询当前登陆用户点赞文章情况
        log.info("查询当前登陆用户点赞文章情况----------------------------");
        try {
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
            Long id = loginUser.getUser().getId();
            articleVoList = articleVoList.stream()
                    .map(articleVo -> {
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
                        return articleVo;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            articleVoList = articleVoList.stream()
                    .map(articleVo -> {
                        articleVo.setLikedStatus(-1);
                        return articleVo;
                    }).collect(Collectors.toList());
        }

        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);

        return ResponseResult.okResult(pageVo);
    }


    @Override
    public Article getArticleById(Long id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, id);
        return getOne(queryWrapper);
    }

    @Override
    public ResponseResult updateArticle(Article article) {
        super.updateById(article);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult addArticle(ArticleVo articleVo) {
        //保存文章
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        article.setStatus("0");
        super.save(article);

        // 更新文章的标签
        //统计过滤标签
        List<Long> newTagList = new ArrayList<>();
        List<Long> oldTagList = articleVo.getTagIds();
        Map<Long, Integer> map = new HashMap<>();
        for (Long oldTag : oldTagList) {
            if (Objects.isNull(map.get(oldTag))) {
                map.put(oldTag, 1);
            } else {
                map.put(oldTag, map.get(oldTag) + 1);
            }
        }
        Set<Map.Entry<Long, Integer>> entries = map.entrySet();
        for (Map.Entry<Long, Integer> entry : entries) {
            Long tagId = entry.getKey();
            Integer tagNum = entry.getValue();
            if (0 != tagNum % 2) {
                newTagList.add(tagId);
            }
        }

        for (Long newTag : newTagList) {
            ArticleTag articleTag = new ArticleTag(article.getId(), newTag);
            articleTagService.save(articleTag);
        }

        return ResponseResult.okResult();
    }

    @Override
    public Long getLikeCountByArticleId(Long articleId) {
        try {
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getId, articleId);
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

}

