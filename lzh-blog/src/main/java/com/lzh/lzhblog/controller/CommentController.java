package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.AddCommentDTO;
import com.lzh.lzhframework.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/commentList")
    public ResponseResult listAllComment(Integer pageNum, Integer pageSize, Long articleId) {
        return commentService.listAllComment(pageNum, pageSize, articleId);
    }

    @PostMapping
    public ResponseResult sendComment(@RequestBody AddCommentDTO addCommentDTO) {
        boolean result = commentService.sendComment(addCommentDTO);
        return ResponseResult.success(result);
    }

    @GetMapping("/article/countComment")
    public ResponseResult countComment(Long articleId) {
        Long count = commentService.countComment(articleId);
        return ResponseResult.success(count);
    }

    @GetMapping("/article/{id}")
    public ResponseResult getCommentByCommentId(@PathVariable Long id) {
        return commentService.getCommentByCommentId(id);
    }
}
