package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.annotation.InvokeAn;
import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.dao.ArticleMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.*;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import com.lzh.lzhblog.domain.vo.PageVo;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.service.LikeStatService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import com.lzh.lzhblog.utils.RedisCache;
import com.lzh.lzhblog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-28 10:16:21
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeStatService likeStatService;

    @Autowired
    private RedisCache redisCache;

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
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getDelFlag, 0);
        queryWrapper.eq(Article::getStatus, "0");

        page(page, queryWrapper);

        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleVo.class);

        //判断用户是否登陆
        try {
            Long userId = SecurityUtils.getUserId();
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
            loginUser.getUser().getId();
            //已登录
            //查询当前登陆用户点赞文章情况
//            articleVoList.stream()
//                    .map(articleVo -> {
//
//                    })
        } catch (Exception e) {
            //未登录
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

    @Override
    public ResponseResult addArticle(Article article) {
        super.save(article);
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


}

