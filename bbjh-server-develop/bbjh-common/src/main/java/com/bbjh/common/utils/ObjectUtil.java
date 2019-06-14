package com.bbjh.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author fwb
 * @Description
 * @date 2019/5/24
 */
public class ObjectUtil {

    /**
     * 任意对象转换成指定对象
     * @return
     */
    public static <T> T convert(Object o, Class<T> clazz) {
        String json = JSONObject.toJSONString(o);
        return JSONObject.parseObject(json, clazz);
    }
}
