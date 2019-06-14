package com.bbjh.admin.controller.common;

import com.bbjh.common.api.ResponseMessage;
import com.bbjh.third.uploadfile.impl.UplodFileServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/upload")
@ApiOperation(value = "文件上传", notes = "用于文件上传", response = ResponseMessage.class, produces = "application/json")
public class FileController {

    @Autowired
    private UplodFileServiceImpl uplodFileService;

    /**
     * 上传文件调用接口
     * @Description
     * @param uploadFile
     * @author fwb
     */
   @RequestMapping(value = "/uploadFile")
   @ResponseBody
   @ApiOperation(value = "上传文件调用接口", notes = "上传文件调用接口", response = ResponseMessage.class, produces = "application/json")
   public Map<String,String> uploadFile(MultipartFile uploadFile) {
       String result=null;
       try {
           return uplodFileService.uploadFile(uploadFile);
       } catch (Exception e) {
           log.error("上传文件异常:",e);
       }
       return null;
   }
}
