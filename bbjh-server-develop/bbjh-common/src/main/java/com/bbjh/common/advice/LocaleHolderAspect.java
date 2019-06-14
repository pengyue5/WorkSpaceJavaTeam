package com.bbjh.common.advice;

import cn.hutool.core.lang.Validator;
import com.bbjh.common.api.Header;
import com.bbjh.common.api.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * @author fangwenbo
 * @Description 获取header中的locale并放入RequestContextHolder中
 * @date 2019/4/13
 */
@Slf4j
@RestControllerAdvice
public class LocaleHolderAspect extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String locale = "";
        Header header = ((RequestMessage) body).getHeader();
        if (header != null) {
            locale = ((RequestMessage) body).getHeader().getLocale();
            if (Validator.isNotEmpty(locale)) {
                LocaleContextHolder.setLocale(Locale.forLanguageTag(locale));
            }
        }
        return body;
    }
}
