package com.lzh.blog.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleIsCommentTopVo {

    private Long articleId;
    private String isTop;
    private String isComment;
}
