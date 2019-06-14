package com.bbjh.admin.service.sys;

import com.bbjh.common.service.BaseService;
import com.bbjh.common.model.Right;

import java.util.List;

public interface RightService extends BaseService<Right, Integer> {

    /**
     * 获取权限树
     * @return
     */
    List<Right> rightsTree(Byte rightLevel, Byte platform, Integer superId, List<Integer> rightIds);

    /**
     * 获取平台对应的权限树获取所有的权限id
     */
    List<Integer> rightIds(Byte platform);

}
