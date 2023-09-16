package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.CarouselImg;
import com.lzh.lzhframework.form.AddOrEditCarouselImgForm;
import com.lzh.lzhframework.form.QueryCarouselImgForm;

import java.util.List;


/**
 * 文档管理-轮播图(CarouselImg)表服务接口
 *
 * @author makejava
 * @since 2022-10-17 12:13:09
 */
public interface SysCarouselImgService {

    /**
     * 分页查询轮播图
     *
     * @param queryCarouselImgForm
     * @return
     */
    ResponseResult pageList(QueryCarouselImgForm queryCarouselImgForm);

    /**
     * 根据id删除轮播图
     * @param id
     * @return
     */
    ResponseResult deleteById(Long id);

    /**
     * 置顶轮播图
     * @param id
     * @param sort
     * @return
     */
    ResponseResult topCarouselImg(Long id, Integer sort);

    /**
     * 添加或修改轮播图
     * @param addOrEditCarouselImgForm
     * @return
     */
    ResponseResult addOrEditCarouselImg(AddOrEditCarouselImgForm addOrEditCarouselImgForm);

    /**
     * 根据id批量删除轮播图
     * @param ids
     * @return
     */
    ResponseResult deleteBatch(List<Long> ids);

}

