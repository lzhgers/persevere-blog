package com.lzh.generate.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.lzh.generate.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "lzh_feedback")
public class FeedbackEntity extends BaseEntity {

    @Column(name = "title", type = MySqlTypeConstant.VARCHAR, comment = "标题")
    private String title;

    @Column(name = "status", type = MySqlTypeConstant.INT, comment = "反馈状态")
    private Integer status;

    @Column(name = "feedbackContent", type = MySqlTypeConstant.VARCHAR, comment = "反馈内容")
    private String feedbackContent  ;

    @Column(name = "replyContent", type = MySqlTypeConstant.VARCHAR, comment = "回复内容")
    private String replyContent;
}