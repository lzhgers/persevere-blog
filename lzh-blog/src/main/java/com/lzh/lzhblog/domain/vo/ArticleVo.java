package com.lzh.lzhblog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {

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
    //访问量
    private Long viewCount;

    //评论数
    private Long commentCount;

    //点赞数
    private Long likedCount;

    //是否点赞：0未点赞，1已点赞
    private Integer likedStatus;

    private Long createBy;

    private Date createTime;

    private Date updateTime;

    private List<Long> tagIds;

    private List<String> tagNames;
}
