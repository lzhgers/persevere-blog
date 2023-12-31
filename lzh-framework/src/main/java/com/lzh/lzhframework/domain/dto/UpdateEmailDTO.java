package com.lzh.lzhframework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmailDTO {

    private Long userId;
    private String curPassword;
    private String email;
    private String code;
}
