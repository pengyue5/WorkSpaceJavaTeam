package com.bbjh.admin.controller.admin.sys;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bbjh.common.enums.PlatformEnum;
import com.bbjh.common.enums.StatusEnum;
import com.bbjh.common.utils.PageConvertUtil;
import com.github.pagehelper.PageHelper;
import com.bbjh.admin.api.admin.sys.*;
import com.bbjh.common.model.Admin;
import com.bbjh.common.model.Right;
import com.bbjh.common.model.Role;
import com.bbjh.common.model.RoleRight;
import com.bbjh.admin.service.account.AdminService;
import com.bbjh.admin.service.sys.RightService;
import com.bbjh.admin.service.sys.RoleRightService;
import com.bbjh.admin.service.sys.RoleService;
import com.bbjh.admin.util.RightConvertUtil;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/role")
@Api(value = "系统-角色", tags = {"系统-角色"}, consumes = "application/json")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RightService rightService;

    @Autowired
    private RoleRightService roleRightService;
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加角色
     * @param request
     * @return
     */
    @PostMapping("/addRole")
    @ApiOperation(value = "添加角色", notes = "添加角色", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Integer> addModifyRole(@Validated @RequestBody RoleAddRequest request) {
        ResponseMessage<Integer> response = new ResponseMessage<>();
        Integer id = roleService.addRole(request);
        response.setData(id);
        return response;
    }

    /**
     * 修改角色
     * @param request
     * @return
     */
    @PostMapping("/modifyRole")
    @ApiOperation(value = "修改角色", notes = "修改角色", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Integer> addModifyRole(@Validated @RequestBody RoleModifyRequest request) {
        ResponseMessage<Integer> response = new ResponseMessage<>();
        Integer id = roleService.modifyRole(request);
        response.setData(id);
        return response;
    }

    /**
     * 删除角色
     * @param request
     * @return
     */
    @PostMapping("/deleteRole")
    @ApiOperation(value = "删除角色", notes = "删除角色", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Boolean> deleteRole(@Validated @RequestBody RoleDeleteRequest request) {
        ResponseMessage<Boolean> response = new ResponseMessage<>();
        Integer id = request.getId();

        Weekend<Admin> adminWeekend = Weekend.of(Admin.class);
        adminWeekend.weekendCriteria()
                .andEqualTo(Admin::getStatus, StatusEnum.NORMAL.getCode())
                .andEqualTo(Admin::getRoleId, ObjectUtil.defaultIfNull(request.getId(), null));
        //正常用户数量
        int normalAdminCount = adminService.selectCountByExample(adminWeekend);
        if (normalAdminCount > 0) {
            String msg = StrUtil.format("角色编号 {} 存在正常用户，请先删除相关用户", request.getId());
            log.warn(msg);
            throw new ScException(ResultCode.ROLE_HAS_NORMAL_ADMIN);
        }

        List<RoleRight> roleRights = roleRightService.selectByProperty(RoleRight::getRoleId, id);

        List<Integer> roleRightIds = new ArrayList<>(roleRights.size());

        roleRights.forEach(roleRight -> {
            roleRightIds.add(roleRight.getId());
        });

        Weekend<RoleRight> weekend = Weekend.of(RoleRight.class);
        weekend.weekendCriteria()
                .andEqualTo("roleId", id);
        roleRightService.deleteByExample(weekend);
        redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_ROLE_RIGHT, roleRightIds);

        roleService.deleteByPrimaryKey(id);
        redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_ROLE, id);
        response.setData(true);
        return response;
    }

    /**
     * 获取角色的权限树
     * @param request
     * @return
     */
    @PostMapping("/roleRightTree")
    @ApiOperation(value = "获取角色的权限树", notes = "获取角色的权限树", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<RightTreeResponse> roleRightTree(@Validated @RequestBody RoleRightTreeRequest request) {
        ResponseMessage<RightTreeResponse> response = new ResponseMessage<>();
        RightTreeResponse aoRightTreeResponse = new RightTreeResponse();

        PlatformEnum fetch = PlatformEnum.fetch(request.getPlatform());

        //自己拥有的权限树
        List<Right> ownRights = null;
        if (ObjectUtil.isNotNull(request.getId())) {
            List<Integer> rightIds = roleRightService.getRightIds(request.getId());
            ownRights = rightService.rightsTree((byte) 1, fetch.getCode(), 0,
                    rightIds).get(0).getChildren();
        }

        //整个权限树
        List<Right> rights = rightService.rightsTree((byte)1, fetch.getCode(), 0,
                rightService.rightIds(request.getPlatform())).get(0).getChildren();

        aoRightTreeResponse.setRightIds(RightConvertUtil.convertOwn(ownRights));
        aoRightTreeResponse.setRightTree(RightConvertUtil.convert(rights));
        response.setData(aoRightTreeResponse);
        return response;
    }

    /**
     * 获取角色列表
     * @param request
     * @return
     */
    @PostMapping("/roleList")
    @ApiOperation(value = "获取角色的权限树", notes = "获取角色的权限树", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<List<Role>> roleList(@Validated @RequestBody RoleListRequest request) {
        ResponseMessage<List<Role>> response = new ResponseMessage<>();

        PageHelper.startPage(request.currentPageNum(), request.currentPageSize());
        List<Role> roles = roleService.selectByProperty(Role::getPlatform, request.getPlatform());

        response.setData(roles);
        response.setPage(PageConvertUtil.convert(roles));
        return response;
    }

    /**
     * 获取角色名列表
     * @param request
     * @return
     */
    @PostMapping("/roleNameList")
    @ApiOperation(value = "获取角色名列表", notes = "获取角色名列表", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<List<Role>> roleList(@Validated @RequestBody RoleNameListRequest request) {
        ResponseMessage<List<Role>> response = new ResponseMessage<>();
        Weekend<Role> weekend = Weekend.of(Role.class);
        weekend.selectProperties("id", "roleName");
        weekend.weekendCriteria()
                .andEqualTo(Role::getPlatform, request.getPlatform());
        List<Role> roles = roleService.selectByExample(weekend);

        response.setData(roles);
        return response;
    }
}
