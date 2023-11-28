package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.QueryLogDto;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.ExceptionLogService;
import com.lzh.lzhframework.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 日志模块接口
 *
 * @Author luzhiheng
 * @Date 2023/11/24 10:24
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @Resource
    private ExceptionLogService exceptionLogService;

    @PostMapping("/queryUserLogPage")
    public ResponseResult queryUserLog(@RequestBody QueryLogDto queryLogDto) {
        PageVo pageVo = logService.queryUserLogPage(queryLogDto);
        return ResponseResult.success(pageVo);
    }

    @PostMapping("/queryOperateLogPage")
    public ResponseResult queryOperateLogPage(@RequestBody QueryLogDto queryLogDto) {
        PageVo pageVo = logService.queryOperateLogPage(queryLogDto);
        return ResponseResult.success(pageVo);
    }

    @PostMapping("/queryAbnormalLogPage")
    public ResponseResult queryAbnormalLogPage(@RequestBody QueryLogDto queryLogDto) {
        PageVo pageVo = logService.queryAbnormalLogPage(queryLogDto);
        return ResponseResult.success(pageVo);
    }

    @GetMapping("/queryAbnormalLogDetail/{logId}")
    public ResponseResult queryAbnormalLogDetail(@PathVariable Long logId) {
        return ResponseResult.success(exceptionLogService.getById(logId));
    }
}
