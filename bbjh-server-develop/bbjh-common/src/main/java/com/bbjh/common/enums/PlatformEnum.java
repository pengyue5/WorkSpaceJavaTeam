package com.bbjh.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台类型
 */
@Getter
public enum PlatformEnum {

    /**
     * 运营
     */
    OPERATOR((byte) 2, "运营"),

    /**
     * App
     */
    APP((byte) 4, "APP"),

    /**
     * 公共
     */
    COMMON((byte) 5, "公共"),

    ;

    private Byte code;
    private String name;

    private static final Map<Byte, PlatformEnum> map;

    static {
        map = new HashMap<>();
        for (PlatformEnum as : values()) {
            map.put(as.code, as);
        }
    }

    PlatformEnum(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Byte code) {
        for (PlatformEnum userSexType : PlatformEnum.values()) {
            if (userSexType.code.equals(code)) {
                return userSexType.name;
            }
        }
        return null;
    }

    /**
     * 根据编码获取状态
     *
     * @param code
     * @return
     */
    public static PlatformEnum fetch(Byte code) {
        return map.get(code);
    }

    /**
     * 根据编码获取名称
     *
     * @param code
     * @return
     */
    public static String fetchName(Byte code) {
        PlatformEnum as = map.get(code);
        return as != null ? as.name : null;
    }

}
