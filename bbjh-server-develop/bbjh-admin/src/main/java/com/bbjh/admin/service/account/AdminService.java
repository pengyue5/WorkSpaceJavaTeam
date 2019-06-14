package com.bbjh.admin.service.account;

import com.bbjh.common.service.BaseService;
import com.bbjh.admin.api.admin.sys.AdminListRequest;
import com.bbjh.common.model.Admin;

import java.util.List;

public interface AdminService extends BaseService<Admin, Integer> {

    /**
     * 运营后台-账号列表
     * @param request
     * @return
     */
    List<Admin> adminList(AdminListRequest request);
}
