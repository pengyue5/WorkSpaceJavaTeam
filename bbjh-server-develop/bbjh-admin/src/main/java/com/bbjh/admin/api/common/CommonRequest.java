package com.bbjh.admin.api.common;


import com.bbjh.admin.dto.AdminDTO;
import com.bbjh.common.api.RequestMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 通用请求
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description = "用于后台管理的通用请求", parent = CommonRequest.class)
public class CommonRequest extends RequestMessage {

    @ApiModelProperty(hidden = true)
    protected AdminDTO aoAdmin;


    public AdminDTO getAoAdmin() {

        return aoAdmin;
    }
    public void setAoAdmin(AdminDTO aoAdmin) {

        this.aoAdmin = aoAdmin;
    }

}
