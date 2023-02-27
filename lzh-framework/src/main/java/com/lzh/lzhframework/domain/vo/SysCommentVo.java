package com.lzh.lzhframework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author LZH
 * @date 2023/2/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysCommentVo {
    private Long id;
    private String avatar;
    private String commenter;
    private String isCommented;
    private String type;
    private String content;
    private Date createTime;
}
