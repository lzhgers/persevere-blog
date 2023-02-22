package com.lzh.blog.admin.controller;

import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.service.UploadService;
import com.lzh.lzhframework.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

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
    public ResponseResult uploadSinglePic(@RequestParam("img") MultipartFile file) {
        try {
            return uploadService.uploadImgToMinio(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPLOAD_ERROR);
    }

    @PostMapping("/singleMd")
    public ResponseResult uploadSingleMd(MultipartFile mdFile, String imgUrlMap) {
        return uploadService.uploadSingleMdFile(mdFile, imgUrlMap);
    }
}
