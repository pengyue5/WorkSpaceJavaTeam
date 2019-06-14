package com.bbjh.admin.api.admin.sys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class AdminListResponse implements Serializable {

    private Integer id;

    private String name;

    private String loginName;

    private Date lastLoginTime;

    private String phone;

    private Integer roleId;

    private String roleName;

    private Integer createAdminId;

    private Byte platform;

    private Byte superAdmin;

    private Date createTime;

    private Integer partnerId;

    private String partnerName;

}
