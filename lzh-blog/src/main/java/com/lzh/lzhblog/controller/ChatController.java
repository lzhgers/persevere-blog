package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.AddCommentDTO;
import com.lzh.lzhblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
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
