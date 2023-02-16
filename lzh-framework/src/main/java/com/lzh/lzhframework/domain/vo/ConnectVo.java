package com.lzh.lzhframework.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author LZH
 * @date 2022/11/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectVo {

    private List<Map<String, Object>> users;
}
