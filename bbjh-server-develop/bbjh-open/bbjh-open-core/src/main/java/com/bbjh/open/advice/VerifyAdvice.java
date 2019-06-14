package com.bbjh.open.advice;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author fwb
 * @Description 用于对接收到的三方消息进行验签
 * @date 2019/4/11
 */
@Slf4j
@RestControllerAdvice(basePackages = {
        "com.bbjh.open.controller.open",
})
public class VerifyAdvice extends RequestBodyAdviceAdapter {


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(body), Feature.OrderedField);

        String appId = params.getString("appId");
        if (ObjectUtil.isNull(parameter)) {
            log.warn("非法的AppId:{}", appId);
            throw new ScException(ResultCode.CHECK_SIGN_FAIL);
        }
        JSONObject header = params.getJSONObject("header");
        String signStr = header.getString("sign");

        //移除sign
        header.remove("sign");
        String joinStr = MapUtil.join(MapUtil.sort(params), "&", "=", true);
        //对接收到的sign进行base64解码
        byte[] signDecode = Base64.decode(signStr, Charset.forName("UTF-8"));

        //存放数据
        return body;
    }
}
