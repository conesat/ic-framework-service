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
public class NetworkMetrics {
    private boolean available;
    private long rxBytes;
    private long txBytes;
    private double rxBytesPerSecond;
    private double txBytesPerSecond;
    private String source;
}
