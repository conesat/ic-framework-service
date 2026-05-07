package cn.icframework.system.module.monitor.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DiskStatsReader {
    private static final Path LINUX_DISKSTATS = Path.of("/proc/diskstats");
    private static final Path LINUX_SYS_BLOCK = Path.of("/sys/block");
    private static final long LINUX_SECTOR_BYTES = 512L;
    private static final String OS_NAME = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);

    public DiskCounters read() {
        if (Files.exists(LINUX_DISKSTATS) && Files.isDirectory(LINUX_SYS_BLOCK)) {
            return readLinuxCounters();
        }
        if (OS_NAME.contains("mac") || OS_NAME.contains("darwin")) {
            return readMacosCounters();
        }
        return new DiskCounters(false, 0L, 0L, "unsupported");
    }

    private DiskCounters readLinuxCounters() {
        try {
            Set<String> devices = resolveLinuxDevices();
            if (devices.isEmpty()) {
                return new DiskCounters(false, 0L, 0L, "unavailable");
            }
            List<String> lines = Files.readAllLines(LINUX_DISKSTATS);
            long readBytes = 0L;
            long writeBytes = 0L;
            for (String line : lines) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length < 10) {
                    continue;
                }
                String name = parts[2];
                if (!devices.contains(name)) {
                    continue;
                }
                readBytes += parseLong(parts[5]) * LINUX_SECTOR_BYTES;
                writeBytes += parseLong(parts[9]) * LINUX_SECTOR_BYTES;
            }
            return new DiskCounters(true, readBytes, writeBytes, "proc-diskstats");
        } catch (IOException ignored) {
            return new DiskCounters(false, 0L, 0L, "unavailable");
        }
    }

    private DiskCounters readMacosCounters() {
        Process process = null;
        try {
            process = new ProcessBuilder("ioreg", "-r", "-c", "IOBlockStorageDriver", "-l").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                long readBytes = 0L;
                long writeBytes = 0L;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (!trimmed.contains("\"Statistics\" = {")) {
                        continue;
                    }
                    readBytes += extractNamedLong(trimmed, "\"Bytes (Read)\"=");
                    writeBytes += extractNamedLong(trimmed, "\"Bytes (Write)\"=");
                }
                int exitCode = process.waitFor();
                if (exitCode == 0 && (readBytes > 0L || writeBytes > 0L)) {
                    return new DiskCounters(true, readBytes, writeBytes, "ioreg-IOBlockStorageDriver");
                }
                return new DiskCounters(false, 0L, 0L, "ioreg-exit-" + exitCode);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return new DiskCounters(false, 0L, 0L, "unavailable");
        } catch (IOException ignored) {
            return new DiskCounters(false, 0L, 0L, "unavailable");
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private Set<String> resolveLinuxDevices() throws IOException {
        try (Stream<Path> stream = Files.list(LINUX_SYS_BLOCK)) {
            return stream
                    .map(path -> path.getFileName().toString())
                    .filter(this::includeLinuxDevice)
                    .collect(Collectors.toSet());
        }
    }

    private boolean includeLinuxDevice(String name) {
        return !(name.startsWith("loop")
                || name.startsWith("ram")
                || name.startsWith("fd")
                || name.startsWith("sr")
                || name.startsWith("dm-")
                || name.startsWith("md")
                || name.startsWith("zram"));
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private long extractNamedLong(String line, String key) {
        int start = line.indexOf(key);
        if (start < 0) {
            return 0L;
        }
        start += key.length();
        int end = line.indexOf(',', start);
        if (end < 0) {
            end = line.indexOf('}', start);
        }
        if (end < 0) {
            end = line.length();
        }
        return parseLong(line.substring(start, end).trim());
    }

    @Getter
    @AllArgsConstructor
    public static class DiskCounters {
        private final boolean available;
        private final long readBytes;
        private final long writeBytes;
        private final String source;
    }
}
