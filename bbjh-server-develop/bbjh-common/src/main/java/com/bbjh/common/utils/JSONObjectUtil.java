package com.bbjh.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author fwb
 * @Description JSONObject工具
 * @date 2019/5/6
 */
public class JSONObjectUtil {

    /**
     * 任意对象转换成JSONObject对象
     * @return
     */
    public static JSONObject convert(Object o) {
        return JSONObject.parseObject(JSONObject.toJSONString(o));
    }

}
