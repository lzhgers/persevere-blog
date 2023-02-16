package com.lzh.lzhframework.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 21:33:39
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}

