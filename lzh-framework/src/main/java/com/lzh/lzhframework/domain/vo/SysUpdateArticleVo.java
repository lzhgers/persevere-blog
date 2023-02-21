package com.lzh.lzhframework.domain.vo;

import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author LZH
 * @date 2023/2/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUpdateArticleVo {
    //id
    private Long id;
    //标题
    private String title;
    //摘要
    private String summary;
    //内容
    private String content;
    //html
    private String html;
    // 分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //状态（0已发布，1草稿）
    private String status;

    private String isTop;

    private String isComment;

    private List<Long> tagIds;
}
