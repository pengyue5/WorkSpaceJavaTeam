package com.bbjh.admin.api.admin.sys;

import com.bbjh.admin.api.common.CommonRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoleRightTreeRequest extends CommonRequest {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 平台
     */
    @NotNull
    @Min(1)
    private Byte platform;
}
