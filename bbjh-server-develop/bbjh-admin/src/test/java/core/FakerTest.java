package core;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
import com.bbjh.admin.AdminApplication;
import com.bbjh.common.utils.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author fwb
 * @since 2018/11/27
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
public class FakerTest {

    @Test
    public void fakerTest() throws UnsupportedEncodingException {
        Faker faker = new Faker(Locale.CHINA);
        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449
        log.info(JSONObject.toJSONString(faker.address().city(), true));
        log.info(faker.friends().location());

    }

    @Test
    public void pwdTest() {
        String originalPassword = "123456";
        String encrypt = PwdUtil.encrypt(originalPassword);
        log.info("加密后的base64串:{}", encrypt);

        String decrypt = PwdUtil.decrypt(encrypt);
        log.info("解密后的原数据:{}", decrypt);

        assertEquals(originalPassword, decrypt);

        String passwordSign = RandomUtil.randomString(6);
        String md5Password = PwdUtil.passwordGenerate(decrypt, passwordSign);

        log.info("passwordSign:{}", passwordSign);
        log.info("数据库中存储的密码:{}", md5Password);
    }

}
