/**************************************************************************
 * 河北世窗信息技术股份有限公司
 * All rights reserved.
 *
 * 项目名称：宝贝计划
 ***************************************************************************/
package com.bbjh.common.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author fangwenbo
 * @version V1.0.1
 * @Description 请求基类封装
 * @date 2018年1月25日 上午9:49:09
 */
@ApiModel(description = "用于指定所有接口的基本参数")
public class RequestMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 请求的header
     */
    protected Header header;

//    @ApiModelProperty(hidden = true)
//    protected AdminLoginInfoDTO aoAdmin;
//
//    @ApiModelProperty(hidden = true)
//    protected APAdminLoginInfoDTO apAdmin;
//
    @ApiModelProperty(required = true)
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
//
//    public AdminLoginInfoDTO getAoAdmin() {
//        return aoAdmin;
//    }
//    public void setAoAdmin(AdminLoginInfoDTO aoAdmin) {
//        this.aoAdmin = aoAdmin;
//    }
//
//    public APAdminLoginInfoDTO getApAdmin() {
//        return apAdmin;
//    }
//    public void setApAdmin(APAdminLoginInfoDTO apAdmin) {
//        this.apAdmin = apAdmin;
//    }

    /**
    * 快速获取当前page的pageNum
    */
    public Integer currentPageNum() {
        Integer pageNum = 1;
        Page page = header.getPage();
        if (ObjectUtil.isNotNull(page)) {
            pageNum = page.getPageNum();
            if (ObjectUtil.isNull(pageNum) || pageNum < 1) {
                pageNum = 1;
                page.setPageNum(pageNum);
            }
        }
        return pageNum;
    }

    public Integer currentPageSize() {
        Integer pageSize = 1;
        Page page = header.getPage();
        if (ObjectUtil.isNotNull(page)) {
            pageSize = page.getPageSize();
            if (ObjectUtil.isNull(pageSize) || pageSize < 1) {
                pageSize = 10;
                page.setPageSize(pageSize);
            }
        }
        return pageSize;
    }
}
