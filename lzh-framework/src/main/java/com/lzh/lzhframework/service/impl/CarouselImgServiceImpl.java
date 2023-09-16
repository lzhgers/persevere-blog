package com.lzh.lzhframework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.CarouselImgMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.CarouselImg;
import com.lzh.lzhframework.service.CarouselImgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文档管理-轮播图(CarouselImg)表服务实现类
 *
 * @author makejava
 * @since 2022-10-17 12:13:10
 */
@Service("carouselImgService")
public class CarouselImgServiceImpl extends ServiceImpl<CarouselImgMapper, CarouselImg> implements CarouselImgService {

    @Override
    public ResponseResult listAll() {
        LambdaQueryWrapper<CarouselImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(CarouselImg::getSort);
        List<CarouselImg> carouselImgs = super.list(queryWrapper);
        return ResponseResult.okResult(carouselImgs);
    }

    @Override
    public Integer getMaxSortCarouselImg() {
        Integer sort = baseMapper.selectMaxSortCarouselImg();
        return sort;
    }

}

