package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author LZH
 * @date 2023/2/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryCategoryForm {
    private String name;
    private Integer pageNum;
    private Integer pageSize;
    private List<Map<String, String>> sortArr;
}
