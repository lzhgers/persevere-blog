package com.lzh.lzhframework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
/**
 * 标签(Tag)表实体类
 *
 * @author makejava
 * @since 2022-09-29 13:17:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lzh_tag")
@Accessors(chain = true)
public class Tag  {
    @TableId
    private Long id;

    //标签名
    private String name;
    
    private Long createBy;
    
    private Date createTime;
    
    private Long updateBy;
    
    private Date updateTime;
    //备注
    private String remark;

    //点击数
    private Long clickNum;

    //排序
    private Long sort;

    //删除标志（0代表未删除，1代表已删除）
    @TableLogic
    private Integer delFlag;
}

