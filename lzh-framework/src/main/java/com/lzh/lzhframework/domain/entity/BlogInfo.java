package com.lzh.lzhframework.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * @author luzhiheng
 * @date 2023-11-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BlogInfo {

    private String articleCount;
    private String tagCount;
    private String categoryCount;
    private String commentCount;
    private String viewCount;
    private Long onlinePerson;
    private String runningTime;
}
