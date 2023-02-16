package com.lzh.lzhblog.quartz;


import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class CalcBlogRunningInfo {

    @Resource
    private RedisCache redisCache;

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateBlogRunningInfo() {
        log.info("执行定时任务-----------------------" + new Date());
        String runningTime = redisCache.getCacheObject(SysConstants.BLOG_RUN_TIME);
        if (!StringUtils.hasText(runningTime)) {
            redisCache.setCacheObject(SysConstants.BLOG_RUN_TIME, "0");
        }
        long l = Long.parseLong(runningTime);
        redisCache.setCacheObject(SysConstants.BLOG_RUN_TIME, ++l);
    }
}
