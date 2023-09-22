package com.lzh.lzhframework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtil {

    public static String generateImgOssPath(String img) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String date = sdf.format(new Date());

        StringBuilder stringBuilder = new StringBuilder();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        int index = img.lastIndexOf(".");
        String imgType = index == -1 ? img : img.substring(index);

        return stringBuilder.append(date).append(uuid).append(imgType).toString();
    }
}
