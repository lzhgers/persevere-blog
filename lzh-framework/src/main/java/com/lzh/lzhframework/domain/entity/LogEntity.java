package com.lzh.lzhframework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lzh.lzhframework.domain.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "lzh_log")
public class LogEntity extends BaseEntity {

    private String operatePerson;

    private String requestInterface;

    private String requestWay;

    private String interfaceName;

    private String ip;

    private String ipSource;

    private String platform;

    private String browser;

    private Long requestTime;

    private String content;

    private String userBehavior;
}