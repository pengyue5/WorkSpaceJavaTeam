/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.exception;


import com.bbjh.common.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScException extends RuntimeException {

    private ResultCode result;

    private Integer code;

    private String msg;

    public ScException(ResultCode result) {
        super(result.getMsg());
        this.result = result;
    }

    public ScException(ResultCode result, String msg) {
        super(result.getMsg());
        this.result = result;
        this.msg = msg;
    }

    public Integer getCode() {
        return result.getCode();
    }

    public String getMsg() {
        return null == msg ? result.getMsg() : msg;
    }

}
