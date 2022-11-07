package com.lzh.common.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserStatus)表实体类
 *
 * @author makejava
 * @since 2022-10-19 15:06:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_user_status")
public class UserStatus {
    @TableId
    private Integer id;

    //粉丝数
    private Long fanCount;
    //总浏览量
    private Long viewCount;
    //关注数
    private Long followCount;
    //创作文章数
    private Long articleCount;
    //收藏数
    private Long collectCount;
    //积分数
    private Long pointCount;
    //点赞数
    private Long likeCount;
    //逻辑删除
    private Integer delFlag;



}

