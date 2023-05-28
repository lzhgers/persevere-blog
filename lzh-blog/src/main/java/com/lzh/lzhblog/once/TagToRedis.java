package com.lzh.lzhblog.once;

import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.entity.Tag;
import com.lzh.lzhframework.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LZH
 * @date 2023/5/28
 */
@Slf4j
@Component
public class TagToRedis {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TagService tagService;

    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void cacheTagToRedis() {
        log.info("缓存标签到redis----------");
        List<Tag> tags = tagService.listAll();
        redisTemplate.opsForValue().set(SysConstants.TAG_CACHE_REDIS, JSON.toJSONString(tags));
    }
}
