/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.admin.api.admin.sys;

import com.bbjh.admin.api.common.CommonRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class RoleListRequest extends CommonRequest {
    /**
     * 平台
     */
    @Min(1)
    private Byte platform;
}
