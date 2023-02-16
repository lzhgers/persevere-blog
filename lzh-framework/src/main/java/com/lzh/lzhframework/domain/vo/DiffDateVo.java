package com.lzh.lzhframework.domain.vo;

import com.lzh.lzhframework.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiffDateVo {

    //2022年11月
    private String yearMonth;

    private List<Article> articleList;
}
