/**************************************************************************
 * Copyright (c) 2013-2023  浙江盘石信息技术股份有限公司
 * All rights reserved.
 *
 * 项目名称： 互金网络平台
 * 版权说明：本软件属浙江盘石信息技术股份有限公司所有，在未获浙江盘石信息技术股份有限公司正式授权情况下，
 *          任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知识产权保护的内容。   
 ***************************************************************************/
package com.bbjh.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author fangwenbo
 * @version V1.0.1
 * @Description 常量定义
 * @date 2018年1月25日 上午9:12:46
 */
@Configuration
@PropertySource(value = "classpath:/constants.properties")
public class Constant {

    // 融云结果码 200表示正常
    public static final int RONG_ERROR_CODE_OK = 200;
    // 请求融云发生IO异常
    public static final int RONG_ERROR_CODE_IOEXCEPTION = -404;
    // 请求融云发生异常
    public static final int RONG_ERROR_CODE_EXCEPTION = -500;
    // 国家
    public static String COUNTRY = "CN";// VN
    // 国家编码
    public static String COUNTRY_CODE = "62";
    // 与中国时差
    public static int TIME_DIFF = -1;
    // 十分钟内短信输入错误次数限制
    public static int SMS_FAIL_COUNT_LIMIT = 5;
    // 十分钟内发送短信条数限制
    public static int SMS_COUNT_LIMIT_TEN_MINTUE = 3;
    // 24小时内发送短信条数限制
    public static int SMS_COUNT_LIMIT_24_HOUR = 10;
    // 姓名最大长度
    public static int MAX_NAME_LENGTH = 50;
    // 电话号码最大长度
    public static int MAX_PHONE_LENGTH = 15;
    // 亲属关系最大值
    public static int MAX_IS_RELATION_NUM = 6;
    // 默认关系
    public static int DEFAULT_RELATION = 9;
    // 关系-本人
    public static String RELATION_USERSELF = "Userself";
    // 关系值-本人
    public static byte RELATION_USERSELF_VALUE = 99;
    // 关系-公司
    public static String RELATION_COMPANY = "Company";
    // 关系值-公司
    public static byte RELATION_COMPANY_VALUE = 98;
    // 数据字典分隔符
    public static String DICT_KEY_SEPARATE = "###";
    // 越南NganLuong还款超时时间 单位分钟
    public static int NGANLUONG_REPAY_OVERTIME = 1440;

    @Value("${COUNTRY}")
    public void setCountry(String country) {
        Constant.COUNTRY = country;
    }

    @Value("${COUNTRY_CODE}")
    public void setCountryCode(String countryCode) {
        Constant.COUNTRY_CODE = countryCode;
    }

    @Value("${TIME_DIFF}")
    public void setTimeDiff(int timeDiff) {
        Constant.TIME_DIFF = timeDiff;
    }

    @Value("${SMS_FAIL_COUNT_LIMIT}")
    public void setSmsFailCountLimit(int smsFailCountLimit) {
        Constant.SMS_FAIL_COUNT_LIMIT = smsFailCountLimit;
    }

    @Value("${SMS_COUNT_LIMIT_TEN_MINTUE}")
    public void setSmsCountLimitTenMinute(int smsCountLimitTenMinute) {
        Constant.SMS_COUNT_LIMIT_TEN_MINTUE = smsCountLimitTenMinute;
    }

    @Value("${SMS_COUNT_LIMIT_24_HOUR}")
    public void setSmsCountLimit24Hour(int smsCountLimit24Hour) {
        Constant.SMS_COUNT_LIMIT_24_HOUR = smsCountLimit24Hour;
    }

    @Value("${MAX_NAME_LENGTH}")
    public void setMaxNameLength(int maxNameLength) {
        Constant.MAX_NAME_LENGTH = maxNameLength;
    }

    @Value("${MAX_PHONE_LENGTH}")
    public void setMaxPhoneLength(int maxPhoneLength) {
        Constant.MAX_PHONE_LENGTH = maxPhoneLength;
    }

    @Value("${MAX_IS_RELATION_NUM}")
    public void setMaxIsRelationNum(int maxIsRelationNum) {
        Constant.MAX_IS_RELATION_NUM = maxIsRelationNum;
    }

    @Value("${DEFAULT_RELATION}")
    public void setDefaultRelation(int defaultRelation) {
        Constant.DEFAULT_RELATION = defaultRelation;
    }

}
