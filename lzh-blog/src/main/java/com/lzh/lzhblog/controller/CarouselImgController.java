package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.CarouselImgService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lzh
 */
@RestController
@RequestMapping("/carouselImg")
public class CarouselImgController {

    @Resource
    private CarouselImgService carouselImgService;

    /**
     *
     * @return
     */
    @GetMapping("/listAll")
    public ResponseResult listAll() {
        return carouselImgService.listAll();
    }

    @GetMapping("/listByConfig")
    public ResponseResult listFromPosition() {
        return carouselImgService.listFromDisposition();
    }

}
