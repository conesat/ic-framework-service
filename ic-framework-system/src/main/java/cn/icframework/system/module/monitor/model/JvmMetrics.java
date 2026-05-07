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
public class JvmMetrics {
    private int threadCount;
    private int daemonThreadCount;
    private long peakThreadCount;
    private long loadedClassCount;
    private long totalLoadedClassCount;
    private long unloadedClassCount;
    private long gcCount;
    private long gcTimeMillis;
    private long uptimeMillis;
}
