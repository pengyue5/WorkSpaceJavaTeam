/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.third.uploadfile;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author fwb
 * @version V1.0.1
 * @Description
 * @date 2019/5/7  19:07
 */
public interface UploadFileService {

    /**
     * @Description
     * @author fwb
     * @return
     *       fileUrl可以回显url
     *       filePath 入库路径
     */
    Map<String,String> uploadFile(MultipartFile uploadFile) throws Exception;

}
