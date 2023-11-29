package com.lzh.lzhframework.domain.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author luzhiheng
 * @Date 2023/11/28 17:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryStatisticsNumVo {

    /**
     * IP数
     */
    private Long ipNum;

    /**
     * 用户数
     */
    private Long userNum;

    /**
     * 评论数
     */
    private Long commentNum;

    /**
     * 文章数
     */
    private Long articleNum;
}
