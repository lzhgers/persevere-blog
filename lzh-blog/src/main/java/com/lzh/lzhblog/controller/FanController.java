package com.lzh.lzhblog.controller;

import com.lzh.common.domain.ResponseResult;
import com.lzh.lzhblog.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fan")
public class FanController {

    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("/pageUserFan")
    public ResponseResult pageUserFan(Long userId, Integer pageNum, Integer pageSize) {
        return subscribeService.pageUserFan(userId, pageNum, pageSize);
    }

    @PostMapping("/subscribeFan")
    public ResponseResult subscribeFan(Long userId, Long fanId) {
        return subscribeService.subscribeFan(userId, fanId);
    }

    @PutMapping("/noSubEach")
    public ResponseResult noSubEach(Long userId, Long fanId) {
        return subscribeService.noSubEach(userId, fanId);
    }
}
