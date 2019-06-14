/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.admin.api.admin.sys;

import com.bbjh.admin.api.common.CommonRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class RoleModifyRequest extends CommonRequest {

    @NotNull
    @Min(1)
    private Integer id;

    @NotNull
    @Min(1)
    private Byte platform;

    /**
     * 角色名
     */
    @NotNull
    @NotBlank
    private String roleName;

    /**
     * 权限Ids
     */
    @NotNull
    @NotEmpty
    private List<Integer> rightIds;

}
