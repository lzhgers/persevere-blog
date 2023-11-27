package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.SubscribeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Resource
    private SubscribeService subscribeService;

    @PostMapping
    public ResponseResult subscribeUser(Long subscribe, Long beSubscribe) {
        return subscribeService.subscribeUser(subscribe, beSubscribe);
    }

    @GetMapping("/pageUserSubscribed/{userId}")
    public ResponseResult pageUserSubscribedByUserId(@PathVariable Long userId, Integer pageNum, Integer pageSize) {
        return subscribeService.pageUserSubscribedByUserId(userId, pageNum, pageSize);
    }

    @PutMapping("/noSubscribe")
    public ResponseResult noSubscribe(Long subscribe, Long beSubscribe) {
        return subscribeService.noSubscribe(subscribe, beSubscribe);
    }
}
