package com.lzh.lzhblog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户关注关系表(Subscribe)表实体类
 *
 * @author makejava
 * @since 2022-10-23 12:54:07
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_subscribe")
@Accessors(chain = true)
public class Subscribe {
    @TableId
    private Long id;

    //被关注者id
    private Long beSubscribe;
    //关注者id
    private Long subscribe;

    private Integer delFlag;
    //最后变更时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //关注关系存续状态，0-存在关注关系，1-取消关注
    private Integer status;
}

