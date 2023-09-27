package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author LZH
 * @date 2023/2/17
 */
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/upload")
public class SysUploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/mulPic")
    public ResponseResult uploadMulPic(@RequestParam("file") MultipartFile[] files) {
        return uploadService.uploadMulImg(files);
    }

    @PostMapping("/singlePic")
    public ResponseResult uploadSinglePic(@RequestParam("img") MultipartFile file) throws Exception {
        return uploadService.uploadImgToMinio(file);
    }

    @PostMapping("/singleMd")
    public ResponseResult uploadSingleMd(MultipartFile mdFile, String imgUrlMap) {
        return uploadService.uploadSingleMdFile(mdFile, imgUrlMap);
    }

    /**
     * 头像上传
     * @param file 文件
     * @return
     */
    @PostMapping("/uploadAvatar")
    public ResponseResult avatar(@RequestParam("avatarfile") MultipartFile file) {
        return uploadService.uploadUserAvatar(file);
    }

}
