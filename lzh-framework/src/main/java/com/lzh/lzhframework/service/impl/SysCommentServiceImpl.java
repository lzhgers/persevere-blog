package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Comment;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.domain.vo.SysCommentVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.QueryCommentForm;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.service.SysCommentService;
import com.lzh.lzhframework.service.UserService;
import com.lzh.lzhframework.utils.UnderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LZH
 * @date 2023/2/25
 */
@Service
public class SysCommentServiceImpl implements SysCommentService {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Override
    public ResponseResult pageList(QueryCommentForm form) {
        Page<Comment> page = new Page<>(form.getPageNum(), form.getPageSize());
//        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        //评论内容
        commentQueryWrapper.like(StringUtils.hasText(form.getContent()), "content", form.getContent());
        //评论时间
        List<Map<String, String>> sortArr = form.getSortArr();
        for (Map<String, String> map : sortArr) {
            String prop = map.get("prop");
            String order = map.get("order");
            prop = UnderUtil.camel2under(prop);
            if ("ascending".equals(order)) {
                commentQueryWrapper.orderByAsc(prop);
            } else if ("descending".equals(order)) {
                commentQueryWrapper.orderByDesc(prop);
            }
        }

        //评论人
        String commenter = form.getCommenter();
        if (StringUtils.hasText(commenter)) {
            List<User> userList = userService.listUserByName(commenter);
            List<Long> commenterIds = userList.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            if (commenterIds.size() > 0) {
                commentQueryWrapper.in("to_comment_id", commenterIds);
            } else {
                commentQueryWrapper.eq("id", -1);
            }
        }
        //被评论人
        String isCommented = form.getIsCommented();
        if (StringUtils.hasText(isCommented)) {
            List<User> isCommentedList = userService.listUserByName(isCommented);
            List<Long> isCommentedIds = isCommentedList.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            if (isCommentedIds.size() > 0) {
                commentQueryWrapper.in("to_comment_user_id", isCommentedIds);
            } else {
                commentQueryWrapper.eq("id", -1);
            }
        }
        //评论来源
        String commentType = form.getSelectedType();
        commentQueryWrapper.eq(StringUtils.hasText(commentType), "type", commentType);

        commentService.page(page, commentQueryWrapper);

        List<Comment> commentList = page.getRecords();
        List<SysCommentVo> sysCommentVoList = commentList.stream()
                .map(comment -> {
                    //评论人
                    Long toCommentId = comment.getToCommentId();
                    User commentUser = userService.getUserById(toCommentId);
                    String toCommentUserName = commentUser.getUserName();
                    String avatar = commentUser.getAvatar();

                    //被评论人
                    Long toCommentUserId = comment.getToCommentUserId();
                    User isCommentedUser = userService.getUserById(toCommentUserId);
                    String isCommentedUserName = Objects.isNull(isCommentedUser) ?
                            "无" : isCommentedUser.getUserName();

                    //来源
                    String type = comment.getType();
                    String source = getSourceByType(type);

                    SysCommentVo sysCommentVo = SysCommentVo.builder()
                            .id(comment.getId())
                            .avatar(avatar)
                            .commenter(toCommentUserName)
                            .isCommented(isCommentedUserName)
                            .type(source)
                            .content(comment.getContent())
                            .createTime(comment.getCreateTime())

                            .build();
                    return sysCommentVo;
                }).collect(Collectors.toList());


        PageVo pageVo = new PageVo(page.getTotal(), sysCommentVoList);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteComment(Long id) {
        Comment comment = commentService.getById(id);
        Long rootId = comment.getRootId();
        //判断是否为根评论
        if (-1 == rootId) {
            LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getRootId, id);
            List<Comment> commentList = commentService.list(queryWrapper);
            if (commentList.size() <= 0) {
                //没有子评论
                commentService.removeById(id);
            } else {
                //有子评论
                throw new SystemException(AppHttpCodeEnum.HAS_SUB_COMMENT);
            }
        } else {
            commentService.removeById(id);
        }
        return ResponseResult.okResult();
    }

    private String getSourceByType(String type) {
        String source = "";
        switch (type) {
            case "0":
                source = "文章评论";
                break;
            case "1":
                source = "友链评论";
                break;
            case "2":
                source = "问答评论";
                break;
            default:
                source = "其它来源";
        }
        return source;
    }
}
