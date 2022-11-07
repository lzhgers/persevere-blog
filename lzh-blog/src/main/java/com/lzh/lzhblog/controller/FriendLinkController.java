package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.FriendLink;
import com.lzh.lzhblog.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friendlink")
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        List<FriendLink> friendLinks = friendLinkService.list();
        return ResponseResult.okResult(friendLinks);
    }

}
