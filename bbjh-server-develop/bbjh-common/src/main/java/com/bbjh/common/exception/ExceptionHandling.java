package com.bbjh.common.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler
    public ResponseMessage handler(Throwable e) {
        //处理异常
        log.error(e.getMessage(), e);
        Integer code = ResultCode.SYSTEM_BUSY.getCode();
        String msg = ResultCode.SYSTEM_BUSY.getMsg();
        ResponseMessage<Object> response = new ResponseMessage<>();
        response.setSuccess(false)
                .setCode(code)
                .setMsg(msg);
        return response;
    }

    @ExceptionHandler
    public ResponseMessage handler(ScException e) {
        //处理异常
        ResponseMessage<Object> response = new ResponseMessage<>();
        response.setSuccess(false)
                .setCode(e.getCode())
                .setMsg(e.getMsg());
        log.warn(e.getResult().getMsg(Locale.SIMPLIFIED_CHINESE));
        return response;
    }

    @ExceptionHandler
    public ResponseMessage handler(MethodArgumentNotValidException e) {
        //处理异常
        ResponseMessage<Object> response = new ResponseMessage<>();
        response.setSuccess(false)
                .setCode(ResultCode.PARAMETER_CHECK_FAIL.getCode());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        JSONArray jsonArray = new JSONArray();
        String msg = ResultCode.PARAMETER_CHECK_FAIL.getMsg();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                String field = ((FieldError) error).getField();
                String defaultMessage = error.getDefaultMessage();
                jsonArray.fluentAdd(new JSONObject().fluentPut(field, defaultMessage));
                msg = jsonArray.toJSONString();
            } else if (error instanceof ObjectError) {
                msg = error.getDefaultMessage();
            }
        }
        log.warn("参数校验异常", msg);
        response.setMsg(msg);
        return response;
    }

    @ExceptionHandler
    public ResponseMessage handler(NoHandlerFoundException e) {
        //处理异常
        log.warn(e.getMessage(), e);
        Integer code = ResultCode.NOT_FOUND.getCode();
        String msg = ResultCode.NOT_FOUND.getMsg();
        ResponseMessage<Object> response = new ResponseMessage<>();
        response.setSuccess(false)
                .setCode(code)
                .setMsg(msg);
        return response;
    }

    @ExceptionHandler
    public ResponseMessage handler(HttpRequestMethodNotSupportedException e) {
        //处理异常
        log.warn(e.getMessage(), e);
        Integer code = ResultCode.METHOD_NOT_SUPPORTED.getCode();
        String msg = ResultCode.METHOD_NOT_SUPPORTED.getMsg();
        ResponseMessage<Object> response = new ResponseMessage<>();
        response.setSuccess(false)
                .setCode(code)
                .setMsg(msg);
        return response;
    }

}
