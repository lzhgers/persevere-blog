package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.QueryLogDto;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 日志模块接口
 * @Author luzhiheng
 * @Date 2023/11/24 10:24
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @PostMapping("/queryUserLogPage")
    public ResponseResult queryUserLog(@RequestBody QueryLogDto queryLogDto) {
        PageVo pageVo = logService.queryUserLog(queryLogDto);
        return ResponseResult.success(pageVo);
    }
}
