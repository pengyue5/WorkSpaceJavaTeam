/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fangwenbo
 * @version V1.0.1
 * @Description 请求响应头封装
 * @date 2018年1月27日 上午10:19:23
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description = "用于指定所有请求的请求头信息")
public class Header implements Serializable {

    /**
     * 发送时间，格式：yyyy-MM-dd HH:mm:ss.SSS
     */
    @ApiModelProperty(value = "请求时间")
    private Date timestamp;


    /**
     * 地区
     */
    @NotBlank
    @ApiModelProperty(value = "地区", example = "zh-CN")
    private String locale;

    /**
     * 分页信息
     */
    @ApiModelProperty(value = "分页信息")
    private Page page;

}