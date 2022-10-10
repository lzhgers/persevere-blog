package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.service.LikeService;
import com.lzh.lzhblog.service.LikeStatService;
import com.lzh.lzhblog.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeStatService likeStatService;

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/{articleId}")
    public ResponseResult getLikeCountByArticleId(@PathVariable Long articleId) {

        Long likeCount = likeStatService.getLikeCountByArticleId(articleId);
        return ResponseResult.okResult(likeCount);
    }

    /**
     * 判断文章是否点过赞
     * @param userId 用户id
     * @param articleId 文章id
     * @return -1 该文章未有用户点过赞    0 用户未点赞     1 用户已点赞
     */
    @GetMapping("/isLiked")
    public ResponseResult isLikedByUserIdAndArticleId(Long userId, Long articleId) {
        int isLiked = userLikeService.isLikedByUserIdAndArticleId(userId, articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("isLiked", isLiked);
        return ResponseResult.okResult(map);
    }

    @PostMapping("/add/{userId}/{articleId}")
    public ResponseResult addLike(@PathVariable Long userId,
                                  @PathVariable Long articleId) {
        return likeService.addUserLikeArticle(userId, articleId);
    }
}
