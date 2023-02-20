package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryArticleForm {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String author;
    private Long tagId;
    private Long categoryId;
}
