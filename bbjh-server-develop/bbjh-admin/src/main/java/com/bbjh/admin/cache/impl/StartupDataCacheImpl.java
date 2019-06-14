package com.bbjh.admin.cache.impl;

import com.bbjh.admin.cache.StartupDataCache;
import com.bbjh.admin.mapping.RightDTOMapping;
import com.bbjh.admin.mapper.RightMapper;
import com.bbjh.admin.mapper.RoleMapper;
import com.bbjh.admin.mapper.RoleRightMapper;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class StartupDataCacheImpl implements StartupDataCache {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RightDTOMapping rightDTOMapping;

    @Autowired
    private RightMapper rightMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRightMapper roleRightMapper;

    @PostConstruct
    private void init() {
        //加载所有角色
        //List<Role> roles = roleMapper.selectByExample(null);
        ////先清除已有角色缓存
        //Set rolesKeys = redisTemplate.opsForHash().keys(CacheConstant.KEY_ALL_RIGHT);
        //redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_ROLE, rolesKeys.toArray());
        //roles.forEach(role -> redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE, role.getId(), role));
        //
        ////加载所有权限
        //List<Right> rights = rightMapper.selectByExample(null);
        ////先清除已有权限缓存
        //Set rightsKeys = redisTemplate.opsForHash().keys(CacheConstant.KEY_ALL_RIGHT);
        //redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_RIGHT, rightsKeys.toArray());
        //rights.forEach(right -> {
        //    RightInfoDTO rightInfo = rightDTOMapping.convert(right);
        //    redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_RIGHT, rightInfo.getId(), rightInfo);
        //});
        //
        ////加载所有角色权限
        //List<RoleRight> roleRights = roleRightMapper.selectByExample(null);
        ////先清除已有角色权限缓存
        //Set roleRightsKeys = redisTemplate.opsForHash().keys(CacheConstant.KEY_ALL_ROLE_RIGHT);
        //redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_ROLE_RIGHT, roleRightsKeys.toArray());
        //roleRights.forEach(roleRight ->
        //    redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_ROLE_RIGHT, roleRight.getId(), roleRight));

    }

}
