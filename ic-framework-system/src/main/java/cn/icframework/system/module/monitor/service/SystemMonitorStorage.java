package cn.icframework.system.module.monitor.service;

import cn.icframework.system.module.monitor.config.SystemMonitorProperties;
import cn.icframework.system.module.monitor.model.MonitorSnapshot;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Component
public class SystemMonitorStorage {
    private final Deque<MonitorSnapshot> snapshots = new ArrayDeque<>();
    private final int maxSamples;

    public SystemMonitorStorage(SystemMonitorProperties properties) {
        this.maxSamples = properties.getMaxSamples();
    }

    public synchronized void append(MonitorSnapshot snapshot) {
        snapshots.addLast(snapshot);
        while (snapshots.size() > maxSamples) {
            snapshots.removeFirst();
        }
    }

    public synchronized MonitorSnapshot latest() {
        return snapshots.peekLast();
    }

    public synchronized List<MonitorSnapshot> range(long minTimestamp) {
        List<MonitorSnapshot> result = new ArrayList<>();
        for (MonitorSnapshot snapshot : snapshots) {
            if (snapshot.getTimestamp() >= minTimestamp) {
                result.add(snapshot);
            }
        }
        return result;
    }
}
