package cn.icframework.system.utils;

import cn.icframework.core.utils.Assert;

import java.security.SecureRandom;

/**
 *
 * @author hzl
 * @since 2024/9/11
 */
public class PasswordUtils {
    public static String generateRandomPassword(int length) {
        Assert.isTrue(length > 6, "密码长度不能小于6位");
        String cs = "!@#$%^&*.";
        String up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String low = "abcdefghijklmnopqrstuvwxyz";
        String num = "0123456789";
        SecureRandom random = new SecureRandom();
        char[] chars = new char[length];
        for (int i = 0; i < 1; i++) {
            int index = random.nextInt(cs.length());
            chars[i] = cs.charAt(index);
        }
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(up.length());
            chars[i + 1] = up.charAt(index);
        }
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(low.length());
            chars[i + 3] = low.charAt(index);
        }
        for (int i = 0; i < length - 5; i++) {
            int index = random.nextInt(num.length());
            chars[i + 5] = num.charAt(index);
        }

        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }

    public static void main(String[] args) {
        System.out.println(generateRandomPassword(8));
    }
}
