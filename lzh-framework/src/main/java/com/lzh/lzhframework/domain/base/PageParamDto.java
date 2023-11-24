package com.lzh.lzhframework.domain.base;

import lombok.Data;

/**
 * 基础类 - 分页查询参数
 *
 * @Author luzhiheng
 * @Date 2023/11/24 10:43
 */
@Data
public class PageParamDto {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
