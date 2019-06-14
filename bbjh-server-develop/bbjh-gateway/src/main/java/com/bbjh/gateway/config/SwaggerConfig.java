package com.bbjh.gateway.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/5/22
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo());
    }

    private ApiInfo groupApiInfo(){
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui zuul")
                .description("<div style='font-size:14px;color:red;'>宝贝计划API接口</div>")
                .version("1.0.0")
                .build();
    }
}
