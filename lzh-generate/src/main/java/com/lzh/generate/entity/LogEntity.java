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
@Table(name = "lzh_log")
public class LogEntity extends BaseEntity {

    @Column(name = "operate_person", type = MySqlTypeConstant.VARCHAR, comment = "操作人")
    private String operatePerson;

    @Column(name = "request_interface", type = MySqlTypeConstant.VARCHAR, comment = "请求接口")
    private String requestInterface;

    @Column(name = "request_way", type = MySqlTypeConstant.VARCHAR, comment = "请求方式")
    private String requestWay;

    @Column(name = "interface_name", type = MySqlTypeConstant.VARCHAR, comment = "接口名")
    private String interfaceName;

    @Column(name = "ip", type = MySqlTypeConstant.VARCHAR, comment = "IP")
    private String ip;

    @Column(name = "ip_source", type = MySqlTypeConstant.VARCHAR, comment = "IP来源")
    private String ipSource;

    @Column(name = "platform", type = MySqlTypeConstant.VARCHAR, comment = "平台")
    private String platform;

    @Column(name = "browser", type = MySqlTypeConstant.VARCHAR, comment = "浏览器")
    private String browser;

    @Column(name = "request_time", type = MySqlTypeConstant.BIGINT, comment = "请求耗时")
    private Integer requestTime;

    @Column(name = "content", type = MySqlTypeConstant.VARCHAR, comment = "内容")
    private String content;

    @Column(name = "user_behavior", type = MySqlTypeConstant.VARCHAR, comment = "用户行为")
    private String userBehavior;
}