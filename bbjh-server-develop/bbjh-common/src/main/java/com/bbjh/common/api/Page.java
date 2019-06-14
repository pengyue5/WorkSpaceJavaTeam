package com.bbjh.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/4/11
 */
@Getter
@Setter
@ApiModel(value = "分页信息", description = "用于请求分页接口传递分页信息")
@Accessors(chain = true)
public class Page implements Serializable {

    @ApiModelProperty(name = "当前页", value = "1")
    private Integer pageNum;

    @ApiModelProperty(name = "每页大小", value = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "总数")
    private Integer total;
}
