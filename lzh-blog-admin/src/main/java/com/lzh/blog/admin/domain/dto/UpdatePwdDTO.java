package com.lzh.blog.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePwdDTO {

    private Long userId;
    private String curPassword;
    private String newPassword;
    private String conPassword;
    private String email;
}
