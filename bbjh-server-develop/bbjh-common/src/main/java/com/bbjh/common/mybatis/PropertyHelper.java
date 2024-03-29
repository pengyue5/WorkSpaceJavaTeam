package com.bbjh.common.mybatis;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/**
 * @author fangwenbo
 * @Description 通用mapper方法扩展相关工具方法
 * @date 2019/4/8
 */
public class PropertyHelper {

    /**
     * 根据实体Class和属性名获取对应的表字段名
     * @param entityClass 实体Class对象
     * @param property 属性名
     * @return
     */
    public static String getColumnByProperty(Class<?> entityClass, String property) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        EntityColumn entityColumn = entityTable.getPropertyMap().get(property);
        return entityColumn.getColumn();
    }
}
