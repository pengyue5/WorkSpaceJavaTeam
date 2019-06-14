/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.admin.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fangwenbo
 * @version V1.0.1
 * @Description 管理员是否允许登录枚举
 * @date 2018年1月25日 上午9:58:40
 */
public enum AdminLockEnum {

    OK((byte) 1, "正常"),

    LOCK((byte) -1, "停用");

    private static final Map<Byte, AdminLockEnum> map;

    static {
        map = new HashMap<>();
        for (AdminLockEnum as : values()) {
            map.put(as.code, as);
        }
    }

    private byte code;
    private String name;

    AdminLockEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据编码获取状态
     *
     * @param code
     * @return
     */
    public static AdminLockEnum fetch(byte code) {
        return map.get(code);
    }

    /**
     * 根据编码获取名称
     *
     * @param code
     * @return
     */
    public static String fetchName(byte code) {
        AdminLockEnum as = map.get(code);
        return as != null ? as.name : null;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
