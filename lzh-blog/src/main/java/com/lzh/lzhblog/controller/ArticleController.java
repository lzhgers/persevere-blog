package com.lzh.lzhblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhblog.annotation.InvokeAn;
import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.Category;
import com.lzh.lzhblog.domain.entity.LoginUser;
import com.lzh.lzhblog.domain.entity.UserLike;
import com.lzh.lzhblog.domain.vo.ArticleVo;
import com.lzh.lzhblog.enums.AppHttpCodeEnum;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.service.UserLikeService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import com.lzh.lzhblog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserLikeService userLikeService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<ArticleVo> articleVoList = articleService.listAll();
        return ResponseResult.okResult(articleVoList);
    }

    @GetMapping("/countCommentsByArticleId/{articleId}")
    public ResponseResult countCommentsByArticleId(@PathVariable Long articleId) {
        long count = commentService.countCommentsByArticleId(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("commentCount", count);
        return ResponseResult.okResult(map);
    }

    @GetMapping("/pageListAll")
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId, String keyword) {
        return articleService.pageListAll(pageNum, pageSize, userId, keyword);
    }

    @InvokeAn
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id, Long userId) {
        Article article = articleService.getArticleById(id);
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        try {
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
            loginUser.getUser().getId();

            LambdaQueryWrapper<UserLike> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserLike::getUserId, userId);
            queryWrapper.eq(UserLike::getLikedId, id);
            UserLike userLike = userLikeService.getOne(queryWrapper);
            articleVo.setLikedStatus(userLike.getLikedStatus());
        } catch (Exception e) {
            articleVo.setLikedStatus(-1);
        }
        if (Objects.isNull(article)) {
            throw new RuntimeException("文章不存在");
        }
        return ResponseResult.okResult(articleVo);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleVo articleVo) {
        return articleService.addArticle(articleVo);
    }

    @GetMapping("/viewCount/top10")
    public ResponseResult getViewCountTop10Article() {
        List<Article> articleList = articleService.getViewCountTopNumArticle(10);
        return ResponseResult.okResult(articleList);
    }

    @GetMapping("/viewCount/top4")
    public ResponseResult getViewCountTop4Article() {
        List<Article> articleList = articleService.getViewCountTopNumArticle(4);
        return ResponseResult.okResult(articleList);
    }

    @GetMapping("/selectByKeyword")
    public ResponseResult selectByKeyword(String keyword) {
        List<Article> articleList = articleService.selectByKeyword(keyword);
        return ResponseResult.okResult(articleList);
    }
}
