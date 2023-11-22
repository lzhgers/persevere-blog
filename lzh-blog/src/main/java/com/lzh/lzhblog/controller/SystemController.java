package com.lzh.lzhblog.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author luzhiheng
 * @Date 2023/11/6 17:51
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/testMq")
    public String sendMsg() {
        Gson gson = new Gson();
        rabbitTemplate.convertAndSend("testExchange10", "test.user", "hello world");
        return "success";
    }
}
