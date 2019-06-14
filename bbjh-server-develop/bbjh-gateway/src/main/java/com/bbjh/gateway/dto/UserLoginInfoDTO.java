package com.bbjh.gateway.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginInfoDTO implements Serializable {

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 用户idCard
     */
    @ApiModelProperty(hidden = true)
    private String idCard;

    /**
     * phone
     */
    @ApiModelProperty(hidden = true)
    private String phone;

}
