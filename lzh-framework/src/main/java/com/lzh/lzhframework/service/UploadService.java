package com.lzh.lzhframework.service;


import com.lzh.lzhframework.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseResult uploadImg(MultipartFile img);

    ResponseResult uploadImgToMinio(MultipartFile img) throws Exception;
}
