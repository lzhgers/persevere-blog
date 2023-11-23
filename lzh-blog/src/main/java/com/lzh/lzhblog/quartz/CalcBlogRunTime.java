package com.lzh.lzhblog.quartz;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhframework.domain.entity.SysDisposition;
import com.lzh.lzhframework.service.SysDispositionService;
import com.lzh.lzhframework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static com.lzh.lzhframework.constants.SysConstants.WEBSITE_RUNTIME;

/**
 * 计算网站运行时间-定时任务
 *
 * @author luzhiheng
 */
@Slf4j
@Component
public class CalcBlogRunTime {

    @Resource
    private SysDispositionService sysDispositionService;

    @Resource
    private RedisCache redisCache;

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateBlogRunningInfo() {
        log.info("----定时任务：网站运行时间计算----");
        LambdaQueryWrapper<SysDisposition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDisposition::getSetting, WEBSITE_RUNTIME);
        SysDisposition sysDisposition = sysDispositionService.getOne(queryWrapper);
        if (sysDisposition != null) {
            long runtime = Long.parseLong(sysDisposition.getSetValue());
            sysDisposition.setSetValue(String.valueOf(runtime + 1));
            sysDisposition.setUpdateTime(new Date());
            sysDispositionService.updateById(sysDisposition);
            // 缓存
            redisCache.setCacheObject(WEBSITE_RUNTIME, sysDisposition.getSetValue());
        } else {
            sysDisposition = new SysDisposition();
            sysDisposition.setSetting(WEBSITE_RUNTIME);
            sysDisposition.setSetValue("0");
            sysDisposition.setCreateTime(new Date());
            sysDispositionService.save(sysDisposition);
        }
    }
}