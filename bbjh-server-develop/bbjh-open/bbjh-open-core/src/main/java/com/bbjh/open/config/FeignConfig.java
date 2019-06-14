package com.bbjh.open.config;

import cn.hutool.core.date.DatePattern;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/4/29
 */
@Configuration
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Decoder feignDecoder() {
        messageConverters.getObject().getConverters().stream().forEach(
                httpMessageConverter -> {
                    if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                        //对jackson 设置指定日期格式
                        ((MappingJackson2HttpMessageConverter) httpMessageConverter).getObjectMapper().setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
                    }
                });
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

}
