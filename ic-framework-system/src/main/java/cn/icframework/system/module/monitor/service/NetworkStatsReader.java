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

@Component
public class NetworkStatsReader {
    private static final Path LINUX_NET_DEV = Path.of("/proc/net/dev");
    private static final String OS_NAME = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);

    public NetworkCounters read() {
        if (Files.exists(LINUX_NET_DEV)) {
            return readLinuxCounters();
        }
        if (OS_NAME.contains("mac") || OS_NAME.contains("darwin")) {
            return readMacosCounters();
        }
        return new NetworkCounters(false, 0L, 0L, "unsupported");
    }

    private NetworkCounters readLinuxCounters() {
        try {
            List<String> lines = Files.readAllLines(LINUX_NET_DEV);
            long rxBytes = 0L;
            long txBytes = 0L;
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.contains(":")) {
                    continue;
                }
                String[] pair = trimmed.split(":", 2);
                String name = pair[0].trim();
                if ("lo".equals(name)) {
                    continue;
                }
                String[] values = pair[1].trim().split("\\s+");
                if (values.length < 16) {
                    continue;
                }
                rxBytes += parseLong(values[0]);
                txBytes += parseLong(values[8]);
            }
            return new NetworkCounters(true, rxBytes, txBytes, "proc-net-dev");
        } catch (IOException ignored) {
            return new NetworkCounters(false, 0L, 0L, "unavailable");
        }
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private NetworkCounters readMacosCounters() {
        Process process = null;
        long rxBytes = 0L;
        long txBytes = 0L;
        try {
            process = new ProcessBuilder("netstat", "-ib").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("Name") || trimmed.startsWith("Kernel")) {
                        continue;
                    }
                    String[] parts = trimmed.split("\\s+");
                    if (parts.length < 10) {
                        continue;
                    }
                    String name = parts[0];
                    if (skipInterface(name)) {
                        continue;
                    }
                    long inbound = parseLong(parts[6]);
                    long outbound = parseLong(parts[9]);
                    if (inbound == 0L && outbound == 0L) {
                        continue;
                    }
                    rxBytes += inbound;
                    txBytes += outbound;
                }
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return new NetworkCounters(true, rxBytes, txBytes, "netstat-ib");
            }
            return new NetworkCounters(false, 0L, 0L, "netstat-exit-" + exitCode);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return new NetworkCounters(false, 0L, 0L, "unavailable");
        } catch (IOException ignored) {
            return new NetworkCounters(false, 0L, 0L, "unavailable");
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private boolean skipInterface(String name) {
        return name.startsWith("lo")
                || name.startsWith("gif")
                || name.startsWith("stf")
                || name.startsWith("awdl")
                || name.startsWith("llw")
                || name.startsWith("anpi");
    }

    @Getter
    @AllArgsConstructor
    public static class NetworkCounters {
        private final boolean available;
        private final long rxBytes;
        private final long txBytes;
        private final String source;
    }
}
