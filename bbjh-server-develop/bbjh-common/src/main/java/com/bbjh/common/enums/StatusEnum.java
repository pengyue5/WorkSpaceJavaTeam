/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.enums;

/**
 * @author fangwenbo
 * @version V1.0.1
 * @Description 删除状态
 * @date 2018年1月24日 下午4:48:34
 */
public enum StatusEnum {

    NORMAL((byte) 1, "正常状态"),

    DELETE((byte) 0, "伪删除状态 ");

    private byte code;
    private String name;

    StatusEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(byte code) {
        for (StatusEnum del : StatusEnum.values()) {
            if (del.code == code) {
                return del.name;
            }
        }
        return null;
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
