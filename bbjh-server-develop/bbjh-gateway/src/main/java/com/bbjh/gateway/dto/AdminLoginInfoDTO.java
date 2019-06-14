package com.bbjh.gateway.dto;

import com.bbjh.common.dto.RightInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AdminLoginInfoDTO implements Serializable {

    private Long id;

    private String name;

    private String loginName;

    private Byte platform;

    private Byte superAdmin;

    private Integer roleId;

    private List<RightInfoDTO> rights;
}
