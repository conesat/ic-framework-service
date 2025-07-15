package cn.icframework.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hzl
 * @date 2025/7/11
 */
@SpringBootApplication
@MapperScan({"cn.icframework.test.*.dao"})
public class TestApplication {
}
