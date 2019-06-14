package com.bbjh.admin.api.admin.account;

import com.bbjh.admin.dto.AdminDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginResponse implements Serializable {

    private AdminDTO admin;

    private String sessionId;
}
