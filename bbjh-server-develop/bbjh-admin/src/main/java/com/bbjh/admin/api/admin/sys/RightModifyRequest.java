package com.bbjh.admin.api.admin.sys;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.admin.api.common.CommonRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RightModifyRequest extends CommonRequest {

    @NotNull
    @Min(1)
    private Integer id;

    /**
     * 权限名
     */
    @NotNull
    private JSONObject rightName;

    /**
     * 权限URL
     */
    @NotNull
    @NotBlank
    private String rightUrl;

    /**
     * 权限路径
     */
    private String rightPath;

    /**
     * 前端页面
     */
    private String component;

    /**
     * 授权唯一标识
     */
    private String uniqueSign;

    /**
     * 父权限id
     */
    @NotNull
    @NotBlank
    @Min(0)
    private String superId;

    /**
     * 平台
     */
    @NotNull
    @Min(1)
    private Byte platform;

    /**
     * 权限类型
     */
    @NotNull
    @Min(1)
    private Byte rightType;

    /**
     * 顺序
     */
    @Min(1)
    private String rightOrder;

    /**
     * 权限层级
     */
    @NotNull
    @Min(1)
    private Byte rightLevel;

    /**
     * 图标
     */
    private String rightPic;

    /**
     * 是否需要授权
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Byte requireAuth;

    /**
     *权限是否可见
     */
    private Byte rightVisible;

    /**
     * 备注
     */
    private String remark;
}
