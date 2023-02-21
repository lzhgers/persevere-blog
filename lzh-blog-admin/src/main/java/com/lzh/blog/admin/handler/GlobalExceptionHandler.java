package com.lzh.blog.admin.handler;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.exception.SystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler(SystemException.class)
    public ResponseResult exceptionHandler(SystemException ex) {
        return ResponseResult.errorResult(ex.getCode(), ex.getMessage());
    }
}