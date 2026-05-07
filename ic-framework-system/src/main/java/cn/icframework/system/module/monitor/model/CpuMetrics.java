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
public class CpuMetrics {
    private double systemUsage;
    private double processUsage;
    private double systemLoadAverage;
    private int availableProcessors;
}
