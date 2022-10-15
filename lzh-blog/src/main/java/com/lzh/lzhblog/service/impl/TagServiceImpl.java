package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.dao.TagMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.LoginUser;
import com.lzh.lzhblog.domain.entity.Tag;
import com.lzh.lzhblog.domain.entity.UserLike;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import com.lzh.lzhblog.domain.vo.PageVo;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.service.TagService;
import com.lzh.lzhblog.service.UserLikeService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import com.lzh.lzhblog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-29 13:17:56
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLikeService userLikeService;

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

    @Override
    public ResponseResult getArticlesByTag(Integer pageNum, Integer pageSize, Long userId, Long tagId) {
        TagMapper tagMapper = getBaseMapper();
        List<Long> articlesId = tagMapper.getArticlesIdByTagId(tagId);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getId, articlesId);
        queryWrapper.eq(Article::getStatus, "0");

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        List<ArticleVo> articleVoList = null;
        try {
            articleService.page(page, queryWrapper);

            articleVoList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);

            articleVoList = articleVoList.stream()
                    .map(articleVo -> {
                        articleVo.setCommentCount(commentService.countCommentsByArticleId(articleVo.getId()));
                        return articleVo;
                    }).collect(Collectors.toList());

            //查询当前登陆用户点赞文章情况
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

        } catch (Exception e) {
            //产生异常，说明没有查询到任何数据          IN ()
            return ResponseResult.okResult(new PageVo(0L, null));
        }
        PageVo pageVo = new PageVo(page.getTotal(), articleVoList);
        return ResponseResult.okResult(pageVo);
    }

}

