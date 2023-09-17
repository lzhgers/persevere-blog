package com.lzh.lzhframework.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.SysDisposition;
import org.apache.ibatis.annotations.Param;

/**
* @author 12034
* @description 针对表【sys_disposition】的数据库操作Mapper
* @createDate 2023-09-16 19:34:32
* @Entity generator.domain.SysDisposition
*/
public interface SysDispositionMapper extends BaseMapper<SysDisposition> {

    /**
     * 根据系统配置key查询系统配置
     * @param setting
     * @return
     */
    SysDisposition selectBySetting(@Param("setting") String setting);
}




