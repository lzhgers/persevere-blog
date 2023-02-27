package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.AddOrEditFriendLinkForm;
import com.lzh.lzhframework.form.QueryFriendLinkForm;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/26
 */
public interface SysFriendLinkService {
    ResponseResult pageList(QueryFriendLinkForm queryFriendLinkForm);

    ResponseResult deleteById(Long id);

    ResponseResult topFriendLink(Long id, Integer listorder);

    ResponseResult addOrEdit(AddOrEditFriendLinkForm addOrEditFriendLinkForm);

    ResponseResult deleteBatch(List<Long> ids);
}
