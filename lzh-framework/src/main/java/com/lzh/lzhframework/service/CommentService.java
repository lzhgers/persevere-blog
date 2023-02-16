package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.AddCommentDTO;
import com.lzh.lzhframework.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-09-28 21:33:41
 */
public interface CommentService extends IService<Comment> {

    long countCommentsByArticleId(Long articleId);

    ResponseResult listAllComment(Integer pageNum, Integer pageSize, Long articleId);

    boolean sendComment(AddCommentDTO addCommentDTO);

    Long countComment(Long articleId);

    ResponseResult getCommentByCommentId(Long id);

    ResponseResult listAllChat(Integer pageNum, Integer pageSize);

    ResponseResult sendChat(AddCommentDTO addCommentDTO);
}

