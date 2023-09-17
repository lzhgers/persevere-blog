package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.CarouselImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzh
 */
@RestController
@RequestMapping("/carouselImg")
public class CarouselImgController {

    @Autowired
    private CarouselImgService carouselImgService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        return carouselImgService.listAll();
    }

    @GetMapping("/listByConfig")
    public ResponseResult listFromPosition() {
        return carouselImgService.listFromDisposition();
    }

}
