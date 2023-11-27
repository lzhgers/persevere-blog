package com.lzh.lzhframework.domain.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author luzhiheng
 * @Date 2023/11/27 11:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFeedbackDto {

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
}
