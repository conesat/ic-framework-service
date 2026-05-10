package cn.icframework.system.config;

import cn.icframework.system.module.sysfile.service.DisabledFileHelper;
import cn.icframework.system.module.sysfile.service.FileStorageStrategy;
import cn.icframework.system.module.ws.WebSocketRouteStore;
import cn.icframework.system.module.ws.WebSocketSessionRegistry;
import cn.icframework.system.module.ws.api.ApiWebSocket;
import cn.icframework.system.task.ClearSysFile;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

class SystemAutoConfigurationTest {

    private final WebApplicationContextRunner webContextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SystemWebAutoConfiguration.class
            ))
            .withBean(cn.icframework.auth.standard.IOnlineUserService.class, () -> Mockito.mock(cn.icframework.auth.standard.IOnlineUserService.class));

    private final ApplicationContextRunner jobContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SystemJobAutoConfiguration.class
            ));

    private final ApplicationContextRunner fileStorageContextRunner = new ApplicationContextRunner()
            .withUserConfiguration(FileStorageTestConfiguration.class);

    @Test
    void websocketBeansShouldBeDisabledWhenPropertyOff() {
        webContextRunner
                .withPropertyValues(
                        "ic.system.websocket.enabled=false"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(ApiWebSocket.class);
                    assertThat(context).doesNotHaveBean(WebSocketConfig.class);
                    assertThat(context).doesNotHaveBean(WebSocketRouteStore.class);
                    assertThat(context).doesNotHaveBean(WebSocketSessionRegistry.class);
                });
    }

    @Test
    void websocketBeansShouldBeProvidedByAutoConfiguration() {
        webContextRunner
                .run(context -> {
                    assertThat(context).hasSingleBean(ApiWebSocket.class);
                    assertThat(context).hasSingleBean(WebSocketConfig.class);
                    assertThat(context).hasSingleBean(WebSocketRouteStore.class);
                    assertThat(context).hasSingleBean(WebSocketSessionRegistry.class);
                });
    }

    @Test
    void jobBeansShouldBeDisabledWhenPropertyOff() {
        jobContextRunner
                .withPropertyValues(
                        "ic.system.jobs.enabled=false"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(ClearSysFile.class);
                });
    }

    @Test
    void fileStorageShouldFallbackToDisabledHelperWhenPropertyOff() {
        fileStorageContextRunner
                .withPropertyValues(
                        "ic.system.file-storage.enabled=false"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(FileStorageStrategy.class);
                    FileStorageStrategy strategy = context.getBean(FileStorageStrategy.class);
                    assertThat(strategy.getFileHelper()).isInstanceOf(DisabledFileHelper.class);
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class FileStorageTestConfiguration {

        @Bean
        FileStorageConfig fileStorageConfig() {
            return new FileStorageConfig();
        }

        @Bean
        DisabledFileHelper disabledFileHelper() {
            return new DisabledFileHelper();
        }

        @Bean
        FileStorageStrategy fileStorageStrategy(FileStorageConfig fileStorageConfig,
                                                ObjectProvider<cn.icframework.system.module.sysfile.service.OssFileHelper> ossFileHelper,
                                                ObjectProvider<cn.icframework.system.module.sysfile.service.MinioFileHelper> minioFileHelper,
                                                DisabledFileHelper disabledFileHelper,
                                                Environment environment) {
            return new FileStorageStrategy(
                    fileStorageConfig,
                    ossFileHelper,
                    minioFileHelper,
                    disabledFileHelper,
                    environment.getProperty("ic.system.file-storage.enabled", Boolean.class, true)
            );
        }
    }
}
