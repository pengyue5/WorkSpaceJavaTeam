package com.bbjh.admin.controller.admin.sys;

import cn.hutool.core.util.StrUtil;
import com.bbjh.admin.api.admin.sys.*;
import com.bbjh.admin.mapping.RightDTOMapping;
import com.bbjh.admin.mapping.RightMapping;
import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.model.Right;
import com.bbjh.common.model.RoleRight;
import com.bbjh.admin.service.sys.RightService;
import com.bbjh.admin.service.sys.RoleRightService;
import com.bbjh.admin.util.RightConvertUtil;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/right")
@Api(value = "系统-权限", tags = {"系统-权限"}, consumes = "application/json")
public class RightController {

    @Autowired
    private RightMapping rightMapping;

    @Autowired
    private RightService rightService;

    @Autowired
    private RoleRightService roleRightService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RightDTOMapping rightDTOMapping;

    /**
     * 添加权限
     * @param request
     * @return
     */
    @PostMapping("/addRight")
    @ApiOperation(value = "添加权限", notes = "添加权限", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Integer> addRight(@Validated @RequestBody RightAddRequest request) {
        ResponseMessage<Integer> response = new ResponseMessage<>();
        Right right = rightMapping.convert(request);
        rightService.insertSelective(right);
        RightInfoDTO rightInfo = rightDTOMapping.convert(right);
        redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_RIGHT, rightInfo.getId(), rightInfo);
        response.setData(right.getId());
        return response;
    }

    /**
     * 修改权限
     * @param request
     * @return
     */
    @PostMapping("/modifyRight")
    @ApiOperation(value = "修改权限", notes = "修改权限", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Integer> modifyRight(@Validated @RequestBody RightModifyRequest request) {
        ResponseMessage<Integer> response = new ResponseMessage<>();
        Right right = rightMapping.convert(request);
        rightService.updateByPrimaryKeySelective(right);
        RightInfoDTO rightInfo = rightDTOMapping.convert(right);
        redisTemplate.opsForHash().put(CacheConstant.KEY_ALL_RIGHT, rightInfo.getId(), rightInfo);
        response.setData(right.getId());
        return response;
    }

    /**
     * 获取权限树
     * @param request
     * @return
     */
    @PostMapping("/rightTree")
    @ApiOperation(value = "获取权限树", notes = "获取权限树", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<RightInfoDTO> rightTree(@Validated @RequestBody RightTreeRequest request) {
        ResponseMessage<RightInfoDTO> response = new ResponseMessage<>();
        List<Right> rights = rightService.rightsTree((byte)1, request.getPlatform(), 0,
                rightService.rightIds(request.getPlatform()));
        response.setData(RightConvertUtil.convertToRightInfo(rights).get(0));
        return response;
    }

    /**
     * 获取权限详情
     * @param request
     * @return
     */
    @PostMapping("/detail")
    @ApiOperation(value = "获取权限详情", notes = "获取权限详情", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Right> detail(@Validated @RequestBody RightDetailRequest request) {
        ResponseMessage<Right> response = new ResponseMessage<>();
        Right right = rightService.selectByPrimaryKey(request.getId());
        response.setData(right);
        return response;
    }

    /**
     * 删除权限
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除权限", notes = "删除权限", response = ResponseMessage.class, produces = "application/json")
    public ResponseMessage<Boolean> delete(@Validated @RequestBody RightDeleteRequest request) {
        ResponseMessage<Boolean> resposne = new ResponseMessage<>();
        Integer id = request.getId();
        int rightIdCount = roleRightService.selectCountByProperty(RoleRight::getRightId, id);

        if (rightIdCount > 0) {
            String msg = StrUtil.format("权限编号 {} 在使用，请先取消相关角色授权后再进行删除！", id);
            log.warn(msg);
            throw new ScException(ResultCode.RIGHT_IS_USING);
        }

        int superIdCount = rightService.selectCountByProperty(Right::getSuperId, id);

        if (superIdCount> 0) {
            String msg = StrUtil.format("权限编号 {} 存在子权限，请先删除子权限后后再进行删除！", id);
            log.warn(msg);
            throw new ScException(ResultCode.RIGHT_IS_USING);
        }

        boolean success = rightService.deleteByPrimaryKey(id);
        redisTemplate.opsForHash().delete(CacheConstant.KEY_ALL_RIGHT, id);
        resposne.setData(success);
        return resposne;
    }
}
