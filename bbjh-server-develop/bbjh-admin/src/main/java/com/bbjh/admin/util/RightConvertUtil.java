package com.bbjh.admin.util;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.bbjh.admin.mapping.RightDTOMapping;
import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.utils.BeanFactoryUtil;
import com.bbjh.common.model.Right;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fangwenbo
 * @Description 权限转换工具
 * @date 2019/4/10
 */
public class RightConvertUtil {

    public static List<RightInfoDTO> convertToRightInfo(List<Right> rights) {
        if (CollUtil.isEmpty(rights)) {
            return null;
        } else {
            List<RightInfoDTO> rightsTree = new ArrayList<>();
            for (int i = 0; i < rights.size(); i++) {
                Right right = rights.get(i);
                List<RightInfoDTO> convert = convertToRightInfo(right.getChildren());
                RightInfoDTO rightInfoDTO = BeanFactoryUtil.getBean(RightDTOMapping.class).convert(right);
                rightInfoDTO.setRightName(right.getRightName().getString(LocaleContextHolder.getLocale().toLanguageTag()));
                rightInfoDTO.setChildren(convert);
                rightsTree.add(rightInfoDTO);
            }
            return rightsTree;
        }
    }

    public static List<JSONObject> convert(List<Right> rights) {
        if (CollUtil.isEmpty(rights)) {
            return null;
        } else {
            List<JSONObject> rightsTree = new ArrayList<>();
            for (int i = 0; i < rights.size(); i++) {
                Right right = rights.get(i);
                List<JSONObject> convert = convert(right.getChildren());
                rightsTree.add(new JSONObject()
                        .fluentPut("key", right.getId())
                        .fluentPut("title", right.getRightName().getString(LocaleContextHolder.getLocale().toLanguageTag()))
                        .fluentPut("children", convert));
            }
            return rightsTree;
        }
    }

    public static List<JSONObject> convertOwn(List<Right> rights) {
        if (CollUtil.isEmpty(rights)) {
            return null;
        } else {
            List<JSONObject> rightsTree = new ArrayList<>();
            for (int i = 0; i < rights.size(); i++) {
                Right right = rights.get(i);
                List<JSONObject> convert = convertOwn(right.getChildren());
                rightsTree.add(new JSONObject()
                        .fluentPut("id", right.getId())
                        .fluentPut("platform", right.getRightLevel())
                        .fluentPut("children", convert));
            }
            return rightsTree;
        }
    }
}
