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
public class MonitorSnapshot {
    private long timestamp;
    private CpuMetrics cpu;
    private SystemMemoryMetrics systemMemory;
    private JvmMemoryMetrics jvmMemory;
    private DiskMetrics disk;
    private NetworkMetrics network;
    private JvmMetrics jvm;
}
