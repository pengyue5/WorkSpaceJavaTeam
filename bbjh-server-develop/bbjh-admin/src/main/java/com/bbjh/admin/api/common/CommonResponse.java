package com.bbjh.admin.api.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 通用响应，用于更新行数，或者data无返回，禁止修改
 */
@Getter
@Setter
public class CommonResponse implements Serializable {

    private Integer result;
}
