package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.common.domain.ResponseResult;
import com.lzh.common.domain.entity.ChatCommunication;

import java.util.List;


/**
 * 聊天记录(ChatCommunication)表服务接口
 *
 * @author makejava
 * @since 2022-11-08 14:07:18
 */
public interface ChatCommunicationService extends IService<ChatCommunication> {

    ResponseResult listCommunicationsByUserId(String fromUserName, String toUserName);
}

