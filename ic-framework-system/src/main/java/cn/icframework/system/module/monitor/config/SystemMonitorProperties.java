package cn.icframework.system.module.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "ic.system.monitor")
public class SystemMonitorProperties {
    private boolean enabled = true;
    private int sampleIntervalSeconds = 5;
    private int retentionMinutes = 60;
    private boolean diskDetailEnabled = true;
    private List<String> diskPaths = new ArrayList<>();

    public int getEffectiveRetentionMinutes() {
        return Math.max(retentionMinutes, 1);
    }

    public long getSampleIntervalMillis() {
        return Math.max(sampleIntervalSeconds, 1) * 1000L;
    }

    public int getMaxSamples() {
        int retentionSeconds = getEffectiveRetentionMinutes() * 60;
        return Math.max((retentionSeconds / Math.max(sampleIntervalSeconds, 1)) + 4, 16);
    }
}
