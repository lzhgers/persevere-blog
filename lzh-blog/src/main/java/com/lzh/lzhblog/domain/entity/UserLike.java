package com.lzh.lzhblog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户点赞表(UserLike)表实体类
 *
 * @author makejava
 * @since 2022-10-09 18:25:18
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_user_like")
@Builder
@Accessors(chain = true)
public class UserLike {
    //id@TableId
    private Long id;

    //用户id
    private Long userId;
    //被点赞的id
    private Long likedId;
    //点赞状态，0未点赞，1已点赞
    private Integer likedStatus;
    //点赞的类型
    private Integer likedType;
    // TODO 点赞时间
    @TableField(fill = FieldFill.INSERT)
    private Date likedTime;
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

