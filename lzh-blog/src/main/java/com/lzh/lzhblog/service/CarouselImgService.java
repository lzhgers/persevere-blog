package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.entity.CarouselImg;


/**
 * 文档管理-轮播图(CarouselImg)表服务接口
 *
 * @author makejava
 * @since 2022-10-17 12:13:09
 */
public interface CarouselImgService extends IService<CarouselImg> {

    ResponseResult listAll();
}

