package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.vo.dashboard.QueryStatisticsNumVo;
import com.lzh.lzhframework.service.ArticleService;
import com.lzh.lzhframework.service.CommentService;
import com.lzh.lzhframework.service.DashboardService;
import com.lzh.lzhframework.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 首页面展示信息控制器
 *
 * @Author luzhiheng
 * @Date 2023/11/28 16:48
 */
@RequestMapping("/dashboard")
@RestController
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleService articleService;

    @GetMapping("/queryStatisticsNum")
    public ResponseResult queryStatisticsNum() {
        QueryStatisticsNumVo vo = new QueryStatisticsNumVo();
        long ipNum = dashboardService.queryTodayIpNum();
        long userNum = userService.count();
        long commentNum = commentService.count();
        long articleCount = articleService.count();

        vo.setIpNum(ipNum);
        vo.setUserNum(userNum);
        vo.setCommentNum(commentNum);
        vo.setArticleNum(articleCount);
        return ResponseResult.success(vo);
    }
}
