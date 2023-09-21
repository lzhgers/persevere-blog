package com.lzh.lzhframework.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author luzhiheng
 * @Date 2023/9/18 14:35
 */
@Data
public class AvatarUserVo {
    //主键@TableId
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;
    //邮箱
    private String email;
    //地址
    private String address;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //备注
    private String remark;
    //创建时间
    private Date createTime;

    private String role;
}
