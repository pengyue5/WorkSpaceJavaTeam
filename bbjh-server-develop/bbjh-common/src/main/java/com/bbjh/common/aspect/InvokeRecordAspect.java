package com.bbjh.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于打印方法执行前参数及执行后的结果
 */
@Aspect
@Component
@Slf4j
public class InvokeRecordAspect {

    /**
     *
     * 申明一个切点 里面是 execution表达式
     */
    @Pointcut("execution(public * com.bbjh.*.controller..*.*(..))")
    private void controllerAspect() {
    }

    @ResponseBody
    @Around("controllerAspect()")
    public Object doRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object result;
        log.info("<------请求内容------");
        // 打印请求内容
        log.info("请求地址:" + request.getRequestURL().toString());
        log.info("请求方式:" + request.getMethod());
        log.info("请求类方法:" + joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        Stream<Object> stream = Arrays.stream(args);
        List<Object> logArgs = stream
                .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());

        log.info("请求类方法参数:" + JSONObject.toJSONString(logArgs, new SimplePropertyPreFilter() {
            @Override
            public boolean apply(JSONSerializer serializer, Object object, String name) {
                return !StrUtil.containsAnyIgnoreCase(name, "rights");
            }
        }));
        log.info("<------请求内容------");

        result = joinPoint.proceed(args);

        log.info("------返回内容------>");
        log.info("Response内容:" + JSONObject.toJSONString(result));
        log.info("------返回内容------>");
        return result;
    }

}
