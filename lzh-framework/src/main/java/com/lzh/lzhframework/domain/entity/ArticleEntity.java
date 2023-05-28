package com.lzh.lzhframework.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author LZH
 * @date 2023/5/28
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("article")
public class ArticleEntity {

    @Id
    private Long id;

    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //html
    private String html;

    private Date createTime;

    private Long createBy;

    private Date updateTime;
}
