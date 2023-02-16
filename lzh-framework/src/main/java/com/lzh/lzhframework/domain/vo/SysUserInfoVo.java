package com.lzh.lzhframework.domain.vo;

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
public class SysUserInfoVo {
    private String userName;
    private String nickName;
    private String avatar;
    private String sex;
    private String address;
    private String remark;
    private String phonenumber;

}
