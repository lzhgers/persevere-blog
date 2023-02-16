package com.lzh.lzhframework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {

    //主键@TableId
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;

    //账号状态（0正常 1停用）
    private String status;

    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //备注
    private String remark;

    //是否互相关注 0 是 1 不是
    private int isFocusEach;
}
