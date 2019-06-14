package com.bbjh.admin.controller.admin.account;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bbjh.admin.api.admin.account.LoginRequest;
import com.bbjh.admin.api.admin.account.LoginResponse;

import com.bbjh.admin.dto.AdminDTO;
import com.bbjh.admin.enums.AdminSuperAdminEnum;
import com.bbjh.admin.mapping.AdminDTOMapping;
import com.bbjh.admin.service.account.AccountService;
import com.bbjh.admin.service.account.AdminService;
import com.bbjh.admin.service.sys.RightService;
import com.bbjh.admin.service.sys.RoleRightService;
import com.bbjh.admin.service.sys.RoleService;
import com.bbjh.admin.util.RightConvertUtil;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.enums.PlatformEnum;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.enums.StatusEnum;
import com.bbjh.common.exception.ScException;
import com.bbjh.common.model.Admin;
import com.bbjh.common.model.Right;
import com.bbjh.common.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/account")
@Api(value = "管理员登陆接口", tags = {"登陆"}, consumes = "application/json")
public class AccountController {
    @Autowired
    private AdminDTOMapping adminDTOMapping;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RightService rightService;

    @Autowired
    private RoleRightService roleRightService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test() {
        return "test";
    }


    /**
     * 管理员登录
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理员登陆", notes = "管理员登陆", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        ResponseMessage<LoginResponse> response = new ResponseMessage<>();
        LoginResponse loginResponse = new LoginResponse();
        //业务校验
        String loginName = request.getLoginName();
        String phone = request.getPhone();
        String password = request.getPassword();
        Admin admin = null;
        if (StringUtils.isNotBlank(loginName)) {
            admin = adminService.selectOneByProperty(Admin::getLoginName, loginName);
        }
        if (admin == null && StringUtils.isNotBlank(phone)) {
            admin = adminService.selectOneByProperty(Admin::getPhone, phone);
        }

        if (admin == null) {
            String msg = StrUtil.format("用户名 {}{} 不存在", loginName, phone);
            log.warn(msg);
            throw new ScException(ResultCode.ADMIN_NOT_EXIST);
        }

        if (admin.getStatus().equals(StatusEnum.DELETE.getCode())) {
            String msg = StrUtil.format("用户名 {}{} 已停用", loginName, phone);
            log.warn(msg);
            throw new ScException(ResultCode.ADMIN_NOT_EXIST, msg);
        }
        //密码校验
        if (!accountService.passwordCheck(admin, password)) {
            throw new ScException(ResultCode.PASSWORD_ERROR);
        }
        admin.setLastLoginTime(new Date());

        //更新登录信息
        adminService.updateByPrimaryKeySelective(admin);
        //分配session
        String sessionId = IdUtil.simpleUUID();
        //不是运营账号不允许登录
        if (!PlatformEnum.OPERATOR.getCode().equals(admin.getPlatform())) {
            log.warn("{} 不允许登录运营平台", admin.getPlatform());
            throw new ScException(ResultCode.NOT_ALLOW_LOGIN);
        }

        //获取权限树
        Byte superAdmin = admin.getSuperAdmin();
        AdminSuperAdminEnum superAdminEnum = AdminSuperAdminEnum.fetch(superAdmin);

        List<Right> rights = null;
        List<Integer> rightIds = null;

        switch (superAdminEnum) {
            case SUPER_ADMIN:
                //获取所有权限id集合
                rightIds = rightService.rightIds(PlatformEnum.OPERATOR.getCode());
                rights = rightService.rightsTree((byte) 1, PlatformEnum.OPERATOR.getCode(), 0, rightIds);
                break;
            case ORDINARY_ADMIN:
                Role role = roleService.selectByPrimaryKey(admin.getRoleId());
                rightIds = roleRightService.getRightIds(role.getId());
                rights = rightService.rightsTree((byte) 1, PlatformEnum.OPERATOR.getCode(), 0, rightIds);
                break;
            default:
                String msg = "无效的账号类型";
                log.warn(msg);
                throw new ScException(ResultCode.SYSTEM_BUSY);
        }
        AdminDTO adminInfo = adminDTOMapping.convert(admin);
        List<RightInfoDTO> rightInfoDTOS = RightConvertUtil.convertToRightInfo(rights);
        adminInfo.setRights(CollUtil.isEmpty(rightInfoDTOS) ? null : rightInfoDTOS.get(0).getChildren());
        loginResponse.setAdmin(adminInfo);
        loginResponse.setSessionId(sessionId);
        //缓存半小时
        redisTemplate.opsForValue().set(CacheConstant.NAMESPACE_ADMIN_LOGIN + sessionId, adminInfo,
                CacheConstant.EXPIRE_ADMIN_LOGIN, TimeUnit.SECONDS);

        response.setData(loginResponse);
        return response;
    }
}
