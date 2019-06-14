package com.bbjh.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author
 * @date 2018/11/9
 */
@MapperScan(basePackages = "com.bbjh.admin.mapper")
@SpringBootApplication(scanBasePackages = {
        "com.bbjh.admin", "com.bbjh.common.config",
        "com.bbjh.common.utils", "com.bbjh.common.exception",
        "com.bbjh.common.advice", "com.bbjh.common.aspect",
        "com.bbjh.third"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bbjh"})
public class AdminApplication {


    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
