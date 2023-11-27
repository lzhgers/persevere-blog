package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.AddCommentDTO;
import com.lzh.lzhframework.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luzhiheng
 * @date 2023-01-12
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private CommentService commentService;

    @GetMapping("/listAllChat")
    public ResponseResult listAll(Integer pageNum, Integer pageSize) {
        return commentService.listAllChat(pageNum, pageSize);
    }

    @PostMapping("/sendChat")
    public ResponseResult addChat(@RequestBody AddCommentDTO addCommentDTO) {
        return commentService.sendChat(addCommentDTO);
    }

}
