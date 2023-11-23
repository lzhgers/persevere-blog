package com.lzh.lzhframework.domain.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author luzhiheng
 * @date 2023-11-23
 */
@Data
public class BaseEntity implements Serializable {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String createName;

    private String updateName;

    private Integer delFlag = 0;
}
