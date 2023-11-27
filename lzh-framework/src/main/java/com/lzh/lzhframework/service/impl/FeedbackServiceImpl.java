package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.domain.dto.feedback.AddFeedbackDto;
import com.lzh.lzhframework.domain.entity.FeedbackEntity;
import com.lzh.lzhframework.dao.FeedbackMapper;
import com.lzh.lzhframework.service.FeedbackService;
import org.springframework.stereotype.Service;

/**
 * (Feedback)表服务实现类
 *
 * @author luzhiheng
 * @since 2023-11-27 11:47:41
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, FeedbackEntity> implements FeedbackService {

    @Override
    public void addFeedback(AddFeedbackDto dto) {
        FeedbackEntity feedbackEntity = new FeedbackEntity(dto);
        this.save(feedbackEntity);
    }
}

