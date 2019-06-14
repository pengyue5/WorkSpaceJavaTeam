/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.third.uploadfile.impl;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.utils.BeanFactoryUtil;
import com.bbjh.third.uploadfile.UploadFileService;
import com.bbjh.third.uploadfile.local.LocalUploadFileUtil;
import com.bbjh.third.uploadfile.oss.UploadToOSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fwb
 * @version V1.0.1
 * @Description
 * @date 2019/5/7  19:07
 */
@Slf4j
@Service
@PropertySource("classpath:/third.properties")
public class UplodFileServiceImpl implements UploadFileService {

    @Value("${uploadType}")
    private String uploadType;
    @Value("${staticAccessPath}")
    private  String staticAccessPath;

    @Override
    public Map<String,String>  uploadFile(MultipartFile uploadFile) throws Exception {
        //可以回显的url
        Map<String,String> result=new HashMap();
        UploadType uploadType = UploadType.valueOfType(this.uploadType);
        switch (uploadType){
            case LOCAL:
                String host = InetAddress.getLocalHost().getHostAddress();
                TomcatServletWebServerFactory tomcatServletWebServerFactory= (TomcatServletWebServerFactory) BeanFactoryUtil.getBean(TomcatServletWebServerFactory.class);
                int port = tomcatServletWebServerFactory.getPort();
                String contextPath = tomcatServletWebServerFactory.getContextPath();
                String fileUrl = "http://" + host + ":" + port + contextPath  + staticAccessPath+"/"+uploadFile.getOriginalFilename();
                fileUrl=fileUrl.replace("**","");
                String filePath = staticAccessPath.replace("**", "") + "/" + uploadFile.getOriginalFilename();
                LocalUploadFileUtil.uplodFile(uploadFile);
                result.put("fileUrl",fileUrl);
                result.put("filePath",filePath);
                break;
            case OSS:
                String fileOriginalName = uploadFile.getOriginalFilename();
                String fileType = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1);
                result=UploadToOSS.uploadFileToOss(uploadFile, fileType);
                break;
            default:
                log.error("配置错误");
                    break;
        }
        log.info(JSONObject.toJSONString(result));
        return result;
    }
}

enum UploadType{

    LOCAL("local"),
    OSS("oss");

    private String type;

    private static Map<String,UploadType> map;

    UploadType(String type) {
        this.type = type;
    }
    static {
        map=new HashMap<>();
        for (UploadType value : UploadType.values()) {
            map.put(value.getType(),value);
        }
    }
    public static UploadType valueOfType(String type){
        return map.get(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}