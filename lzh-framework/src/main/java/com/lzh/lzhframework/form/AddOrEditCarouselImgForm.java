package com.lzh.lzhframework.form;

import lombok.Data;

/**
 * @Author luzhiheng
 * @Date 2023/9/16 15:18
 */
@Data
public class AddOrEditCarouselImgForm {
    private Long id;
    private String img;
    private String title;
    private String remarks;
    private String url;
    private Integer status;
    private Integer sort;
}
