package com.lzh.lzhframework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    private Long id;
    private String userName;
    private String nickName;
    private String address;
    private String sex;
    private String remark;
}
