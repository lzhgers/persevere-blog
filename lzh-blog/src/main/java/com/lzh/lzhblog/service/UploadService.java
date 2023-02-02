package com.lzh.lzhblog.service;

import com.lzh.common.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    ResponseResult uploadImg(MultipartFile img);

    ResponseResult uploadImgToMinio(MultipartFile img) throws Exception;
}
