package com.lzh.lzhframework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lzh.lzhframework.domain.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志异常记录表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lzh_exception_log")
public class ExceptionLogEntity extends BaseEntity {

    private static final long serialVersionUID = -4851055162892178225L;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 异常对象json格式
     */
    private String exceptionJson;

    /**
     * 异常简单信息,等同于e.getMessage
     */
    private String exceptionMessage;
}
