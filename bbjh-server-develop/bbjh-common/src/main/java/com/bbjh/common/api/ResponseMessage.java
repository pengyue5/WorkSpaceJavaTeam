/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.api;

import com.github.pagehelper.PageInfo;
import com.bbjh.common.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "响应实体", description = "用于返回客户端的消息")
public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 返回成功或失败
     */
    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success = true;

    /**
     * 响应码,0为成功
     */
    @ApiModelProperty(value = "响应码", example = "0")
    private Integer code = ResultCode.OK.getCode();

    /**
     * 提交或返回数据
     */
    @ApiModelProperty(value = "响应数据", example = "true")
    private T data;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", example = "成功")
    private String msg = ResultCode.OK.getMsg();

    /**
     * 分页信息
     */
    @ApiModelProperty(value = "分页信息")
    private Page page;

    public static ResponseMessage ok(Object data) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setData(data);
        return responseMessage;
    }

    public static ResponseMessage error() {
        return new ResponseMessage<>()
                .setSuccess(false)
                .setCode(ResultCode.SYSTEM_BUSY.getCode())
                .setMsg(ResultCode.SYSTEM_BUSY.getMsg());
    }

    public static ResponseMessage error(ResultCode resultCode) {
        return new ResponseMessage()
                .setSuccess(false)
                .setCode(resultCode.getCode())
                .setMsg(resultCode.getMsg());
    }
    @ApiModelProperty(value = "响应数据")
    public T getData() {
        return data;
    }
    public ResponseMessage<T> setData(T data) {
        this.data = data;
        return this;
    }
}
