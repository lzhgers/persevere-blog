package com.lzh.lzhframework.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 友情链接表(FriendLink)表实体类
 *
 * @author makejava
 * @since 2022-10-26 10:59:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_friend_link")
@Accessors(chain = true)
public class FriendLink {

    @TableId
    private Long id;

    //友链名称
    private String name;
    //url链接
    private String url;
    //跳转方式，0_blank，1_self，2_parent，3_top，4framename
    private Integer target;

    private String status;

    private Long clickNum;

    //分组ID
    private Integer groupId;
    //排序
    private Integer listorder;

    private String avatar;

    //友链简介
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    @TableLogic
    private Integer delFlag;

}

