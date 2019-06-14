package com.bbjh.admin.controller.admin.sys;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bbjh.admin.mapping.AdminListResponseMapping;
import com.bbjh.admin.mapping.AdminMapping;
import com.bbjh.common.enums.StatusEnum;
import com.bbjh.common.utils.PageConvertUtil;
import com.github.pagehelper.PageHelper;
import com.bbjh.admin.api.admin.sys.*;
import com.bbjh.common.model.Admin;
import com.bbjh.common.model.Role;
import com.bbjh.admin.service.account.AdminService;
import com.bbjh.admin.service.sys.RoleService;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
import com.bbjh.common.utils.PwdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/admin")
@Api(value = "系统-管理员", tags = {"系统-管理员"}, consumes = "application/json")
public class AdminController {

    @Autowired
    private AdminMapping adminMapping;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AdminListResponseMapping adminListResponseMapping;

    /**
     * 添加管理员账号
     * @param request
     * @return
     */
    @PostMapping("/addAdmin")
    @ApiOperation(value = "添加管理员账号", notes = "添加管理员账号", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Long> addAdmin(@Validated @RequestBody AdminAddRequest request) {
        ResponseMessage<Long> response = new ResponseMessage<>();
        String loginName = request.getLoginName();
        Weekend<Admin> weekend = Weekend.of(Admin.class);
        weekend.weekendCriteria()
                .andEqualTo(Admin::getLoginName, loginName)
                .andEqualTo(Admin::getStatus, StatusEnum.NORMAL.getCode());
        Admin originalAdmin = adminService.selectOneByExample(weekend);
        if (ObjectUtil.isNotNull(originalAdmin)) {
            String msg = StrUtil.format("登录名 {} 已经存在", loginName);
            throw new ScException(ResultCode.USER_EXISTS, msg);
        }
        Role role = roleService.selectByPrimaryKey(request.getRoleId());
        if (ObjectUtil.isNull(role)) {
            String msg = StrUtil.format("角色id {} 不存在", request.getRoleId());
            throw new ScException(ResultCode.ROLE_NOT_EXIST, msg);
        }
        String passwordDec = PwdUtil.decrypt(request.getPassword());
        String operateSign = RandomUtil.randomString(6);
        String passwordSign = RandomUtil.randomString(6);
        String md5Password = PwdUtil.passwordGenerate(passwordDec, passwordSign);

        Admin admin = adminMapping.convert(request);
        admin.setPasswordSign(passwordSign);
        admin.setOperateSign(operateSign);
        admin.setPassword(md5Password);
        admin.setOperatorId(request.getAoAdmin().getId());
        adminService.insertSelective(admin);
        response.setData(admin.getId());
        return response;
    }

    /**
     * 修改管理员账号
     * @param request
     * @return
     */
    @PostMapping("/modifyAdmin")
    @ApiOperation(value = "修改管理员账号", notes = "修改管理员账号", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Long> modifyAdmin(@Validated @RequestBody AdminModifyRequest request) {
        ResponseMessage<Long> response = new ResponseMessage<>();
        String loginName = request.getLoginName();
        Admin originalAdmin = adminService.selectOneByProperty(Admin::getLoginName, loginName);
        if (ObjectUtil.isNotNull(originalAdmin) && !originalAdmin.getId().equals(request.getId())) {
            String msg = StrUtil.format("登录名 {} 已经存在", loginName);
            throw new ScException(ResultCode.USER_EXISTS, msg);
        }
        Role role = roleService.selectByPrimaryKey(request.getRoleId());
        if (ObjectUtil.isNull(role)) {
            String msg = StrUtil.format("角色id {} 不存在", request.getRoleId());
            throw new ScException(ResultCode.ROLE_NOT_EXIST, msg);
        }

        Admin admin = adminMapping.convert(request);
        admin.setOperatorId(request.getAoAdmin().getId());
        adminService.updateByPrimaryKeySelective(admin);
        response.setData(admin.getId());
        return response;
    }

    /**
     * 删除管理员账号
     * @param request
     * @return
     */
    @PostMapping("/deleteAdmin")
    @ApiOperation(value = "删除管理员账号", notes = "删除管理员账号", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Boolean> deleteAdmin(@Validated @RequestBody AdminDeleteRequest request) {
        ResponseMessage<Boolean> response = new ResponseMessage<>();
        Admin admin = new Admin();
        admin.setId(request.getId());
        admin.setStatus(StatusEnum.DELETE.getCode());
        adminService.updateByPrimaryKeySelective(admin);
        response.setData(true);
        return response;
    }

    /**
     * 获取管理员列表
     * @param request
     * @return
     */
    @PostMapping("/adminList")
    @ApiOperation(value = "获取管理员列表", notes = "获取管理员列表", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<List<AdminListResponse>> adminList(@Validated @RequestBody AdminListRequest request) {
        ResponseMessage<List<AdminListResponse>> response = new ResponseMessage<>();
        PageHelper.startPage(request.currentPageNum(), request.currentPageSize());
        List<Admin> admins = adminService.adminList(request);

        List<AdminListResponse> adminList = new ArrayList<>(admins.size());
        admins.forEach(admin -> {
            AdminListResponse aoAdmin = adminListResponseMapping.convert(admin);
            Role role = ((Role) redisTemplate.opsForHash().get(CacheConstant.KEY_ALL_ROLE,
                    aoAdmin.getRoleId()));
            aoAdmin.setRoleName(ObjectUtil.isNotNull(role) ? role.getRoleName() : null);
            adminList.add(aoAdmin);
        });
        response.setData(adminList);
        response.setPage(PageConvertUtil.convert(adminList));
        return response;
    }

    /**
     * 重置密码
     * @param request
     * @return
     */
    @PostMapping("/resetPwd")
    @ApiOperation(value = "重置密码", notes = "重置密码", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Boolean> resetPwd(@Validated @RequestBody AdminResetPwdRequest request) {
        ResponseMessage<Boolean> response = new ResponseMessage<>();

        String originalPassword = PwdUtil.decrypt(request.getPassword());
        String operateSign = RandomUtil.randomString(6);
        String passwordSign = RandomUtil.randomString(6);
        String md5Password = PwdUtil.passwordGenerate(originalPassword, passwordSign);

        Admin admin = adminService.selectByPrimaryKey(request.getId());

        admin.setPasswordSign(passwordSign);
        admin.setOperateSign(operateSign);
        admin.setPassword(md5Password);
        admin.setUpdateTime(new Date());

        adminService.updateByPrimaryKeySelective(admin);
        response.setData(true);
        return response;
    }

}
