package com.bbjh.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/5/5
 */
@MapperScan(basePackages = "com.bbjh.common.mapper")
@SpringBootApplication(scanBasePackages = {
        "com.bbjh.app", "com.bbjh.common.config",
        "com.bbjh.common.utils", "com.bbjh.common.exception",
        "com.bbjh.common.advice", "com.bbjh.common.aspect",
        "com.bbjh.common.interceptor",
        "com.bbjh.third"})
@EnableDiscoveryClient
@EnableFeignClients
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
