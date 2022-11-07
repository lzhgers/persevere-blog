package com.lzh.lzhblog.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzh.common.domain.ResponseResult;
import com.lzh.common.domain.enums.AppHttpCodeEnum;
import com.lzh.lzhblog.service.UploadService;
import com.lzh.common.utils.PathUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Data
@ConfigurationProperties(prefix = "oss")
@Service
public class UploadServiceImpl implements UploadService {

    private String secretKey;

    private String accessKey;

    private String bucket;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {

        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new RuntimeException(AppHttpCodeEnum.FILE_TYPE_ERROR.getMsg());
        }

        String key = PathUtil.generateImgOssPath(originalFilename);
        String url = this.uploadOss(key, img);

        return ResponseResult.okResult(url);
    }

    private String uploadOss(String key, MultipartFile img) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        UploadManager uploadManager = new UploadManager(cfg);

        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                return "http://rkafz00mn.hd-bkt.clouddn.com/" + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                System.err.println(r.bodyString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }
}
