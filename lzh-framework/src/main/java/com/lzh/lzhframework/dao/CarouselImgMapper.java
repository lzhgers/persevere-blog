package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.dto.CarouselImgConfigDTO;
import com.lzh.lzhframework.domain.entity.CarouselImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 文档管理-轮播图(CarouselImg)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-17 12:13:04
 */
@Mapper
public interface CarouselImgMapper extends BaseMapper<CarouselImg> {

    /**
     * 查找最大排序数
     * @return
     */
    Integer selectMaxSortCarouselImg();

    /**
     * 根据轮播图配置查询
     * @param carouselImgConfigDTO
     * @return
     */
    List<CarouselImg> selectCarouselImgByConfig(CarouselImgConfigDTO carouselImgConfigDTO);
}

