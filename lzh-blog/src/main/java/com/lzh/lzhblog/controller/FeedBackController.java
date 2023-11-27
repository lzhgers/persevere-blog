package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.annotation.LogAnnotation;
import com.lzh.lzhframework.constants.LogType;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.feedback.AddFeedbackDto;
import com.lzh.lzhframework.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author luzhiheng
 * @Date 2023/11/27 11:27
 */
@RequestMapping("/feedback")
@RestController
public class FeedBackController {

    @Resource
    private FeedbackService feedbackService;

    @LogAnnotation(message = "用户反馈", operation = LogType.ADD)
    @PostMapping("/addFeedback")
    public ResponseResult addFeedback(AddFeedbackDto dto) {
        feedbackService.addFeedback(dto);
        return ResponseResult.success();
    }
}
