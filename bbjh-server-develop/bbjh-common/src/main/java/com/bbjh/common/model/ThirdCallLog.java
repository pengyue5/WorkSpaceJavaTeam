package com.bbjh.common.model;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.mybatis.SnowflakeIdGenId;
import com.bbjh.common.mybatis.type.JSONObjectTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Table(name = "dc_third_call_log")
public class ThirdCallLog implements Serializable {
    @Id
    @KeySql(genId = SnowflakeIdGenId.class)
    private Long id;

    /**
     * 请求url
     */
    @Column(name = "call_url")
    private String callUrl;

    /**
     * 调用类型 1：发送方；5：接收方
     */
    @Column(name = "call_type")
    private Byte callType;

    /**
     * 是否成功 1：是；0：否
     */
    @Column(name = "call_status")
    private Boolean callStatus;

    /**
     * 记录状态  1：正常；0：删除；
     */
    private Boolean status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者id
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 请求消息
     */
    @Column(name = "request_body")
    @ColumnType(typeHandler = JSONObjectTypeHandler.class)
    private JSONObject requestBody;

    /**
     * 响应消息
     */
    @Column(name = "response_body")
    @ColumnType(typeHandler = JSONObjectTypeHandler.class)
    private JSONObject responseBody;

}