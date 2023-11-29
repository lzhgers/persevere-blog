package com.lzh.lzhframework.service.impl;

import com.lzh.lzhframework.dao.ArticleMapper;
import com.lzh.lzhframework.dao.CategoryMapper;
import com.lzh.lzhframework.dao.TagMapper;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.service.CategoryService;
import com.lzh.lzhframework.service.DashboardService;
import com.lzh.lzhframework.service.ExceptionLogService;
import com.lzh.lzhframework.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author luzhiheng
 * @Date 2023/11/28 16:50
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private LogService logService;

    @Resource
    private ExceptionLogService exceptionLogService;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagMapper tagMapper;

    @Override
    public Long queryTodayIpNum() {
        String formatDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Set<String> logIpSet = logService.queryTodayIpNum(formatDate);
        Set<String> expLogIpSet = exceptionLogService.queryTodayIpNum(formatDate);
        Set<String> mergedSet = Stream.concat(logIpSet.stream(), expLogIpSet.stream()).collect(Collectors.toSet());
        return (long) mergedSet.size();
    }

    @Override
    public List<Map<String, Object>> queryBlogNumByCategory() {
        return articleMapper.queryArticleNumByCategory();
    }

    @Override
    public List<Map<String, Object>> queryBlogNumByTag() {
        return tagMapper.queryArticleNumByTag();
    }
}
