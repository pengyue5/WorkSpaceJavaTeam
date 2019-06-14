package core;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.*;
import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.utils.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.security.KeyPair;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/4/13
 */
@Slf4j
public class PwdUtilTest {

    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJFdgVhMYj25WKl+X0/mWgsBYtEhA+lLOcFzUAIikI7+m0pgtePO6dbPZPIKPk5XXKOz2kQhzI6fGzoRKmWxQ6wRqR/rAOCjHqxTcDnWZCZCWboAvG1+C66tkY4nQqyD+AvWr3thZNIywf1HWyUNX3me8wqxAB3hh3/qL7b+KFrFAgMBAAECgYAoC4BEWNZ/dn3MQ3nyQtzvRzR3tAal0AlbF28lB2yXV+BjvvycgzW77Wo7m0LKxhpJJpSsTDtT1tTjTDNHzGt9oxscdWDYAgaQZpDaL/nBH4A9AJG2gPfszJUGrN7HtAQJWNAQUDwcdMSuz9rJRUt3T1e/QEz8nobzuPQ1AWACgQJBAMQZ8MaCGGcBcowKMY6F3RWP7pCe2ukJtsiH4FwqE3K5R8g2+/CWn3oRbHz7erydBpgToQJWbccbUs2FAJNc+VUCQQC9xEdwiV5xKprzTziCHohXgatv6CgcgK5oiuxMgfHgq1WkN2zq8XemjUrghy6q4AxE9viVnj78DkZaZ/vKRBuxAkAnwG5rfxG9R7DVrHdRQdeIOG4OyPTtSnfP/KNBa5IXrnFbp7G4mn/necK5Ly05MMeWalw4IhcMxoApgy2TscQlAkEAlblTlFsOBMPU1bvfnepxMHnCxdyqKTLuaNWTcxnjuZv1Skfgy84Q1XwNY/HExFVZ2N/zajkdAMpSf+ojI4dxQQJAO7Nio7zdggqd8nu2Cy7K9FWg3XEqff4HMZ8PKakwhOaq2p0gOUc4aVM1ajd2FzGU8yb1Rcwu5EfM19bv5lRsCQ==";
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRXYFYTGI9uVipfl9P5loLAWLRIQPpSznBc1ACIpCO/ptKYLXjzunWz2TyCj5OV1yjs9pEIcyOnxs6ESplsUOsEakf6wDgox6sU3A51mQmQlm6ALxtfguurZGOJ0Ksg/gL1q97YWTSMsH9R1slDV95nvMKsQAd4Yd/6i+2/ihaxQIDAQAB";

    @Test
    public void rsaTest() {
        KeyPair keyPair = SecureUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue());
        String privateKeyStr = Base64.encode(keyPair.getPrivate().getEncoded());
        log.info(privateKeyStr);
        String publicKeyStr = Base64.encode(keyPair.getPublic().getEncoded());
        log.info(publicKeyStr);

        RSA rsa = SecureUtil.rsa(privateKeyStr, publicKeyStr);

        byte[] encrypt = rsa.encrypt("123456", KeyType.PublicKey);
        String encode = Base64.encode(encrypt);
        log.info(encode);
        byte[] decrypt = rsa.decrypt(Base64.decode(encode), KeyType.PrivateKey);
        log.info(StrUtil.str(decrypt, "UTF-8"));
    }

    @Test
    public void signTest() {
        //商户签名步骤
        //创建商户签名对象
        Sign partnerSign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, null);

        //待发送数据
        JSONObject params = new JSONObject();
        params.fluentPut("header", new JSONObject()
                .fluentPut("deviceType", 5)
                .fluentPut("locale", "vn")
                .fluentPut("algorithm", "MD5withRSA"))
                .fluentPut("appId", 1000011110)
                .fluentPut("partnerId", 100001)
                .fluentPut("name", "张三");
        String randomStr = RandomUtil.randomString(10);
        //得到拼接的字符串
        String joinStr = PwdUtil.paramsJoin(params, randomStr);

        log.info("待发送数据:[{}]", joinStr);

        //对待发送数据进行签名
        byte[] originalSign = partnerSign.sign(joinStr.getBytes(Charset.forName("UTF-8")));

        //对签名进行Base64编码
        String signEncode = Base64.encode(originalSign, Charset.forName("UTF-8"));
        log.info("签名的Base64编码:[{}]", signEncode);

        //贷超验签步骤
        //创建贷超签名对象
        Sign loanMarketSign = SecureUtil.sign(SignAlgorithm.MD5withRSA, null, publicKey);

        //对签名的Base64串解码
        byte[] signDecode = Base64.decode(signEncode, Charset.forName("UTF-8"));

        Assert.assertTrue(loanMarketSign.verify(joinStr.getBytes(Charset.forName("UTF-8")), signDecode));
    }
}
