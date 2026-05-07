package cn.icframework.system.module.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorTimelineResponse {
    private long generatedAt;
    private int minutes;
    private int retentionMinutes;
    private int sampleIntervalSeconds;
    private MonitorSnapshot latest;
    @Builder.Default
    private List<MonitorSnapshot> samples = new ArrayList<>();
}
