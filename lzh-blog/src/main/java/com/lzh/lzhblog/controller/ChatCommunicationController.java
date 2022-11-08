package com.lzh.lzhblog.controller;

import com.lzh.common.domain.ResponseResult;
import com.lzh.common.domain.entity.ChatCommunication;
import com.lzh.lzhblog.service.ChatCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author LZH
 * @date 2022/11/8
 */
@RestController
@RequestMapping("/chatCommunication")
public class ChatCommunicationController {

    @Autowired
    private ChatCommunicationService chatCommunicationService;

    @GetMapping("/listAll/{fromUserName}/{toUserName}")
    public ResponseResult listCommunicationsByUserId(@PathVariable String fromUserName,
                                                     @PathVariable String toUserName) {
        return chatCommunicationService.listCommunicationsByUserId(fromUserName,toUserName);
    }

}
