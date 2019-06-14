package com.bbjh.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/4/17
 */
@EnableFeignClients
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.bbjh.gateway", "com.bbjh.common.config",
        "com.bbjh.common.utils", "com.bbjh.common.exception"
})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
