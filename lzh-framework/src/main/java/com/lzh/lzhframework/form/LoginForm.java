package com.lzh.lzhframework.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    private String userName;
    private String password;
}
