package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.dto.feedback.AddFeedbackDto;
import com.lzh.lzhframework.domain.entity.FeedbackEntity;


/**
 * (FeedbackEntity)表服务接口
 *
 * @author luzhiheng
 * @since 2023-11-27 11:47:41
 */
public interface FeedbackService extends IService<FeedbackEntity> {

    /**
     * 用户反馈
     * @param dto 反馈参数
     */
    void addFeedback(AddFeedbackDto dto);
}

