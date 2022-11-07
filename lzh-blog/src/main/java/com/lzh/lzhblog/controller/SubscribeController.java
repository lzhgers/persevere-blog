package com.lzh.lzhblog.controller;

import com.lzh.common.domain.ResponseResult;
import com.lzh.lzhblog.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
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
