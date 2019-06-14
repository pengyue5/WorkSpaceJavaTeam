package com.bbjh.admin.service.sys.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bbjh.admin.mapper.RoleRightMapper;
import com.bbjh.common.service.impl.BaseServiceImpl;
import com.bbjh.common.model.RoleRight;
import com.bbjh.admin.service.sys.RoleRightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoleRightServiceImpl extends BaseServiceImpl<RoleRight, Integer> implements RoleRightService {

    @Autowired
    private RoleRightMapper roleRightMapper;

    @Override
    public List<Integer> getRightIds(Integer roleId) {
        List<Integer> rightIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(roleId)) {
            List<RoleRight> roleRights = roleRightMapper.selectByProperty(RoleRight::getRoleId, roleId);
            roleRights.forEach(r -> {
                rightIds.add(r.getRightId());
            });
        }
        return rightIds;
    }
}
