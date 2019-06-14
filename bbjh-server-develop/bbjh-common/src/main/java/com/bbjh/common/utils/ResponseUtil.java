package com.bbjh.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

@Slf4j
public class ResponseUtil {

    public static void sendError(HttpServletResponse response, int sc, String message) {
        try {
            response.sendError(sc, message);
        } catch (IOException e) {
            log.error("发送错误失败", e);
        }
    }

    public static void sendError(HttpServletResponse response) {
        try {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.setCharacterEncoding(Charset.forName("UTF-8").name());
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            PrintWriter writer = response.getWriter();
            ResponseMessage<Object> responseMessage = new ResponseMessage<>();
            responseMessage.setSuccess(false)
                    .setCode(ResultCode.SYSTEM_BUSY.getCode())
                    .setMsg(ResultCode.SYSTEM_BUSY.getMsg());
            writer.print(JSONObject.toJSON(responseMessage));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("发送错误失败", e);
        }
    }

    /**
     * 快速设置允许跨域的头信息
     * @param response
     */
    public static void allCors(HttpServletRequest request, HttpServletResponse response) {

        //        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//
//        response.setHeader("Access-Control-Allow-Headers", "content-type, power-by");
//
//        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
//
//        response.setHeader("Access-Control-Max-Age", "1800");
//
//        response.setHeader("Vary", "Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.setCharacterEncoding(Charset.forName("UTF-8").name());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
        response.setHeader("Access-Control-Max-Age", "1800");
        response.setHeader("Vary", "Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
    }
}
