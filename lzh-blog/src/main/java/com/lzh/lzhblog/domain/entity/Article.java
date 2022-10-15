package com.lzh.lzhblog.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章表(Article)表实体类
 *
 * @author makejava
 * @since 2022-09-28 10:16:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_article")
@Accessors(chain = true)
public class Article {

    @TableId
    private Long id;

    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    private Long viewCount;

//    //评论数
//    private Long commentCount;

    //点赞数
    private Long likedCount;
    //是否允许评论 1是，0否
    private String isComment;

    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @TableLogic
    private Integer delFlag;


}

