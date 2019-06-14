package com.bbjh.admin.service.account.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bbjh.admin.mapper.AdminMapper;
import com.bbjh.common.enums.StatusEnum;
import com.bbjh.common.service.impl.BaseServiceImpl;
import com.bbjh.admin.api.admin.sys.AdminListRequest;
import com.bbjh.common.model.Admin;
import com.bbjh.admin.service.account.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin, Integer> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> adminList(AdminListRequest request) {
        Weekend<Admin> weekend = Weekend.of(Admin.class);
        WeekendCriteria<Admin, Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria
                .andEqualTo(Admin::getStatus, StatusEnum.NORMAL.getCode())
                .andEqualTo(Admin::getPlatform, ObjectUtil.defaultIfNull(request.getPlatform(), null))
                .andEqualTo(Admin::getName, StrUtil.isEmpty(request.getName()) ? null : request.getName())
                .andEqualTo(Admin::getLoginName, StrUtil.isEmpty(request.getLoginName()) ? null : request.getLoginName())
                .andEqualTo(Admin::getPhone, StrUtil.isEmpty(request.getPhone()) ? null : request.getPhone())
                .andGreaterThanOrEqualTo(Admin::getCreateTime, ObjectUtil.defaultIfNull(request.getStartDate(), null))
                .andLessThanOrEqualTo(Admin::getCreateTime, ObjectUtil.defaultIfNull(request.getEndDate(), null));
        return adminMapper.selectByExample(weekend);
    }
}
