package com.bbjh.admin.dto;

import com.bbjh.common.dto.RightInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AdminDTO implements Serializable {

    private Long id;

    private String name;

    private String loginName;

    private Byte platform;

    private Byte superAdmin;

    private Integer roleId;

    private List<RightInfoDTO> rights;
}
