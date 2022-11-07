package com.lzh.lzhblog.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FriendLink {

    @TableId
    private Long id;

    //友链名称
    private String name;
    //url链接
    private String url;
    //跳转方式，0_blank，1_self，2_parent，3_top，4framename
    private Integer target;
    //分组ID
    private Integer groupId;
    //排序
    private Integer listorder;

    private String avatar;

    //友链简介
    private String remark;

    //删除标志（0代表未删除，1代表已删除）
    @TableLogic
    private Integer delFlag;

}

