package com.lzh.lzhblog.controller;

import com.lzh.common.domain.ResponseResult;
import com.lzh.lzhblog.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @GetMapping("pageArticleByUserId/{userId}")
    public ResponseResult pageArticleByUserId(@PathVariable Long userId,
                                              Integer pageNum, Integer pageSize) {
        return collectService.pageArticleByUserId(userId, pageNum, pageSize);
    }

}
