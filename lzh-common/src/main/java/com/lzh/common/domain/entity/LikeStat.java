package com.lzh.common.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 点赞统计表(LikeStat)表实体类
 *
 * @author makejava
 * @since 2022-10-09 18:23:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_like_stat")
@Accessors(chain = true)
public class LikeStat {
    //id@TableId
    private Long id;

    //被点赞id
    private Long likedId;
    //点赞总数量
    private Long likedCount;
    //是否逻辑删除
    @TableLogic
    private Integer delFlag;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;



}

