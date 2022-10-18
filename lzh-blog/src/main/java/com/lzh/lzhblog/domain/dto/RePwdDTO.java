package com.lzh.lzhblog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RePwdDTO {

    private String newPassword;
    private String conPassword;
    private String email;
    private String code;
}
