package com.lzh.lzhblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket getDocket() {
        //创建封⾯信息对象
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("《PERSEVERE-BLOG前台管理系统》后端接⼝说明")
                .description("此⽂档详细说明了 PERSEVERE-BLOG 后端接⼝规范....")
                .version("v 1.0.0")
                .contact(new Contact("LZH", "https://gitee.com/lu-zhihen/persevere-blog", "1203464373@qq.com"));
        ApiInfo apiInfo = apiInfoBuilder.build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo) //指定⽣成的⽂档中的封⾯信息：⽂档标题、版本、作者
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lzh.lzhblog.controller"))
                .paths(PathSelectors.any())
                .build();

    }
}