package com.lzh.lzhframework.service;


import com.lzh.lzhframework.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseResult uploadImgToQiniuyun(MultipartFile img);

    ResponseResult uploadImgToMinio(MultipartFile img) throws Exception;

    ResponseResult uploadMulImg(MultipartFile[] pictures);

    ResponseResult uploadSingleMdFile(MultipartFile mdFile, String imgUrlMap);

    /**
     * 上传用户头像
     * @param file
     * @return
     */
    ResponseResult uploadUserAvatar(MultipartFile file);
}
