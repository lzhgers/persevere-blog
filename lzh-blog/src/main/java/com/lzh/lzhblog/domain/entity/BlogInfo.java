package com.lzh.lzhblog.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BlogInfo {

    private Long articleCount;
    private Long tagCount;
    private Long categoryCount;
    private Long commentCount;
    private Long viewCount;
    private Long onlinePerson;
    private String runningTime;
}
