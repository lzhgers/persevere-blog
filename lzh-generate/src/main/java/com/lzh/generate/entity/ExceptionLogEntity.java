package com.lzh.generate.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.lzh.generate.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志异常记录表
 * </p>
 *
 * @author luzhiheng
 * @since 2023-11-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "lzh_exception_log")
public class ExceptionLogEntity extends BaseEntity {

    private static final long serialVersionUID = -4851055162892178225L;

    /**
     * 操作IP
     */
    @Column(name = "ip", type = MySqlTypeConstant.VARCHAR, comment = "操作IP")
    private String ip;

    /**
     * ip来源
     */
    @Column(name = "ip_source", type = MySqlTypeConstant.VARCHAR, comment = "IP来源")
    private String ipSource;

    /**
     * 请求方法
     */
    @Column(name = "method", type = MySqlTypeConstant.VARCHAR, comment = "请求方法")
    private String method;

    /**
     * 参数
     */
    @Column(name = "params", type = MySqlTypeConstant.VARCHAR, comment = "参数")
    private String params;

    /**
     * 异常对象json格式
     */
    @Column(name = "exception_json", type = MySqlTypeConstant.VARCHAR, comment = "异常对象json格式")
    private String exceptionJson;

    /**
     * 异常简单信息,等同于e.getMessage
     */
    @Column(name = "exception_message", type = MySqlTypeConstant.VARCHAR, comment = "异常简单信息,等同于e.getMessage")
    private String exceptionMessage;
}
