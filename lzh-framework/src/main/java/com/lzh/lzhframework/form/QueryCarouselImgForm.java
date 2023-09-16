package com.lzh.lzhframework.form;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author luzhiheng
 * @Date 2023/9/15 17:11
 */
@Data
public class QueryCarouselImgForm {

    private String title;

    private String url;

    private String img;

    private Integer status;

    private String remarks;

    private Integer pageNum;

    private Integer pageSize;

    private List<Map<String, String>> sortArr;
}
