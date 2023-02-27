package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author LZH
 * @date 2023/2/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryFriendLinkForm {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String status;
    private List<Map<String, String>> sortArr;
}
