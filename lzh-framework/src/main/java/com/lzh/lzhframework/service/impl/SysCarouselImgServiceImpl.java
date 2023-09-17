package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.dao.SysDispositionMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.CarouselImg;
import com.lzh.lzhframework.domain.entity.Category;
import com.lzh.lzhframework.domain.entity.FriendLink;
import com.lzh.lzhframework.domain.entity.SysDisposition;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.AddOrEditCarouselImgForm;
import com.lzh.lzhframework.form.ConfigCarouselImgForm;
import com.lzh.lzhframework.form.QueryCarouselImgForm;
import com.lzh.lzhframework.service.CarouselImgService;
import com.lzh.lzhframework.service.SysCarouselImgService;
import com.lzh.lzhframework.service.SysDispositionService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.UnderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lzh.lzhframework.constants.SysConstants.CAROUSEL_IMG_NUM;
import static com.lzh.lzhframework.constants.SysConstants.CAROUSEL_IMG_ORDER;

/**
 * @Author luzhiheng
 * @Date 2023/9/15 17:12
 */
@Service
public class SysCarouselImgServiceImpl implements SysCarouselImgService {

    @Resource
    private CarouselImgService carouselImgService;

    @Resource
    private SysDispositionMapper sysDispositionMapper;
    @Override
    public ResponseResult pageList(QueryCarouselImgForm form) {
        String title = form.getTitle();
        List<Map<String, String>> sortArr = form.getSortArr();

        Page<CarouselImg> page = new Page<>(form.getPageNum(), form.getPageSize());

        QueryWrapper<CarouselImg> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.like(StringUtils.hasText(title), "title", title);
        categoryQueryWrapper.eq(form.getStatus() != null, "status", form.getStatus());

        for (Map<String, String> map : sortArr) {
            String order = map.get("order");
            String prop = map.get("prop");
            prop = UnderUtil.camel2under(prop);
            if ("ascending".equals(order)) {
                categoryQueryWrapper.orderByAsc(prop);
            } else if ("descending".equals(order)) {
                categoryQueryWrapper.orderByDesc(prop);
            }
        }

        carouselImgService.page(page, categoryQueryWrapper);

        PageVo pageVo = new PageVo(page.getTotal(), page.getRecords());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteById(Long id) {
        carouselImgService.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult topCarouselImg(Long id, Integer sort) {
        Integer maxListOrder = carouselImgService.getMaxSortCarouselImg();
        if (maxListOrder.equals(sort)) {
            throw new SystemException(AppHttpCodeEnum.FRIEND_LINK_IS_TOP);
        }
        CarouselImg carouselImg = new CarouselImg();
        carouselImg.setId(id);
        carouselImg.setSort(maxListOrder + 1);
        carouselImgService.updateById(carouselImg);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addOrEditCarouselImg(AddOrEditCarouselImgForm addOrEditCarouselImgForm) {
        Long id = addOrEditCarouselImgForm.getId();

        CarouselImg carouselImg = BeanCopyUtils.copyBean(addOrEditCarouselImgForm, CarouselImg.class);
        if (Objects.isNull(id)) {
            //添加
            carouselImgService.save(carouselImg);
        } else {
            //编辑
            carouselImgService.updateById(carouselImg);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteBatch(List<Long> ids) {
        carouselImgService.removeBatchByIds(ids);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult configCarouselImg(ConfigCarouselImgForm configCarouselImgForm) {
        if (configCarouselImgForm.getCount() == null || configCarouselImgForm.getCount() <= 0) {
            throw new SystemException(AppHttpCodeEnum.CAROUSEL_IMG_COUNT_ERROR);
        }
        if (!StringUtils.hasText(configCarouselImgForm.getOrder())) {
            throw new SystemException(AppHttpCodeEnum.ORDER_NOT_NULL);
        }
        SysDisposition sysDisposition = sysDispositionMapper.selectBySetting(CAROUSEL_IMG_NUM);
        sysDisposition.setSetValue(String.valueOf(configCarouselImgForm.getCount()));
        sysDispositionMapper.updateById(sysDisposition);

        sysDisposition = sysDispositionMapper.selectBySetting(CAROUSEL_IMG_ORDER);
        sysDisposition.setSetValue(configCarouselImgForm.getOrder());
        sysDispositionMapper.updateById(sysDisposition);

        return ResponseResult.okResult();
    }

}