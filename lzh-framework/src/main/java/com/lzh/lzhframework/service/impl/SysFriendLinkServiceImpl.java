package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.FriendLink;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.AddOrEditFriendLinkForm;
import com.lzh.lzhframework.form.QueryFriendLinkForm;
import com.lzh.lzhframework.service.FriendLinkService;
import com.lzh.lzhframework.service.SysFriendLinkService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.UnderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LZH
 * @date 2023/2/26
 */
@Service
public class SysFriendLinkServiceImpl implements SysFriendLinkService {

    @Resource
    private FriendLinkService friendLinkService;

    @Override
    public ResponseResult pageList(QueryFriendLinkForm form) {

        Page<FriendLink> page = new Page<>(form.getPageNum(), form.getPageSize());
        QueryWrapper<FriendLink> queryWrapper = new QueryWrapper<>();

        //友链名
        String name = form.getName();
        queryWrapper.like(StringUtils.hasText(name), "name", name);
        //友链状态
        String status = form.getStatus();
        queryWrapper.eq(StringUtils.hasText(status), "status", status);

        //排序
        List<Map<String, String>> sortArr = form.getSortArr();
        for (Map<String, String> map : sortArr) {
            String prop = map.get("prop");
            String order = map.get("order");
            prop = UnderUtil.camel2under(prop);
            if ("ascending".equals(order)) {
                queryWrapper.orderByAsc(prop);
            } else if ("descending".equals(order)) {
                queryWrapper.orderByDesc(prop);
            }
        }

        friendLinkService.page(page, queryWrapper);

        List<FriendLink> friendLinkList = page.getRecords();
        friendLinkList = friendLinkList.stream()
                .peek(friendLink -> {
                    String linkStatus = friendLink.getStatus();
                    friendLink.setStatus(getFriendLinkStatus(linkStatus));
                }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(page.getTotal(), friendLinkList);
        return ResponseResult.success(pageVo);
    }

    @Override
    public ResponseResult deleteById(Long id) {
        friendLinkService.removeById(id);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult topFriendLink(Long id, Integer listorder) {
        Integer maxListOrder = friendLinkService.getMaxSortFriendLink();
        if (maxListOrder.equals(listorder)) {
            throw new SystemException(AppHttpCodeEnum.FRIEND_LINK_IS_TOP);
        }
        FriendLink friendLink = new FriendLink()
                .setId(id)
                .setListorder(maxListOrder + 1);
        friendLinkService.updateById(friendLink);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult addOrEdit(AddOrEditFriendLinkForm form) {
        Long id = form.getId();

        FriendLink friendLink = BeanCopyUtils.copyBean(form, FriendLink.class);
        if (Objects.isNull(id)) {
            //添加
            friendLinkService.save(friendLink);
        } else {
            //编辑
            friendLinkService.updateById(friendLink);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteBatch(List<Long> ids) {
        friendLinkService.removeBatchByIds(ids);
        return ResponseResult.success();
    }

    private String getFriendLinkStatus(String linkStatus) {
        String res = null;
        switch (linkStatus) {
            case "0":
                res = "下架";
                break;
            case "1":
                res = "上架";
                break;
            case "2":
                res = "申请";
                break;
            default:
                res = "";
        }
        return res;
    }
}
