package cn.icframework.system.utils;

import java.util.Random;

/**
 * @author iceFire
 * @date 2023/6/3
 */
public class CaptchaUtils {

    public static Info gen() {
        int result;//结果
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder text = new StringBuilder();
        int op = random.nextInt(4);
        //判断结果生成加减乘除
        switch (op) {
            case 0 -> {
                result = x * y;
                text.append(x);
                text.append("*");
                text.append(y);
            }
            case 1 -> {
                if (!(x == 0) && y % x == 0) {
                    result = y / x;
                    text.append(y);
                    text.append("/");
                    text.append(x);
                } else {
                    result = x + y;
                    text.append(x);
                    text.append("+");
                    text.append(y);
                }
            }
            case 2 -> {
                if (x >= y) {
                    result = x - y;
                    text.append(x);
                    text.append("-");
                    text.append(y);
                } else {
                    result = y - x;
                    text.append(y);
                    text.append("-");
                    text.append(x);
                }
            }
            default -> {
                result = x + y;
                text.append(x);
                text.append("+");
                text.append(y);
            }
        }
        return new Info(text.toString(), result);
    }

    public record Info(String text, int result) {
    }
}
