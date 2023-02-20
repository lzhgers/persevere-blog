package com.lzh.lzhframework.domain.vo;

import com.lzh.lzhframework.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysArticleVo {

    private Long id;
    //标题
    private String title;
    //创建者id
    private Long createBy;
    // 分类名称
    private String categoryName;
    //缩略图
    private String thumbnail;
    //状态（0已发布，1草稿）
    private String status;

    private String author;

    private Date createTime;

    private Date updateTime;

    private List<Tag> tags;
}
