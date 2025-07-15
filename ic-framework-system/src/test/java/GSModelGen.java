import cn.icframework.gen.Generator;
import cn.icframework.system.module.onlineuser.OnlineUser;
import org.junit.jupiter.api.Test;

/**
 * @author iceFire
 * @date 2023/6/5
 */
public class GSModelGen {
    String javaPath = "D:\\product\\java\\ic-framework-serveice\\ic-framework-system\\src\\main\\java\\";
    String apiVueSrcPath = "D:\\product\\java\\ic-framework-serveice\\_web\\admin\\src\\";
    String vuePath = "D:\\product\\java\\ic-framework-serveice\\_web\\admin\\src";

    @Test
    public void gen(){
        Generator generator = new Generator();
        generator.author("ic");
        generator.moduleName("sys");
        generator.tableClass(OnlineUser.class);
        generator.javaPath(javaPath);
        generator.apiVueSrcPath(apiVueSrcPath);
        generator.vuePath(vuePath);
        generator.routerVueSrcPath(vuePath);
//        generator.modelName("permissionGroup");
//        generator.packageName("cn.icframework.system.module");
        generator.build();
    }
}
