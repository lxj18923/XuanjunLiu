package com.qigetech.mark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by liuxuanjun on 2019/2/23
 * Project : article
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 添加摘要信息(Docket)
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("com.iktbl"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("课题部落-API文档")
                // 文档描述
                .description("文章模块API文档")
                .termsOfServiceUrl("https://www.qigetech.com")
                .version("v1")
                .build();
    }

}
