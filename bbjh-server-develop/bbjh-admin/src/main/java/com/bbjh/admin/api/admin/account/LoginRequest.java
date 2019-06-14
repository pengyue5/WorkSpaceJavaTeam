/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.admin.api.admin.account;

import com.bbjh.admin.api.common.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequest extends CommonRequest {

    /**
     * 登录名
     */
//    @NotNull
//    @NotBlank
    private String loginName;


    /**
     * 手机号
     */
    @NotNull
    @NotBlank
    @ApiModelProperty(value = "手机号", required = true, example = "18888888888")
    private String phone;

    /**
     * 验证码
     */
    @NotNull
    @NotBlank
    @ApiModelProperty(value = "验证码", required = true, example = "1234")
    private String verificationCode;

    /**
     * 密码
     */
    @NotNull
    @NotBlank
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

}
