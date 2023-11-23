package com.lzh.lzhframework.domain.entity;

import com.lzh.lzhframework.domain.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 10:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogEntity extends BaseEntity {

    private String accessPath;

}