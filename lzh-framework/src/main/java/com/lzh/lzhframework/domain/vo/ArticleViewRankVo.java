package com.lzh.lzhframework.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LZH
 * @date 2023/5/20
 */
@Data
public class ArticleViewRankVo {

    private Long id;

    //标题
    private String title;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;
}
