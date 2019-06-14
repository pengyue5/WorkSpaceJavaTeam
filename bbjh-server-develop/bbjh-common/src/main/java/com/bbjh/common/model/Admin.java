package com.bbjh.common.model;

import com.bbjh.common.mybatis.SnowflakeIdGenId;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "dc_admin")
public class Admin extends BaseEntity {
    @Id
    @KeySql(genId = SnowflakeIdGenId.class)
    private Long id;

    private String name;

    private String loginName;

    private String email;

    private String password;

    private String passwordSign;

    private String operateSign;

    private Byte isLock;

    private Date lockTime;

    private Integer loginCount;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Date lastLogoutTime;

    private String telephone;

    private String phone;

    private Byte platform;

    private Byte superAdmin;

    private Integer partnerId;

    private Integer roleId;

    private Byte status;

    private Long operatorId;

    @Transient
    private List<Right> rights;

}