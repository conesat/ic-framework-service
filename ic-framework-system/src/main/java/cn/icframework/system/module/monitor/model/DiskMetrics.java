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
public class DiskMetrics {
    private long totalBytes;
    private long freeBytes;
    private long usableBytes;
    private long usedBytes;
    private double usage;
    private boolean ioAvailable;
    private long readBytes;
    private long writeBytes;
    private double readBytesPerSecond;
    private double writeBytesPerSecond;
    private String source;
    @Builder.Default
    private List<DiskPathMetrics> items = new ArrayList<>();
}
