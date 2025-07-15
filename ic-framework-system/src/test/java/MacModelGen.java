import cn.icframework.gen.Generator;
import cn.icframework.system.module.noticereceiver.NoticeReceiver;
import org.junit.jupiter.api.Test;

/**
 * @author iceFire
 * @date 2023/6/5
 */
public class MacModelGen {
    String javaPath = "/Volumes/MacOsData/projects/ic-framework-serveice/ic-framework-system/src/main/java/";
    String apiVueSrcPath = "/Volumes/MacOsData/projects/ic-framework-serveice/_web/admin/src/";
    String vuePath = "/Volumes/MacOsData/projects/ic-framework-serveice/_web/admin/src";

    @Test
    public void gen(){
        Generator generator = new Generator();
        generator.author("ic");
        generator.moduleName("sys");
        generator.tableClass(NoticeReceiver.class);
        generator.javaPath(javaPath);
        generator.apiVueSrcPath(apiVueSrcPath);
        generator.vuePath(vuePath);
        generator.routerVueSrcPath(vuePath);
//        generator.modelName("permissionGroup");
//        generator.packageName("cn.icframework.system.module");
        generator.build();
    }
}
