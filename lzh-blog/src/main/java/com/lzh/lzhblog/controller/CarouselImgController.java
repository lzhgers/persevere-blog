package com.lzh.lzhblog.controller;

import com.lzh.common.domain.ResponseResult;
import com.lzh.lzhblog.service.CarouselImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carouselImg")
public class CarouselImgController {

    @Autowired
    private CarouselImgService carouselImgService;

    @GetMapping("/listAll")
    public ResponseResult listAll() {
        return carouselImgService.listAll();
    }

}
