package cn.icframework.system.consts;

/**
 * @author iceFire
 */
public interface CacheKeys {
    /**
     * 管理登录密钥前缀
     */
    String LOGIN_KEY_PREFIX = "login_key";
    /**
     * 验证码前缀
     */
    String LOGIN_CODE_PREFIX = "login_code";
    /**
     * ws在线用户前缀
     */
    String WS_ONLINE_USER = "ws_online_user";

    /**
     * 拼接密钥
     * @return key[0] + ":" + key[1] + ":" + key[2]
     */
    static String appendKey(String... key) {
        return String.join(":", key);
    }
}
