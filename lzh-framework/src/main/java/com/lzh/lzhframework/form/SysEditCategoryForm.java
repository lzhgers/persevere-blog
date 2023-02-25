package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LZH
 * @date 2023/2/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysEditCategoryForm {
    private Long id;
    private String name;
    private String description;
    private Long sort;
}
