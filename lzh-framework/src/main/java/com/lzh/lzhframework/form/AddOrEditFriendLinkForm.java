package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LZH
 * @date 2023/2/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrEditFriendLinkForm {
    private Long id;
    private String avatar;
    private String name;
    private String remark;
    private String url;
    private String status;
    private Integer listorder;
}
