package com.bbjh.admin.api.admin.sys;

import com.bbjh.admin.api.common.CommonRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminListRequest extends CommonRequest {

    /**
     * 平台
     */
    private Byte platform;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;
}
