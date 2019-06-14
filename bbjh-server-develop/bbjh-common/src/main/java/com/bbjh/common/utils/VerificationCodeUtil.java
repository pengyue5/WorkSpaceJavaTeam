/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.utils;

import java.util.Random;

/**
 * @author fwb
 * @version V1.0.1
 * @Description
 * @date 2019/5/21  11:19
 */
public class VerificationCodeUtil {


    /**
     * 生成六位验证码
     * @Description
     * @author fwb
     */
    public static String generationCode(){
        String verifyCode = String
                .valueOf(new Random().nextInt(899999)+ 100000 );
        return verifyCode;
    }

}
