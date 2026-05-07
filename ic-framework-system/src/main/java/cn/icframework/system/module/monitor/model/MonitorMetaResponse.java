package cn.icframework.system.module.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorMetaResponse {
    private boolean enabled;
    private int sampleIntervalSeconds;
    private int retentionMinutes;
    private boolean diskDetailEnabled;
}
