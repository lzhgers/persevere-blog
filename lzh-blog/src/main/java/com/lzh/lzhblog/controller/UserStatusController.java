package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.service.SubscribeService;
import com.lzh.lzhblog.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userStatus")
public class UserStatusController {

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("/countArticle/{userId}")
    public ResponseResult countArticle(@PathVariable Long userId) {
        Long articleCount = userStatusService.countArticle(userId);
        return ResponseResult.okResult(articleCount);
    }

    @GetMapping("/countLiked/{userId}")
    public ResponseResult countLiked(@PathVariable Long userId) {
        Long likedCount = userStatusService.countLiked(userId);
        return ResponseResult.okResult(likedCount);
    }

    @GetMapping("/countViewCount/{userId}")
    public ResponseResult countViewCount(@PathVariable Long userId) {
        Long viewCount = userStatusService.countViewCount(userId);
        return ResponseResult.okResult(viewCount);
    }

    @GetMapping("/showInfo/{userId}")
    public ResponseResult showInfo(@PathVariable Long userId) {
        return userStatusService.showInfo(userId);
    }

    @GetMapping("/countFans/{userId}")
    public ResponseResult countFans(@PathVariable Long userId) {
        return subscribeService.countFans(userId);
    }

    @GetMapping("/countSubscribe/{userId}")
    public ResponseResult countSubscribe(@PathVariable Long userId) {
        return subscribeService.countSubscribe(userId);
    }
}
