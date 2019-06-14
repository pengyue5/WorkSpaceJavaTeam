package com.bbjh.common.model;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.mybatis.type.JSONObjectTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "dc_right")
public class Right extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ColumnType(typeHandler = JSONObjectTypeHandler.class)
    private JSONObject rightName;

    private String rightUrl;

    private String rightPath;

    private String component;

    private Byte platform;

    private Integer superId;

    private Byte rightType;

    private Integer rightOrder;

    /**
     * 是否需要授权；1：需要；0：不需要
     */
    private Byte requireAuth;

    private String rightPic;

    private String uniqueSign;

    private Byte rightLevel;

    private Byte rightVisible;

    private String remark;

    private String operatorId;

    @Transient
    private List<Right> children;

}