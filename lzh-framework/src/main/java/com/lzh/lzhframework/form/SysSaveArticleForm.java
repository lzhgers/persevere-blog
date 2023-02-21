package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysSaveArticleForm {
    //标题
    private String title;
    //摘要
    private String summary;
    //内容
    private String content;
    //html
    private String html;
    //分类id
    private Long category;
    //标签id集合
    private List<Long> tag;
    //是否允许评论
    private String isComment;
    //是否发布
    private String status;
    //是否置顶
    private String isTop;
    //缩略图
    private String thumbnail;

}
