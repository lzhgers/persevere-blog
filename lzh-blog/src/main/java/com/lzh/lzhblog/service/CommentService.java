package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.AddCommentDTO;
import com.lzh.lzhblog.domain.entity.Comment;
import com.lzh.lzhblog.domain.vo.CommentVo;

import java.util.List;


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

