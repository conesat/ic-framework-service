package cn.icframework.system.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * @author iceFire
 * @since 2023/6/3
 */
@Component
public class CaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        // 文本颜色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // 图片宽度
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "100");
        // 图片高度
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "30");
        // 文本字符大小
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "28");
        // 文本字符间距
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 文本字符长度
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 文本字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 干扰颜色
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
