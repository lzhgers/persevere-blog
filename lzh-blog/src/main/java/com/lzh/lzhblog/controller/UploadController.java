package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author luzhiheng
 */
@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

//    @PostMapping("/upload")
//    public ResponseResult uploadQiniuyun(MultipartFile img) {
//        try {
//            return uploadService.uploadImg(img);
//        } catch (Exception e) {
//            throw new RuntimeException(AppHttpCodeEnum.FILE_UPLOAD_ERROR.getMsg());
//        }
//    }

    @PostMapping("/upload")
    public ResponseResult uploadMinio(MultipartFile img) {
        try {
            return uploadService.uploadImgToMinio(img);
        } catch (Exception e) {
            throw new RuntimeException(AppHttpCodeEnum.FILE_UPLOAD_ERROR.getMsg());
        }
    }

}
