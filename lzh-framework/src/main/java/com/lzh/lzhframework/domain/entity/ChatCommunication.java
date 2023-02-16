package com.lzh.lzhframework.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 聊天记录(ChatCommunication)表实体类
 *
 * @author makejava
 * @since 2022-11-08 14:07:17
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_chat_communication")
@Accessors(chain = true)
public class ChatCommunication {
    @TableId
    private Long id;

    //发送人id
    private Long fromId;
    //发送人name
    private String fromName;
    //接收人id，不适用与群消息
    private Integer toId;
    //接收人name不适用于群消息
    private String toName;
    //消息内容
    private String content;
    //时间
    private Date time;
    //群id
    private Integer groupId;
    //群名称
    private Integer groupName;
    //是否已读,不适用于群消息
    private Integer isRead;
    //消息类型：1是普通文本，2是图片，3是语音
    private Integer type;
    //消息类：1是用户聊天，2是群组聊天
    private String isUserGroup;

    private String fromAvatar;

    //逻辑删除 0 未删除 1 已删除
    @TableLogic
    private Integer delFlag;




}

