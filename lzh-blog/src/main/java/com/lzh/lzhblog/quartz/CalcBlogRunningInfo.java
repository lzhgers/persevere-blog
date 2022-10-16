package com.lzh.lzhblog.quartz;

import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.domain.entity.BlogInfo;
import com.lzh.lzhblog.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Component
public class CalcBlogRunningInfo {

    @Autowired
    private RedisCache redisCache;

    @Scheduled(cron = "0 0 12 * * ?")
    public void updateBlogRunningInfo() {
        log.info("执行定时任务-----------------------" + new Date());
        String runningTime = redisCache.getCacheObject(SysConstants.BLOG_RUN_TIME);
        if (!StringUtils.hasText(runningTime)) {
            redisCache.setCacheObject(SysConstants.BLOG_RUN_TIME, 0);
        }
        long l = Long.parseLong(runningTime);
        redisCache.setCacheObject(SysConstants.BLOG_RUN_TIME, ++l);
    }
}
