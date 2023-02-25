package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.QueryTagForm;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/23
 */
public interface SysTagService {
    ResponseResult pageList(QueryTagForm queryTagForm);

    ResponseResult addTag(String name, Long sort);

    ResponseResult deleteTag(Long id);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult editTag(Long id, String name, Long sort);
}
