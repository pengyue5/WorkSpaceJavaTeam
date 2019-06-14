package com.bbjh.common.enums;

import com.bbjh.common.utils.BeanFactoryUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 返回码枚举类
 * 说明
 * 0为成功
 * 响应码为1+5+2
 * 第一位为异常级别：1：系统级别；2：业务级别
 * 中间五位为业务编号如：00002为登录相关业务
 * 最后2两位为具体业务下具体的异常码
 * 组合起来如10000102位session过期异常
 */
public enum ResultCode {
    
    // 成功
    OK(0),
    // 1 系统返回码部分
    /**
     * 0001为通用业务
     * 用来表示未知错误
     */
    SYSTEM_BUSY(1000101),

    PARAMETER_CHECK_FAIL(1000102),

    /**
     * 用来表示 404
     */
    NOT_FOUND(1000103),

    /**
     * 用来表示权限没有在系统当中配置
     */
    RIGHT_NOT_INITIALISE(1000104),

    /**
     * 表示解密失败
     */
    DECRYPT_ERROR(1000105),

    /**
     * 发送http请求发生异常
     */
    HTTP_EXCEPTION(1000106),

    /**
     * 签名验证失败
     */
    CHECK_SIGN_FAIL(1000107),

    /**
     * 签名失败
     */
    SIGN_FAIL(1000108),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_SUPPORTED(1000109),

    /**
     * 0002为登录相关业务
     */
    NO_SESSION(1000201),

    SESSION_EXPIRED(1000202),

    INVALID_SESSION(1000203),

    NOT_ALLOW_LOGIN(1000204),

    ADMIN_NOT_EXIST(1000205),

    PASSWORD_ERROR(1000206),
    /**
     * 设备类型为空
     */
    DEVICE_TYPE_ISNULL(1000207),
    /**
     * 手机终端类型为空
     */
    TERM_TYPE_ISNULL(1000208),
    /**
     * 设备IMEI不能为空
     */
    IMEI_TOKEN_ISNULL(1000209),
    /**
     * 用户已锁定
     */
    USER_DISABLED(1000210),
    /**
     * 用户名或密码错误
     */
    USER_NAME_PASSWORD_ERROR(1000211),

    /**
     * 0003
     */
    NO_PERMISSION(1000301),

     // 2 业务返回码部分
    /**
     * 0001为通用业务
     */
    RECORD_NOT_EXIST(2000101),

    RECORD_SAVE_FAIL(2000102),

    RECORD_MODIFY_FAIL(2000103),

    RECORD_DELETE_FAIL(2000104),

    RECORD_EXIST(2000105),

    /**
     * 0002为账号相关
     */
    ADMIN_EXIST(2000201),
    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(2000202),
    /**
     * 用户已存在
     */
    USER_EXISTS(2000203),
    /**
     * 密码不一致
     */
    CONFIRM_PASSWORD_ERROR(2000204),
    /**
     * 原密码错误
     */
    OLD_PASSWORD_ERROR(2000205),

    /**
     * 00003为角色相关业务
     */
    ROLE_NOT_EXIST(2000301),

    ROLE_HAS_NORMAL_ADMIN(2000302),

    ROLENAME_EXISTS(2000303),
    /**
     * 0004为权限管理相关
     */
    RIGHT_IS_USING(2000401),

    RIGHT_HAS_CHILDREN(2000402),

    /**
     * 0005为产品相关
     */
    PRODUCT_ONLINE(2000501),

    PRODUCTS_NOT_ONLINE(2000502),

    /**
     * 0006为短信相关
     */
    SIGN_NOT_EXIST(2000601),
    TEMPLATE_NOT_EXIST(2000602),
    CONDITION_NOT_EXIST(2000603),
    SMS_ILLEGAL_DATE(2000604),
    /**
     * cron表达式非法
     */
    SMS_ILLEGAL_CRON(2000605),
    /**
     * 10分钟内只能发送3条短信
     */
    AUTH_COUNT_TEN_LIMIT(2000606),
    /**
     * 验证码失效，请重新获取
     */
    AUTH_INVALID(2000607),
    /**
     * 验证码错误
     */
    AUTH_ERROR(2000608),
    /**
     * 验证码输错次数过多
     */
    AUTH_FAIL_COUNT(2000609),
    /**
     * 24小时内只能发送10条同类短信
     */
    AUTH_COUNT_24_HOUR(2000610),
    ;
    /**
     * 返回码
     */
    private Integer code;

    ResultCode(Integer code) {
        this.code = code;
    }

    public static ResultCode fetch(Integer code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code.equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取本次请求中设置的locale对应的消息
     * @return
     */
    public String getMsg() {
        MessageSource messageSource = BeanFactoryUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(fetch(code).name(), null, LocaleContextHolder.getLocale());
        return message;
    }

    /**
     * 根据locale获取对应消息
     * @return
     */
    public String getMsg(Locale locale) {
        MessageSource messageSource = BeanFactoryUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(fetch(code).name(), null, locale);
        return message;
    }

}
