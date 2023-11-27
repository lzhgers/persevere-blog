package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author luzhiheng
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @GetMapping("pageArticleByUserId/{userId}")
    public ResponseResult pageArticleByUserId(@PathVariable Long userId,
                                              Integer pageNum, Integer pageSize) {
        return collectService.pageArticleByUserId(userId, pageNum, pageSize);
    }

}
