package com.lzh.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Collect)表实体类
 *
 * @author makejava
 * @since 2022-10-19 10:51:26
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_collect")
public class Collect {
    @TableId
    private Long id;

    //用户id
    private Long userId;
    //文章id
    private Long articleId;
    //收藏状态 0 未收藏 1 已收藏
    private Integer collectStatus;
    //收藏时间
    @TableField(fill = FieldFill.INSERT)
    private Date collectTime;
    //是否逻辑删除
    @TableLogic
    private Integer delFlag;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

