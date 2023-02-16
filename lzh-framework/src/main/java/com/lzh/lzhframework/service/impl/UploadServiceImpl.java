package com.lzh.lzhframework.service.impl;

import com.google.gson.Gson;
import com.lzh.lzhframework.config.MinIOConfig;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.service.UploadService;
import com.lzh.lzhframework.utils.MinIOUtils;
import com.lzh.lzhframework.utils.PathUtil;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Data
@ConfigurationProperties(prefix = "oss")
@Service
public class UploadServiceImpl implements UploadService {

    private String secretKey;

    private String accessKey;

    private String bucket;

    @Resource
    private MinIOConfig minIOConfig;

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

    @Override
    public ResponseResult uploadImgToMinio(MultipartFile img) throws Exception {
        String imgName = img.getOriginalFilename();
        assert imgName != null;
        if (!imgName.endsWith(".png") && !imgName.endsWith(".jpg")) {
            throw new RuntimeException(AppHttpCodeEnum.FILE_TYPE_ERROR.getMsg());
        }

//        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
//                imgName,
//                img.getInputStream());

        long currentTimeMillis = System.currentTimeMillis();
        imgName = SysConstants.IMG_PREFIX + currentTimeMillis + imgName.substring(imgName.lastIndexOf("."));

        MinIOUtils.uploadFile(
                minIOConfig.getBucketName(),
                img,
                imgName,
                "image/png"
        );

        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + imgName;

        return ResponseResult.okResult(imgUrl);
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
