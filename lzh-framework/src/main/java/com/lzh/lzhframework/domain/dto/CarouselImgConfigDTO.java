package com.lzh.lzhframework.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 轮播图查询条件配置
 * @Author luzhiheng
 * @Date 2023/9/17 10:51
 */
@Accessors(chain = true)
@Data
public class CarouselImgConfigDTO {
    private Integer count;
    private String order;
}
