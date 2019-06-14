package com.bbjh.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author fwb
 * @date 2018/11/9
 */
@MapperScan(basePackages = "com.bbjh.common.mapper")
@SpringBootApplication(scanBasePackages = {
        "com.bbjh.open", "com.bbjh.common.config",
        "com.bbjh.common.utils", "com.bbjh.common.exception",
        "com.bbjh.common.aspect"})
@EnableDiscoveryClient
@EnableRetry
@EnableFeignClients(basePackages = "com.bbjh")
public class OpenApplication {


    public static void main(String[] args) {
        SpringApplication.run(OpenApplication.class, args);
    }

}
