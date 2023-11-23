package com.lzh.lzhblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhblog.annotation.InvokeAn;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Article;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.domain.entity.UserLike;
import com.lzh.lzhframework.domain.vo.ArticleViewRankVo;
import com.lzh.lzhframework.domain.vo.ArticleVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.service.*;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.RedisCache;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserLikeService userLikeService;

    @Resource
    private TagService tagService;

    @Resource
    private CollectService collectService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MongoTemplate mongoTemplate;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<ArticleVo> articleVoList = articleService.listAll();
        return ResponseResult.success(articleVoList);
    }

    @GetMapping("/countCommentsByArticleId/{articleId}")
    public ResponseResult countCommentsByArticleId(@PathVariable Long articleId) {
        long count = commentService.countCommentsByArticleId(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("commentCount", count);
        return ResponseResult.success(map);
    }

    @GetMapping("/pageListAll")
    public ResponseResult pageListAll(Integer pageNum, Integer pageSize, Long userId, String keyword) {
        return articleService.pageListAll(pageNum, pageSize, userId, keyword);
    }

    @InvokeAn
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id, Long userId) {
        Article article = articleService.getArticleById(id);
//        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        articleLambdaQueryWrapper.select(
//                Article::getId,
//                Article::getCategoryId,
//                Article::getThumbnail,
//                Article::getIsTop,
//                Article::getStatus,
//                Article::getViewCount,
//                Article::getCollectCount,
//                Article::getLikedCount,
//                Article::getIsComment,
//                Article::getCreateBy,
//                Article::getCreateTime,
//                Article::getDelFlag,
//                Article::getLikedCount
//        );
//        articleLambdaQueryWrapper.eq(Article::getId, id);
//        Article article = articleService.getOne(articleLambdaQueryWrapper);
//
//        ArticleEntity articleEntity = mongoTemplate.findById(id, ArticleEntity.class);
//        assert articleEntity != null;
//        BeanUtils.copyProperties(articleEntity, article);

        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);

        List<Tag> tagList = tagService.getTagsByArticleId(id);
        List<Long> tagIds = tagList.stream().map(Tag::getId).collect(Collectors.toList());
        articleVo.setTagIds(tagIds);

        try {
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
            loginUser.getUser().getId();

            //封装用户收藏情况
            Integer collectStatus = articleService.getCollectStmt(id, userId);
            articleVo.setCollectStatus(collectStatus);

            //封装用户点赞情况
            LambdaQueryWrapper<UserLike> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(UserLike::getLikedStatus);
            queryWrapper.eq(UserLike::getUserId, userId);
            queryWrapper.eq(UserLike::getLikedId, id);
            queryWrapper.select(UserLike::getLikedStatus);
            UserLike userLike = userLikeService.getOne(queryWrapper);
            articleVo.setLikedStatus(userLike.getLikedStatus());
        } catch (Exception e) {
            articleVo.setLikedStatus(-1);
        }
        if (Objects.isNull(article)) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_EXIST);
        }
        return ResponseResult.success(articleVo);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleVo articleVo) {
        return articleService.updateArticle(articleVo);
    }

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleVo articleVo) {
        ResponseResult result = articleService.addArticle(articleVo);
        // 更新缓存
        updateViewCountTopOnRedis();
        return result;
    }

    @GetMapping("/view/top10")
    public ResponseResult getViewCountTop10Article() {
        List<Article> articleList = articleService.getViewCountTopNumArticle(10);
        return ResponseResult.success(articleList);
    }

    @GetMapping("/view/top4")
    public ResponseResult getViewCountTop4Article() {

        Set set = redisTemplate.opsForZSet().reverseRange(SysConstants.ARTICLE_VIEW_RANK, 0, 3);

        if (!Objects.isNull(set) && set.size() > 0) {
            List<ArticleViewRankVo> articleViewRankVos = new ArrayList<>();
            for (Object o : set) {
                articleViewRankVos.add((ArticleViewRankVo) o);
            }
            return ResponseResult.success(articleViewRankVos);
        }
        List<Article> articleList = articleService.getViewCountTopNumArticle(4);
        return ResponseResult.success(articleList);
    }

    @GetMapping("/selectByKeyword")
    public ResponseResult selectByKeyword(String keyword) {
        List<Article> articleList = articleService.selectByKeyword(keyword);
        return ResponseResult.success(articleList);
    }

    @GetMapping("/listDiffDate")
    public ResponseResult listDiffDate() {
        List<String> diffDateVos = articleService.listDiffDate();
        return ResponseResult.success(diffDateVos);
    }

    @GetMapping("/listArticleByDate")
    public ResponseResult listArticleByDate(String date) {
        List<Article> articleList = articleService.listArticleByDate(date);

        List<ArticleVo> articleVoList = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);

        articleVoList = articleVoList.stream()
                .map(articleVo -> {
                    List<Tag> tags = tagService.getTagsByArticleId(articleVo.getId());
                    articleVo.setTags(tags);
                    return articleVo;
                }).collect(Collectors.toList());

        return ResponseResult.success(articleVoList);
    }

    @GetMapping("/countCollect")
    public ResponseResult countCollect(Long articleId) {
        Long countCollect = articleService.countCollect(articleId);
        return ResponseResult.success(countCollect);
    }

    @GetMapping("/getCollectStmt")
    public ResponseResult getCollectStmt(Long articleId, Long userId) {
        return ResponseResult.success(articleService.getCollectStmt(articleId, userId));
    }

    @PostMapping("/addCollect/{userId}/{articleId}")
    public ResponseResult addCollection(@PathVariable Long userId,
                                        @PathVariable Long articleId) {
        return collectService.addCollection(userId, articleId);
    }

    @GetMapping("/pageUserPublishArticle/{userId}")
    public ResponseResult pageUserPublishArticle(@PathVariable Long userId, Integer pageNum, Integer pageSize) {
        return articleService.pageUserPublishArticle(userId, pageNum, pageSize);
    }

    @GetMapping("/pageUserRoughArticle")
    public ResponseResult pageUserRoughArticle(Long userId, Integer pageNum, Integer pageSize) {
        return articleService.pageUserRoughArticle(userId, pageNum, pageSize);
    }

    @GetMapping("/getArticleByArticleId/{articleId}")
    public ResponseResult getArticleByArticleId(@PathVariable Long articleId) {
        Article article = articleService.getById(articleId);
        return ResponseResult.success(article);
    }

    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable Long articleId) {
        articleService.removeById(articleId);

        updateViewCountTopOnRedis();
        return ResponseResult.success();
    }

    private void updateViewCountTopOnRedis() {
        // 更新redis rank
        redisTemplate.delete(SysConstants.ARTICLE_VIEW_RANK);
        List<Article> articleList = articleService.list();
        List<ArticleViewRankVo> articleViewLists = BeanCopyUtils.copyBeanList(articleList, ArticleViewRankVo.class);

        Set<DefaultTypedTuple<ArticleViewRankVo>> tuples = articleViewLists.stream()
                .map(articleViewRankVo -> new DefaultTypedTuple<>(articleViewRankVo, articleViewRankVo.getViewCount().doubleValue()))
                .collect(Collectors.toSet());

        redisTemplate.opsForZSet().add(SysConstants.ARTICLE_VIEW_RANK, tuples);
    }

}