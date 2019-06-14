package com.bbjh.admin.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.bbjh.admin.mapper.RightMapper;
import com.bbjh.common.service.impl.BaseServiceImpl;
import com.bbjh.common.model.Right;
import com.bbjh.admin.service.sys.RightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RightServiceImpl extends BaseServiceImpl<Right, Integer> implements RightService {

    @Autowired
    private RightMapper rightMapper;

    /**
     * 获取权限树
     * @param rightLevel 权限层级，默认为1
     * @param platform
     * @param superId 父节点id
     * @param rightIds 权限id集合
     * @return
     */
    @Override
    public List<Right> rightsTree(Byte rightLevel, Byte platform, Integer superId, List<Integer> rightIds) {
        if (CollUtil.isEmpty(rightIds)) {
            rightIds = new ArrayList<>();
        }
        Weekend<Right> weekend1 = Weekend.of(Right.class);
        weekend1.weekendCriteria()
                .andEqualTo(Right::getPlatform, platform)
                .andEqualTo(Right::getSuperId, 0);
        Right rootRight = rightMapper.selectOneByExample(weekend1);
        //添加对应机构的根权限
        rightIds.add(rootRight.getId());

        Weekend<Right> weekend = Weekend.of(Right.class);
        weekend.orderBy("rightOrder");
        weekend.weekendCriteria()
                .andEqualTo(Right::getPlatform, platform)
                .andEqualTo(Right::getSuperId, superId)
                .andIn(Right::getId, rightIds);
        List<Right> rights = rightMapper.selectByExample(weekend);
        if (rights != null && rights.size() > 0) {
            rightLevel++;
            for (Right right : rights) {
                List<Right> rightList = rightsTree(rightLevel, platform, right.getId(), rightIds);
                right.setChildren(rightList);
            }
            return rights;
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> rightIds(Byte platform) {
        List<Right> rights = rightMapper.selectByProperty(Right::getPlatform, platform);
        List<Integer> rightIds = new ArrayList<>(rights.size());
        rights.forEach(r -> {
            rightIds.add(r.getId());
        });
        return rightIds;
    }
}
