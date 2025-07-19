package cn.icframework.test;


import cn.icframework.gen.Generator;
import org.junit.jupiter.api.Test;

/**
 * @author iceFire
 * @since 2023/6/5
 */
public class TestGen {
    @Test
    public void genCode() {
        Generator generator = new Generator();
        // 作者
        generator.author("iceFire");
        // 指定模块名称
        generator.moduleName("dep");
        // 指定需要生成的实体类
//        generator.tableClass(cn.icframework.project.module.dep.Dept.class);
        // 指定生成的java文件路径
        generator.javaPath("D:\\product\\ic\\ic-framework-service\\ic-framework-project\\src\\main\\java");
        // 指定生成的vue文件路径
        generator.pageVueSrcPath("D:\\product\\ic\\ic-framework-service\\_web\\admin\\src");
        // 生成
        generator.build();
    }
}
