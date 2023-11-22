package com.lzh.lzhframework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.CarouselImgMapper;
import com.lzh.lzhframework.dao.SysDispositionMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.CarouselImgConfigDTO;
import com.lzh.lzhframework.domain.entity.CarouselImg;
import com.lzh.lzhframework.domain.entity.SysDisposition;
import com.lzh.lzhframework.service.CarouselImgService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.lzh.lzhframework.constants.SysConstants.CAROUSEL_IMG_NUM;
import static com.lzh.lzhframework.constants.SysConstants.CAROUSEL_IMG_ORDER;

/**
 * 文档管理-轮播图(CarouselImg)表服务实现类
 *
 * @author makejava
 * @since 2022-10-17 12:13:10
 */
@Service("carouselImgService")
public class CarouselImgServiceImpl extends ServiceImpl<CarouselImgMapper, CarouselImg> implements CarouselImgService {

    @Resource
    private SysDispositionMapper sysDispositionMapper;

    @Override
    public ResponseResult listAll() {
        LambdaQueryWrapper<CarouselImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(CarouselImg::getSort);
        List<CarouselImg> carouselImgs = super.list(queryWrapper);
        return ResponseResult.success(carouselImgs);
    }

    @Override
    public Integer getMaxSortCarouselImg() {
        Integer sort = baseMapper.selectMaxSortCarouselImg();
        return sort;
    }

    @Override
    public ResponseResult listFromDisposition() {
        SysDisposition sysDisposition = sysDispositionMapper.selectBySetting(CAROUSEL_IMG_NUM);
        int carouselImgCount;
        try {
            carouselImgCount = Integer.parseInt(sysDisposition.getSetValue());
        } catch (NumberFormatException e) {
            carouselImgCount = 5;
        }
        sysDisposition = sysDispositionMapper.selectBySetting(CAROUSEL_IMG_ORDER);

        String carouselImgOrder = "desc";
        if (sysDisposition != null && StringUtils.hasText(sysDisposition.getSetValue())) {
            carouselImgOrder = sysDisposition.getSetValue();
        }

        CarouselImgConfigDTO carouselImgConfigDTO = new CarouselImgConfigDTO().setCount(carouselImgCount).setOrder(carouselImgOrder);
        List<CarouselImg> carouselImgList = baseMapper.selectCarouselImgByConfig(carouselImgConfigDTO);
        return ResponseResult.success(carouselImgList);
    }


}

