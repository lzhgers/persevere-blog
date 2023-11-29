package com.lzh.lzhframework.service;

import java.util.List;
import java.util.Map;

/**
 * @Author luzhiheng
 * @Date 2023/11/28 16:50
 */
public interface DashboardService {

    /**
     * 获取当日IP数
     * @return
     */
    Long queryTodayIpNum();

    /**
     * 通过博客分类获取博客数目
     * @return
     */
    List<Map<String, Object>> queryBlogNumByCategory();

    /**
     * 通过博客标签获取博客数目
     * @return
     */
    List<Map<String, Object>> queryBlogNumByTag();
}
