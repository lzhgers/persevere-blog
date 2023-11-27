package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.ChatCommunicationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author LZH
 * @date 2022/11/8
 */
@RestController
@RequestMapping("/chatCommunication")
public class ChatCommunicationController {

    @Resource
    private ChatCommunicationService chatCommunicationService;

    @GetMapping("/listAll/{fromUserName}/{toUserName}")
    public ResponseResult listCommunicationsByUserId(@PathVariable String fromUserName,
                                                     @PathVariable String toUserName) {
        return chatCommunicationService.listCommunicationsByUserId(fromUserName, toUserName);
    }

    @PostMapping("/getMsg/{toUserName}")
    public ResponseResult getMsgByFromAndToName(@PathVariable String toUserName, @RequestBody List<String> users) {
        return chatCommunicationService.getMsgByFromAndToName(toUserName, users);
    }

    @PutMapping("/updateIsRead/{toUserName}/{fromUserName}")
    public ResponseResult updateIsRead(@PathVariable String fromUserName,
                                       @PathVariable String toUserName) {
        return chatCommunicationService.updateIsRead(fromUserName, toUserName);
    }
}
