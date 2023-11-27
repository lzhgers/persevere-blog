package com.lzh.lzhframework.domain.dto;

import com.lzh.lzhframework.domain.base.PageParamDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author luzhiheng
 * @Date 2023/11/24 10:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryLogDto extends PageParamDto {

    /**
     * 用户行为
     */
    private String userBehavior;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 操作人
     */
    private String operatePerson;

    /**
     * 请求接口
     */
    private String requestInterface;

    /**
     * IP
     */
    private String ip;
}
