package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.enums.AppHttpCodeEnum;
import com.lzh.lzhblog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.spi.ResolveResult;
import java.io.IOException;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img) {
        try {
            return uploadService.uploadImg(img);
        } catch (Exception e) {
            throw new RuntimeException(AppHttpCodeEnum.FILE_UPLOAD_ERROR.getMsg());
        }
    }

}
