package com.bbjh.admin.api.admin.sys;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RightTreeResponse implements Serializable {

    private List<JSONObject> rightIds;

    private List<JSONObject> rightTree;
}
