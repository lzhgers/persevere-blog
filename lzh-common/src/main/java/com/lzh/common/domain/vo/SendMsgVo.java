package com.lzh.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LZH
 * @date 2022/11/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMsgVo {

    private String from;
    private String text;
}
