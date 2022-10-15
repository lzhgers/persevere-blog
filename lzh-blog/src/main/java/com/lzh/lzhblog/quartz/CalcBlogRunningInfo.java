package com.lzh.lzhblog.quartz;

import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.domain.entity.BlogInfo;
import com.lzh.lzhblog.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class CalcBlogRunningInfo {

    @Autowired
    private RedisCache redisCache;

    private static Long runningTime = 0L;

    @Scheduled(cron = "0 0 10 * * ?")
    public void updateBlogRunningInfo() {
        log.info("执行定时任务-----------------------" + new Date());
        redisCache.setCacheObject(SysConstants.BLOG_RUN_TIME, ++runningTime);
    }
}
