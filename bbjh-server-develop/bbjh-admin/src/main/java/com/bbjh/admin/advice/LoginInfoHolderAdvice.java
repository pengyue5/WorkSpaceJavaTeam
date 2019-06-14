package com.bbjh.admin.advice;

import cn.hutool.core.collection.CollUtil;


import com.bbjh.admin.api.common.CommonRequest;
import com.bbjh.admin.dto.AdminDTO;
import com.bbjh.common.utils.BeanFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author fwb
 * @Description 用于对登录信息设置到对应Header的对象中
 * @date 2019/4/11
 */
@Slf4j
@RestControllerAdvice(basePackages = {
        "com.gndc.admin.controller",
})
public class LoginInfoHolderAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        RedisTemplate<String, Serializable> redisTemplate = (RedisTemplate<String, Serializable>) BeanFactoryUtil.getBean("redisTemplate");
        HttpHeaders headers = inputMessage.getHeaders();
        boolean hasLogin = CollUtil.isNotEmpty(headers.get("loginType")) && CollUtil.isNotEmpty(headers.get("sessionId"));
        if (hasLogin) {
            String loginType = headers.get("loginType").stream().findFirst().get();
            String sessionId = headers.get("sessionId").stream().findFirst().get();
            Serializable obj = redisTemplate.opsForValue().get(sessionId);
            switch (loginType) {
                case "admin" :
                    ((CommonRequest) body).setAoAdmin((AdminDTO) obj);
                    break;
                default:
                    log.warn("未知的用户类型");
                    break;
            }
        }
        return body;
    }
}
