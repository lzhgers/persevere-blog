package com.lzh.generate.entity.base;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author luzhiheng
 * @date 2023-11-23
 */
@Data
public class BaseEntity implements Serializable {

    @Column(name = "id", type = MySqlTypeConstant.BIGINT, isKey = true, isAutoIncrement = true)
    private Long id;

    @Column(name = "create_time", type = MySqlTypeConstant.DATETIME)
    private Date createTime;

    @Column(name = "update_time", type = MySqlTypeConstant.DATETIME)
    private Date updateTime;

    @Column(name = "create_name", type = MySqlTypeConstant.VARCHAR)
    private String createName;

    @Column(name = "update_name", type = MySqlTypeConstant.VARCHAR)
    private String updateName;

    @Column(name = "del_flag", type = MySqlTypeConstant.INT)
    private Integer delFlag = 0;
}
