package cn.icframework.system.module.monitor.service;

import cn.icframework.system.module.monitor.config.SystemMonitorProperties;
import cn.icframework.system.module.monitor.model.MonitorMetaResponse;
import cn.icframework.system.module.monitor.model.MonitorTimelineResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SystemMonitorService {
    private final SystemMonitorStorage storage;
    private final SystemMonitorProperties properties;

    public SystemMonitorService(SystemMonitorStorage storage, SystemMonitorProperties properties) {
        this.storage = storage;
        this.properties = properties;
    }

    public MonitorTimelineResponse timeline(int minutes) {
        if (!properties.isEnabled()) {
            return disabledTimeline();
        }
        Assert.isTrue(minutes > 0, "minutes must be greater than 0");
        int safeMinutes = Math.min(minutes, properties.getEffectiveRetentionMinutes());
        long now = System.currentTimeMillis();
        long minTimestamp = now - safeMinutes * 60_000L;
        return MonitorTimelineResponse.builder()
                .generatedAt(now)
                .minutes(safeMinutes)
                .retentionMinutes(properties.getEffectiveRetentionMinutes())
                .sampleIntervalSeconds(properties.getSampleIntervalSeconds())
                .latest(storage.latest())
                .samples(storage.range(minTimestamp))
                .build();
    }

    public MonitorMetaResponse meta() {
        return MonitorMetaResponse.builder()
                .enabled(properties.isEnabled())
                .sampleIntervalSeconds(properties.getSampleIntervalSeconds())
                .retentionMinutes(properties.getEffectiveRetentionMinutes())
                .diskDetailEnabled(properties.isDiskDetailEnabled())
                .build();
    }

    private MonitorTimelineResponse disabledTimeline() {
        return MonitorTimelineResponse.builder()
                .generatedAt(System.currentTimeMillis())
                .minutes(0)
                .retentionMinutes(properties.getEffectiveRetentionMinutes())
                .sampleIntervalSeconds(properties.getSampleIntervalSeconds())
                .latest(null)
                .samples(java.util.List.of())
                .build();
    }
}
