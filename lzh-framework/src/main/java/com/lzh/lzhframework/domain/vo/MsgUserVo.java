package com.lzh.lzhframework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LZH
 * @date 2022/11/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgUserVo {

    private String username;
    private Integer isRead;
}
