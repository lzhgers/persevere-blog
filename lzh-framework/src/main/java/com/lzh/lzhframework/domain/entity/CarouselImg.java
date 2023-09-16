package com.lzh.lzhframework.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档管理-轮播图(CarouselImg)表实体类
 *
 * @author makejava
 * @since 2022-10-17 12:13:06
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_carousel_img")
public class CarouselImg {
    @TableId
    private Long id;

    //创建者
    private String createBy;
    //更新者
    private String updateBy;
    //描述
    private String remarks;
    //逻辑删除标记（0：未删除；1：已删除）
    @TableLogic
    private Integer delFlag;
    //标题
    private String title;
    //链接地址
    private String url;
    //图片地址
    private String img;
    //排序
    private Integer sort;
    //排序
    private Integer status;
}

