package com.lzh.lzhframework.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lzh.lzhframework.config.MinIOConfig;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.service.SysArticleService;
import com.lzh.lzhframework.service.UploadService;
import com.lzh.lzhframework.utils.*;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@Data
@ConfigurationProperties(prefix = "oss")
@Service
public class UploadServiceImpl implements UploadService {

    private String secretKey;

    private String accessKey;

    private String bucket;

    @Resource
    private MinIOConfig minIOConfig;

    @Resource
    private ThreadPoolTaskExecutor executor;

    @Resource
    private SysArticleService sysArticleService;

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
//        if (!imgName.endsWith(".png") && !imgName.endsWith(".jpg")) {
//            throw new RuntimeException(AppHttpCodeEnum.FILE_TYPE_ERROR.getMsg());
//        }

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

    /**
     * 多文件上传
     *
     * @param pictures
     * @return
     */
    @Override
    public ResponseResult uploadMulImg(MultipartFile[] pictures) {
        Map<String, String> imgMap = new ConcurrentHashMap<>(16);
        List<Future<String>> futureList = new ArrayList<>();

        for (MultipartFile picture : pictures) {
            Future<String> submit = executor.submit(() -> {
                String originalFilename = picture.getOriginalFilename();
                assert originalFilename != null;
                String preImgName = originalFilename.substring(0, originalFilename.lastIndexOf("."));

                long currentTimeMillis = System.currentTimeMillis();
                String imgName = SysConstants.IMG_PREFIX + preImgName + currentTimeMillis
                        + originalFilename.substring(originalFilename.lastIndexOf("."));

                MinIOUtils.uploadFile(
                        minIOConfig.getBucketName(),
                        picture,
                        imgName,
                        "image/png"
                );

                String imgUrl = minIOConfig.getFileHost()
                        + "/"
                        + minIOConfig.getBucketName()
                        + "/"
                        + imgName;
                imgUrl = imgUrl + "&" + originalFilename;
                return imgUrl;
            });
            futureList.add(submit);
        }
        for (Future<String> future : futureList) {
            try {
                String res = future.get();
                String[] split = res.split("&");
                imgMap.put(split[1], split[0]);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

        return ResponseResult.okResult(imgMap);
    }

    @Override
    public ResponseResult uploadSingleMdFile(MultipartFile mdFile, String imgUrlMap) {
        if (Objects.isNull(mdFile)) {
            throw new SystemException(AppHttpCodeEnum.FILE_NOT_NULL);
        }
        Map<String, String> map = JSON.parseObject(imgUrlMap, Map.class);
        if (Objects.isNull(map)) {
            map = new ConcurrentHashMap<>();
        }

        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = mdFile.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int len = -1;
            char[] chars = new char[1024];
            while ((len = br.read(chars)) != -1) {
                sb.append(chars, 0, len);
            }
            String markdown = sb.toString();

            //获取所有img标签
            List<String> imgTags = MarkdownUtils.getAllImgTagFromMarkdown(markdown);
            for (String imgTag : imgTags) {

                List<String> match = RegexUtils.match(imgTag, "\\((.*?)\\)");
                if (Objects.isNull(match) || match.size() == 0) {
                    continue;
                }
                String path = match.get(0);
                String localPath = path.substring(1, path.length() - 1);

                //不是网络图片
                if (!localPath.startsWith(SysConstants.HTTP)) {
                    //替换本地图片地址
                    String[] splitPath = null;
                    if (localPath.contains("\\")) {
                        splitPath = localPath.split("\\\\");
                    } else if (localPath.contains("/")) {
                        splitPath = localPath.split("/");
                    }
                    if (splitPath == null || splitPath.length < 2) {
                        continue;
                    }
                    String simpleImgName = splitPath[splitPath.length - 1];
                    String onlineUrl = map.get(simpleImgName);
                    if (StringUtils.hasText(onlineUrl)) {
                        markdown = markdown.replace(localPath, onlineUrl);
                    }
                }
            }
            //保存数据库
            String mdName = Objects.requireNonNull(mdFile.getOriginalFilename())
                    .substring(0, mdFile.getOriginalFilename().indexOf("."));

            //md转为html
            String html = MarkdownUtils.markdownToHtml(markdown);
            sysArticleService.saveUploadArticle(markdown, html, mdName);

            return ResponseResult.okResult(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.okResult();
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
