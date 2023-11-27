package com.lzh.lzhframework.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.lzh.lzhframework.domain.base.BaseEntity;
import com.lzh.lzhframework.domain.dto.feedback.AddFeedbackDto;
import com.lzh.lzhframework.utils.SecurityUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "lzh_feedback")
public class FeedbackEntity extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 反馈状态
     */
    private Integer status;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 回复内容
     */
    private String replyContent;

    public FeedbackEntity(AddFeedbackDto dto) {
        this.title = dto.getTitle();
        this.feedbackContent = dto.getFeedbackContent();
        this.status = dto.getStatus();
        this.setCreateTime(new Date());
        this.setCreateName(String.valueOf(SecurityUtils.getUserId()));
    }
}