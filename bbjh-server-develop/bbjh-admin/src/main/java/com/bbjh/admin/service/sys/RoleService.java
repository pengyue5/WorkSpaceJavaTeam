package com.bbjh.admin.service.sys;

import com.bbjh.common.service.BaseService;
import com.bbjh.admin.api.admin.sys.RoleAddRequest;
import com.bbjh.admin.api.admin.sys.RoleModifyRequest;
import com.bbjh.common.model.Role;

public interface RoleService extends BaseService<Role, Integer> {

    /**
     * 运营后台-添加角色
     * @param request
     * @return
     */
    Integer addRole(RoleAddRequest request);

    /**
     * 运营后台-修改角色
     * @param request
     * @return
     */
    Integer modifyRole(RoleModifyRequest request);
}
