package com.lzh.lzhblog.annotation;

import java.lang.annotation.*;

/**
 * @author luzhiheng
 * @date 2023-11-23
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface LogAnnotation {

    /**
     * 日志内容
     *
     * @return
     */
    String message();

    /**
     * 日志类型
     *
     * @return
     */
    String operation();
}
