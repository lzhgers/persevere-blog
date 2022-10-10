package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.CommentMapper;
import com.lzh.lzhblog.dao.UserMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.AddCommentDTO;
import com.lzh.lzhblog.domain.entity.Comment;
import com.lzh.lzhblog.domain.entity.User;
import com.lzh.lzhblog.domain.vo.CommentVo;
import com.lzh.lzhblog.domain.vo.PageVo;
import com.lzh.lzhblog.service.CommentService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-09-28 21:33:41
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long countCommentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getType, "0");
        long count = super.count(queryWrapper);
        return count;
    }

    @Override
    public ResponseResult listAllComment(Integer pageNum, Integer pageSize, Long articleId) {

        //分页查询根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, -1L);
        queryWrapper.eq(Comment::getType, "0");

        Page<Comment> page = new Page<>(pageNum, pageSize);
        this.page(page, queryWrapper);

        List<CommentVo> rootCommentVos = BeanCopyUtils.copyBeanList(page.getRecords(), CommentVo.class);
        rootCommentVos = rootCommentVos.stream()
                .map(rootCommentVo -> {
                    String userName = getUserNameByUserId(rootCommentVo.getToCommentId());
                    String avatar = getAvatarByUserId(rootCommentVo.getToCommentId());
                    rootCommentVo.setUserName(userName);
                    rootCommentVo.setAvatar(avatar);
                    return rootCommentVo;
                }).collect(Collectors.toList());

        //查询非根评论
        List<CommentVo> noRootCommentVos = BeanCopyUtils.copyBeanList(getNoRootCommentVos(articleId), CommentVo.class);

        noRootCommentVos = noRootCommentVos.stream()
                .map(noRootCommentVo -> {
                    noRootCommentVo.setAvatar(getAvatarByUserId(noRootCommentVo.getToCommentId()));
                    noRootCommentVo.setUserName(getUserNameByUserId(noRootCommentVo.getToCommentId()));
                    noRootCommentVo.setToCommentUserName(getUserNameByUserId(noRootCommentVo.getToCommentUserId()));
                    return noRootCommentVo;
                }).collect(Collectors.toList());

        //封装子评论
        List<CommentVo> children = null;
        for (CommentVo rootCommentVo : rootCommentVos) {
            //设置根评论用户名
            String userName = getUserNameByUserId(rootCommentVo.getToCommentId());
            rootCommentVo.setUserName(userName);

            children = new ArrayList<>();
            Long rootId = rootCommentVo.getId();
            for (CommentVo noRootCommentVo : noRootCommentVos) {
                if (rootId.equals(noRootCommentVo.getRootId())) {
                    children.add(noRootCommentVo);
                }
            }
            rootCommentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(page.getTotal(), rootCommentVos));
    }

    @Override
    public boolean sendComment(AddCommentDTO addCommentDTO) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDTO, Comment.class);
        return super.save(comment);
    }

    @Override
    public Long countComment(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getType, "0");
        return Long.valueOf(list(queryWrapper).size());
    }

    @Override
    public ResponseResult getCommentByCommentId(Long id) {
        Comment comment = getBaseMapper().selectById(id);
        if (Objects.isNull(comment)) {
            throw new RuntimeException("评论不存在");
        }
        return ResponseResult.okResult(comment);
    }

    private String getAvatarByUserId(Long userId) {
        if (userId.equals(-1L)) {
            return "";
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(queryWrapper);
        return user.getAvatar();
    }

    private String getUserNameByUserId(Long userId) {
        if (userId.equals(-1L)) {
            return "";
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(queryWrapper);
        return user.getUserName();
    }

    private List<Comment> getNoRootCommentVos(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getType, "0");
        queryWrapper.ne(Comment::getRootId, -1L);
        queryWrapper.eq(Comment::getArticleId, articleId);
        return list(queryWrapper);
    }
}

