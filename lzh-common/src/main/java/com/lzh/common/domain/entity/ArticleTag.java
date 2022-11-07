package com.lzh.common.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2022-10-12 09:00:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_article_tag")
@Accessors(chain = true)
public class ArticleTag {

    //文章id@TableId
    private Long articleId;
    //标签id@TableId
    private Long tagId;
}

