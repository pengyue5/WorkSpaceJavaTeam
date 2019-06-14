package com.bbjh.admin.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.bbjh.admin.mapper.RoleMapper;
import com.bbjh.admin.mapper.RoleRightMapper;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
import com.bbjh.common.service.impl.BaseServiceImpl;
import com.bbjh.admin.api.admin.sys.RoleAddRequest;
import com.bbjh.admin.api.admin.sys.RoleModifyRequest;

import com.bbjh.common.model.Role;
import com.bbjh.common.model.RoleRight;
import com.bbjh.admin.service.sys.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRightMapper roleRightMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addRole(RoleAddRequest request) {
        String roleName = request.getRoleName();

        Role role = new Role();
        List<Integer> rightIds = request.getRightIds();
        List<RoleRight> roleRights = new ArrayList<>(rightIds.size());
        //新增
        Role originalRole = roleMapper.selectOneByProperty(Role::getRoleName, roleName);

        if (originalRole != null) {
            String msg = StrUtil.format("{} 已经存在", roleName);
            log.warn(msg);
            throw new ScException(ResultCode.ROLENAME_EXISTS);
        }
        role.setPlatform(request.getPlatform());
        role.setRoleName(roleName);
        role.setOperatorId(request.getAoAdmin().getId());
        roleMapper.insertSelective(role);
        redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE, role.getId(), role);

        for (Integer rightId : rightIds) {
            RoleRight roleRight = new RoleRight();
            roleRight.setRoleId(role.getId());
            roleRight.setRightId(rightId);
            roleRight.setOperatorId(request.getAoAdmin().getId());
            Date now = new Date();
            roleRight.setCreateTime(now);
            roleRight.setUpdateTime(now);
            roleRights.add(roleRight);
        }
        roleRightMapper.insertList(roleRights);
        for (RoleRight roleRight : roleRights) {
            redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE_RIGHT, roleRight.getId(), roleRight);
        }
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer modifyRole(RoleModifyRequest request) {
        String roleName = request.getRoleName();

        Integer id = request.getId();
        Role role = new Role();
        List<Integer> newRightIds = request.getRightIds();
            //修改
        role.setId(id);
        role.setRoleName(roleName);

        roleMapper.updateByPrimaryKeySelective(role);

        redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE, role.getId(), role);

        List<RoleRight> oldRoleRights = roleRightMapper.selectByProperty(RoleRight::getRoleId, id);

        //删除旧的权限
        Weekend<RoleRight> weekend = Weekend.of(RoleRight.class);
        weekend.weekendCriteria()
                .andEqualTo(RoleRight::getRoleId, id);
        roleRightMapper.deleteByExample(weekend);

        //删除缓存中旧权限
        List<Integer> oldRoleRightIds = new ArrayList<>(oldRoleRights.size());

        oldRoleRights.forEach(roleRight -> {
            oldRoleRightIds.add(roleRight.getId());
        });
        redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_ROLE_RIGHT, oldRoleRightIds);

        List<RoleRight> newRoleRights = new ArrayList<>(newRightIds.size());
        for (Integer rightId : newRightIds) {
            RoleRight roleRight = new RoleRight();
            roleRight.setRoleId(role.getId());
            roleRight.setRightId(rightId);
            roleRight.setOperatorId(request.getAoAdmin().getId());
            Date now = new Date();
            roleRight.setCreateTime(now);
            roleRight.setUpdateTime(now);
            newRoleRights.add(roleRight);
        }
        roleRightMapper.insertList(newRoleRights);
        for (RoleRight roleRight : newRoleRights) {
            redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE_RIGHT, roleRight.getId(), roleRight);
        }
        return role.getId();
    }
}
