package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.CarouselImg;


/**
 * 文档管理-轮播图(CarouselImg)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-17 12:13:04
 */
public interface CarouselImgMapper extends BaseMapper<CarouselImg> {

    /**
     * 查找最大排序数
     * @return
     */
    Integer selectMaxSortCarouselImg();
}

