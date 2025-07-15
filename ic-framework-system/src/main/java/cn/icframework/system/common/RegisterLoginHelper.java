package cn.icframework.system.common;

import cn.icframework.cache.utils.CacheUtils;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.RsaUtils;
import cn.icframework.system.consts.CacheKeys;
import cn.icframework.system.utils.CaptchaUtils;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

/**
 *
 * @author hzl
 * @date 2024/8/21
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterLoginHelper {
    private final Producer captchaProducer;

    /**
     * 获取登录加密公钥密钥
     *
     * @param userType
     * @param username 账号
     * @return
     */
    public String buildKey(String userType, String username) {
        Assert.isNotEmpty(userType, "userType不能为空");
        Assert.isNotEmpty(username, "username不能为空");
        Map<String, String> keys = RsaUtils.createKeys(1024);
        CacheUtils.set(CacheKeys.appendKey(CacheKeys.LOGIN_KEY_PREFIX, userType, username), keys.get("privateKey"), 10L);
        return keys.get("publicKey");
    }

    /**
     * 获取登录加密私钥密钥
     *
     * @param userType
     * @param username 账号
     * @return
     */
    public String getCacheKey(String userType, String username) {
        Assert.isNotEmpty(userType, "userType不能为空");
        Assert.isNotEmpty(username, "username不能为空");
        String cacheKey = CacheKeys.appendKey(CacheKeys.LOGIN_KEY_PREFIX, userType, username);
        Object key = CacheUtils.get(cacheKey);
        Assert.isNotNull(key, "未获取到登录密钥");
        CacheUtils.remove(cacheKey);
        return key.toString();
    }

    /**
     * 解密密码
     *
     * @param userType
     * @param username 账号
     * @return
     */
    public String decryptPassWd(String userType, String username, String passwd) {
        Assert.isNotEmpty(userType, "userType不能为空");
        Assert.isNotEmpty(username, "username不能为空");
        Assert.isNotEmpty(passwd, "密码不能为空");
        // 获取加密密钥
        String key = getCacheKey(userType, username);
        Assert.isNotNull(key, "获取秘钥失败");
        try {
            return RsaUtils.privateDecrypt(passwd, RsaUtils.getPrivateKey(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取图片验证码
     *
     * @param userType
     * @param username
     * @return
     */
    public String buildCaptcha(String userType, String username) {
        Assert.isNotEmpty(userType, "userType不能为空");
        Assert.isNotEmpty(username, "username不能为空");
        // 生成验证码
        CaptchaUtils.Info captchaInfo = CaptchaUtils.gen();
        //截取结果
        BufferedImage image = captchaProducer.createImage(captchaInfo.text());
        CacheUtils.set(CacheKeys.appendKey(CacheKeys.LOGIN_CODE_PREFIX, userType, username), captchaInfo.result(), 5L);
        // 转换流信息写出
        try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();) {
            ImageIO.write(image, "jpg", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取验证码
     *
     * @param userType
     * @param username
     * @return
     */
    public String getCaptcha(String userType, String username) {
        Assert.isNotEmpty(userType, "userType不能为空");
        Assert.isNotEmpty(username, "username不能为空");
        Object codeCache = CacheUtils.get(CacheKeys.appendKey(CacheKeys.LOGIN_CODE_PREFIX, userType, username));
        if (codeCache == null) {
            return "";
        }
        return codeCache.toString();
    }
}
