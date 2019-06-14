package com.bbjh.admin.service.account;

import com.bbjh.common.model.Admin;

public interface AccountService {

    /**
     * 密码校验
     */
    Boolean passwordCheck(Admin admin, String password);

}
