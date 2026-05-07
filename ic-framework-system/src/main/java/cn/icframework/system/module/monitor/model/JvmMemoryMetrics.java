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
public class JvmMemoryMetrics {
    private long heapUsedBytes;
    private long heapCommittedBytes;
    private long heapMaxBytes;
    private double heapUsage;
    private long nonHeapUsedBytes;
    private long nonHeapCommittedBytes;
    private long nonHeapMaxBytes;
}
