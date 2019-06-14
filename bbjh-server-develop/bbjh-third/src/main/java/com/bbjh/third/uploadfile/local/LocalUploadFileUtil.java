/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.third.uploadfile.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author fwb
 * @version V1.0.1
 * @Description
 * @date 2019/5/7  19:15
 */
@Configuration
@PropertySource("third.properties")
public class LocalUploadFileUtil {

    private static String localPath;
    private static String winLocalPath;

    @Value("${uploadFileToLocalPath}")
    public void setLocalPath(String localPath) {
        LocalUploadFileUtil.localPath = localPath;
    }
    @Value("${winUploadFileToLocalPath}")
    public void setWinLocalPath(String winLocalPath) {
        LocalUploadFileUtil.winLocalPath = winLocalPath;
    }

    public static void uplodFile(MultipartFile uploadFile) throws Exception{
        String os = System.getProperty("os.name");
        File file=null;
        if(os.toLowerCase().startsWith("win")){
            file = new File(winLocalPath);
            if(!file.exists()){
                file.mkdir();
            }
            file = new File(winLocalPath+"/"+uploadFile.getOriginalFilename());
        }else{
            file = new File(localPath);
            if(!file.exists()){
                file.mkdir();
            }
            file = new File(localPath+"/"+uploadFile.getOriginalFilename());
        }

        uploadFile.transferTo(file);
    }


}
