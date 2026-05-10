<template>
  <div class="ops-monitor-page" :style="pageStyleVars">
    <t-card class="hero-panel" :bordered="false">
      <div class="hero-toolbar">
        <div class="hero-title-group">
          <div class="hero-title">
            <t-icon name="server" size="22px" />
            <span>资源监控</span>
          </div>
          <div class="hero-subtitle">
            <template v-if="monitorMeta.enabled">
              最近 {{ timeline.minutes }} 分钟 · {{ timeline.sampleIntervalSeconds || monitorMeta.sampleIntervalSeconds || 5
              }} 秒采样 ·
              {{ latest ? formatDateTime(latest.timestamp) : '等待首批数据' }}
            </template>
            <template v-else>
              当前环境已关闭资源监控采集
            </template>
          </div>
        </div>
        <div v-if="monitorMeta.enabled" class="hero-inline-stats">
          <div class="hero-stat">
            <span class="hero-stat-label">CPU</span>
            <span class="hero-stat-value">{{ latest ? toPercent(latest.cpu.systemUsage) : '--' }}</span>
          </div>
          <div class="hero-stat">
            <span class="hero-stat-label">内存</span>
            <span class="hero-stat-value">{{ latest ? toPercent(latest.systemMemory.usage) : '--' }}</span>
          </div>
          <div class="hero-stat">
            <span class="hero-stat-label">磁盘</span>
            <span class="hero-stat-value">{{ latest ? toPercent(latest.disk.usage) : '--' }}</span>
          </div>
          <div class="hero-stat">
            <span class="hero-stat-label">采样</span>
            <span class="hero-stat-value">{{ timeline.samples.length }}</span>
          </div>
        </div>
        <div class="hero-actions">
          <div v-if="monitorMeta.enabled" class="range-switch">
            <t-button v-for="item in ranges" :key="item.value"
              :theme="selectedMinutes === item.value ? 'primary' : 'default'" variant="outline" size="small"
              @click="changeRange(item.value)">
              {{ item.label }}
            </t-button>
          </div>
          <t-button class="refresh-button" theme="default" variant="outline" size="small" :loading="loading"
            @click="refreshPageData">
            刷新
          </t-button>
        </div>
      </div>
    </t-card>

    <div v-if="monitorMeta.enabled" class="summary-grid">
      <t-card v-for="item in summaryCards" :key="item.key" class="summary-card" :class="`summary-card--${item.theme}`"
        :bordered="false">
        <div class="summary-top">
          <span class="summary-label">{{ item.label }}</span>
          <t-tag size="small" variant="light" :theme="item.theme">{{ item.tag }}</t-tag>
        </div>
        <div class="summary-value">{{ item.value }}</div>
        <div class="summary-meta">{{ item.meta }}</div>
        <div class="summary-progress">
          <span :style="{ width: `${item.progress}%` }"></span>
        </div>
      </t-card>
    </div>

    <div v-else class="disabled-panel">
      <t-icon name="chart" size="48px" />
      <div class="disabled-title">资源监控未启用</div>
      <div class="disabled-text">
        可在配置中设置 <code>ic.system.monitor.enabled: true</code> 后启用采集和看板展示。
      </div>
    </div>

    <div v-if="monitorMeta.enabled" class="chart-grid">
      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">CPU 使用率</div>
            <div class="panel-subtitle">{{ cpuPanelMeta }}</div>
          </div>
        </template>
        <v-chart :key="`${chartRenderKey}-cpu`" class="chart" :option="cpuChartOption"
          :update-options="chartUpdateOptions" autoresize />
      </t-card>
      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">内存使用率</div>
            <div class="panel-subtitle">{{ memoryPanelMeta }}</div>
          </div>
        </template>
        <v-chart :key="`${chartRenderKey}-memory`" class="chart" :option="memoryChartOption"
          :update-options="chartUpdateOptions" autoresize />
      </t-card>
      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">磁盘使用趋势</div>
            <div class="panel-subtitle">{{ diskChartMeta }}</div>
          </div>
        </template>
        <v-chart :key="`${chartRenderKey}-disk`" class="chart" :option="diskChartOption"
          :update-options="chartUpdateOptions" autoresize />
      </t-card>
      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">网络吞吐</div>
            <div class="panel-subtitle">{{ networkPanelMeta }}</div>
          </div>
        </template>
        <v-chart :key="`${chartRenderKey}-network`" class="chart" :option="networkChartOption"
          :update-options="chartUpdateOptions" autoresize />
      </t-card>
    </div>

    <div v-if="monitorMeta.enabled" class="detail-grid">
      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">JVM 运行状态</div>
            <div class="panel-subtitle">{{ jvmPanelMeta }}</div>
          </div>
        </template>
        <div class="jvm-panel">
          <div class="metric-row">
            <span>堆内存</span>
            <div class="metric-main">
              <t-progress :percentage="formatPercentageValue(latest?.jvmMemory?.heapUsage)" color="#0052d9" />
            </div>
            <span class="metric-value">
              {{ bytes(latest?.jvmMemory?.heapUsedBytes) }} / {{ bytes(heapLimitBytes) }}
            </span>
          </div>
          <div class="metric-row">
            <span>非堆内存</span>
            <div class="metric-main">
              <t-progress :percentage="formatPercentageValue(nonHeapUsage)" color="#00a870" />
            </div>
            <span class="metric-value">
              {{ bytes(latest?.jvmMemory?.nonHeapUsedBytes) }} / {{ bytes(latest?.jvmMemory?.nonHeapCommittedBytes) }}
            </span>
          </div>
          <div class="metric-stats">
            <div class="metric-stat">
              <div class="metric-stat-label">线程数</div>
              <div class="metric-stat-value">{{ formatCount(latest?.jvm?.threadCount) }}</div>
            </div>
            <div class="metric-stat">
              <div class="metric-stat-label">守护线程</div>
              <div class="metric-stat-value">{{ formatCount(latest?.jvm?.daemonThreadCount) }}</div>
            </div>
            <div class="metric-stat">
              <div class="metric-stat-label">GC 次数</div>
              <div class="metric-stat-value">{{ formatCount(latest?.jvm?.gcCount) }}</div>
            </div>
            <div class="metric-stat">
              <div class="metric-stat-label">GC 耗时</div>
              <div class="metric-stat-value">{{ formatDuration(latest?.jvm?.gcTimeMillis) }}</div>
            </div>
            <div class="metric-stat">
              <div class="metric-stat-label">已加载类</div>
              <div class="metric-stat-value">{{ formatCount(latest?.jvm?.loadedClassCount) }}</div>
            </div>
            <div class="metric-stat">
              <div class="metric-stat-label">运行时长</div>
              <div class="metric-stat-value">{{ formatDuration(latest?.jvm?.uptimeMillis) }}</div>
            </div>
          </div>
        </div>
      </t-card>

      <t-card class="monitor-card" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div class="panel-title">磁盘明细</div>
            <div class="panel-subtitle">{{ diskPanelMeta }}</div>
          </div>
        </template>
        <div class="disk-table">
          <div class="disk-row disk-head">
            <span>路径</span>
            <span>已用</span>
            <span>可用</span>
            <span>总量</span>
            <span>使用率</span>
          </div>
          <div v-if="diskRows.length === 0" class="disk-empty">{{ diskEmptyText }}</div>
          <div v-for="item in diskRows" :key="item.path" class="disk-row">
            <span class="disk-path">{{ item.path }}</span>
            <div class="disk-cell">
              <span class="disk-cell-label">已用</span>
              <span>{{ bytes(item.usedBytes) }}</span>
            </div>
            <div class="disk-cell">
              <span class="disk-cell-label">可用</span>
              <span>{{ bytes(item.usableBytes) }}</span>
            </div>
            <div class="disk-cell">
              <span class="disk-cell-label">总量</span>
              <span>{{ bytes(item.totalBytes) }}</span>
            </div>
            <div class="disk-cell">
              <span class="disk-cell-label">使用率</span>
              <span>{{ toPercent(item.usage) }}</span>
            </div>
          </div>
        </div>
      </t-card>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: 'DashboardOps',
};
</script>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue';
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { LineChart } from 'echarts/charts';
import { DataZoomComponent, GridComponent, LegendComponent, TimelineComponent, TitleComponent, TooltipComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { MessagePlugin } from 'tdesign-vue-next';

import { request } from '@/utils/request';
import { themeManager } from '@/utils/echarts-themes';

use([CanvasRenderer, LineChart, GridComponent, LegendComponent, TooltipComponent, TitleComponent, DataZoomComponent, TimelineComponent]);

interface CpuMetrics {
  systemUsage: number;
  processUsage: number;
  systemLoadAverage: number;
  availableProcessors: number;
}

interface SystemMemoryMetrics {
  totalBytes: number;
  freeBytes: number;
  usedBytes: number;
  usage: number;
}

interface JvmMemoryMetrics {
  heapUsedBytes: number;
  heapCommittedBytes: number;
  heapMaxBytes: number;
  heapUsage: number;
  nonHeapUsedBytes: number;
  nonHeapCommittedBytes: number;
  nonHeapMaxBytes: number;
}

interface DiskPathMetrics {
  path: string;
  totalBytes: number;
  freeBytes: number;
  usableBytes: number;
  usedBytes: number;
  usage: number;
}

interface DiskMetrics {
  totalBytes: number;
  freeBytes: number;
  usableBytes: number;
  usedBytes: number;
  usage: number;
  ioAvailable: boolean;
  readBytes: number;
  writeBytes: number;
  readBytesPerSecond: number;
  writeBytesPerSecond: number;
  source: string;
  items: DiskPathMetrics[];
}

interface NetworkMetrics {
  available: boolean;
  rxBytes: number;
  txBytes: number;
  rxBytesPerSecond: number;
  txBytesPerSecond: number;
  source: string;
}

interface JvmMetrics {
  threadCount: number;
  daemonThreadCount: number;
  peakThreadCount: number;
  loadedClassCount: number;
  totalLoadedClassCount: number;
  unloadedClassCount: number;
  gcCount: number;
  gcTimeMillis: number;
  uptimeMillis: number;
}

interface MonitorSnapshot {
  timestamp: number;
  cpu: CpuMetrics;
  systemMemory: SystemMemoryMetrics;
  jvmMemory: JvmMemoryMetrics;
  disk: DiskMetrics;
  network: NetworkMetrics;
  jvm: JvmMetrics;
}

interface MonitorTimelineResponse {
  generatedAt: number;
  minutes: number;
  retentionMinutes: number;
  sampleIntervalSeconds: number;
  latest: MonitorSnapshot | null;
  samples: MonitorSnapshot[];
}

interface ApiResponse<T> {
  code: number;
  msg?: string;
  data: T;
}

interface MonitorMetaResponse {
  enabled: boolean;
  sampleIntervalSeconds: number;
  retentionMinutes: number;
  diskDetailEnabled: boolean;
}

const ranges = [
  { label: '5 分钟', value: 5 },
  { label: '15 分钟', value: 15 },
  { label: '30 分钟', value: 30 },
  { label: '60 分钟', value: 60 },
];

const selectedMinutes = ref(15);
const loading = ref(false);
const monitorMeta = ref<MonitorMetaResponse>({
  enabled: true,
  sampleIntervalSeconds: 5,
  retentionMinutes: 60,
  diskDetailEnabled: true,
});
const timeline = ref<MonitorTimelineResponse>({
  generatedAt: 0,
  minutes: 15,
  retentionMinutes: 60,
  sampleIntervalSeconds: 5,
  latest: null,
  samples: [],
});
const themeConfig = ref(themeManager.getCurrentThemeConfig());
const currentTheme = ref(themeManager.getCurrentTheme());
const chartThemeVersion = ref(0);
const chartUpdateOptions = { notMerge: false, lazyUpdate: true };

let refreshTimer: number | undefined;
let themeObserver: MutationObserver | undefined;

const latest = computed(() => timeline.value.latest);
const chartRenderKey = computed(() => `${currentTheme.value}-${chartThemeVersion.value}`);
const chartLabels = computed(() => timeline.value.samples.map((item) => formatTime(item.timestamp)));
const chartPalette = computed(() => {
  const isDark = currentTheme.value === 'dark';
  return {
    isDark,
    title: isDark ? '#f3f6fb' : '#1f2329',
    text: isDark ? '#c7cfdb' : '#4f5661',
    muted: isDark ? '#8f99ab' : '#8a919f',
    axisLine: isDark ? 'rgb(255 255 255 / 10%)' : '#e5e7eb',
    splitLine: isDark ? 'rgb(255 255 255 / 8%)' : '#eef1f6',
    tooltipBackground: isDark ? '#202632' : '#fff',
    tooltipBorder: isDark ? 'rgb(255 255 255 / 10%)' : '#e5e7eb',
  };
});
const pageStyleVars = computed(() => ({
  '--ops-panel-title-color': chartPalette.value.title,
  '--ops-panel-subtitle-color': chartPalette.value.muted,
}));
const chartThemeConfig = computed(() => {
  const { dataZoom, timeline, ...rest } = themeConfig.value as Record<string, any>;
  return rest;
});
const heapLimitBytes = computed(() => {
  const jvmMemory = latest.value?.jvmMemory;
  if (!jvmMemory) return 0;
  return jvmMemory.heapMaxBytes > 0 ? jvmMemory.heapMaxBytes : jvmMemory.heapCommittedBytes;
});
const nonHeapUsage = computed(() => {
  const jvmMemory = latest.value?.jvmMemory;
  if (!jvmMemory || !jvmMemory.nonHeapCommittedBytes) return 0;
  return (jvmMemory.nonHeapUsedBytes * 100) / jvmMemory.nonHeapCommittedBytes;
});
const diskRows = computed(() => latest.value?.disk?.items || []);
const diskEmptyText = computed(() =>
  monitorMeta.value.diskDetailEnabled ? '暂无磁盘明细' : '当前环境未开启磁盘路径明细',
);

const summaryCards = computed(() => {
  const snapshot = latest.value;
  if (!snapshot) {
    return [
      buildCard('cpu', '系统 CPU', '-', '等待采样', 'default', '无数据', 0),
      buildCard('processCpu', '进程 CPU', '-', '等待采样', 'default', '无数据', 0),
      buildCard('memory', '系统内存', '-', '等待采样', 'default', '无数据', 0),
      buildCard('heap', 'JVM 堆', '-', '等待采样', 'default', '无数据', 0),
      buildCard('disk', '磁盘使用', '-', '等待采样', 'default', '无数据', 0),
      buildCard('network', '网络吞吐', '-', '等待采样', 'default', '无数据', 0),
    ];
  }

  return [
    buildCard(
      'cpu',
      '系统 CPU',
      toPercent(snapshot.cpu.systemUsage),
      `负载 ${snapshot.cpu.systemLoadAverage.toFixed(2)} · ${snapshot.cpu.availableProcessors} 核`,
      usageTheme(snapshot.cpu.systemUsage),
      usageTag(snapshot.cpu.systemUsage),
      clampPercent(snapshot.cpu.systemUsage),
    ),
    buildCard(
      'processCpu',
      '进程 CPU',
      toPercent(snapshot.cpu.processUsage),
      '当前 Java 进程占用',
      usageTheme(snapshot.cpu.processUsage),
      usageTag(snapshot.cpu.processUsage),
      clampPercent(snapshot.cpu.processUsage),
    ),
    buildCard(
      'memory',
      '系统内存',
      toPercent(snapshot.systemMemory.usage),
      `${bytes(snapshot.systemMemory.usedBytes)} / ${bytes(snapshot.systemMemory.totalBytes)}`,
      usageTheme(snapshot.systemMemory.usage),
      usageTag(snapshot.systemMemory.usage),
      clampPercent(snapshot.systemMemory.usage),
    ),
    buildCard(
      'heap',
      'JVM 堆',
      toPercent(snapshot.jvmMemory.heapUsage),
      `${bytes(snapshot.jvmMemory.heapUsedBytes)} / ${bytes(heapLimitBytes.value)}`,
      usageTheme(snapshot.jvmMemory.heapUsage),
      usageTag(snapshot.jvmMemory.heapUsage),
      clampPercent(snapshot.jvmMemory.heapUsage),
    ),
    buildCard(
      'disk',
      '磁盘使用',
      toPercent(snapshot.disk.usage),
      `${bytes(snapshot.disk.usableBytes)} 可用`,
      usageTheme(snapshot.disk.usage),
      usageTag(snapshot.disk.usage),
      clampPercent(snapshot.disk.usage),
    ),
    buildCard(
      'network',
      '网络吞吐',
      `${bytes(snapshot.network.rxBytesPerSecond)}/s`,
      `出站 ${bytes(snapshot.network.txBytesPerSecond)}/s${snapshot.network.source ? ` · ${snapshot.network.source}` : ''}`,
      snapshot.network.available ? 'primary' : 'warning',
      snapshot.network.available ? '在线' : '未知',
      snapshot.network.available ? 100 : 36,
    ),
  ];
});

const cpuPanelMeta = computed(() => {
  if (!latest.value) return '展示系统与当前进程的 CPU 波动';
  return `系统 ${toPercent(latest.value.cpu.systemUsage)} · 进程 ${toPercent(latest.value.cpu.processUsage)} · 线程 ${formatCount(latest.value.jvm.threadCount)}`;
});

const memoryPanelMeta = computed(() => {
  if (!latest.value) return '对比系统内存与 JVM 堆占用';
  return `系统 ${bytes(latest.value.systemMemory.usedBytes)} / ${bytes(latest.value.systemMemory.totalBytes)} · JVM 堆 ${toPercent(latest.value.jvmMemory.heapUsage)}`;
});

const diskChartMeta = computed(() => {
  if (!latest.value) return '展示磁盘使用率与可用容量变化';
  if (!latest.value.disk.ioAvailable) {
    return `使用率 ${toPercent(latest.value.disk.usage)} · 可用 ${bytes(latest.value.disk.usableBytes)} · 暂未识别磁盘读写吞吐`;
  }
  return `使用率 ${toPercent(latest.value.disk.usage)} · 读 ${bytes(latest.value.disk.readBytesPerSecond)}/s · 写 ${bytes(latest.value.disk.writeBytesPerSecond)}/s`;
});

const networkPanelMeta = computed(() => {
  if (!latest.value) return '查看入站与出站吞吐趋势';
  if (!latest.value.network.available) return '当前环境暂未识别有效网络吞吐来源';
  return `入站 ${bytes(latest.value.network.rxBytesPerSecond)}/s · 出站 ${bytes(latest.value.network.txBytesPerSecond)}/s`;
});

const jvmPanelMeta = computed(() => {
  if (!latest.value) return '堆内存、非堆内存与运行时状态';
  return `堆 ${toPercent(latest.value.jvmMemory.heapUsage)} · GC ${formatCount(latest.value.jvm.gcCount)} 次 · 运行 ${formatDuration(latest.value.jvm.uptimeMillis)}`;
});

const diskPanelMeta = computed(() => {
  if (!latest.value) return '展示挂载路径容量与使用率';
  if (!monitorMeta.value.diskDetailEnabled) return '当前环境未开启磁盘路径明细';
  return `${diskRows.value.length} 个路径 · 总可用 ${bytes(latest.value.disk.usableBytes)}`;
});

const baseChartOption = computed(() => ({
  ...chartThemeConfig.value,
  backgroundColor: 'transparent',
  textStyle: {
    color: chartPalette.value.text,
  },
  legend: {
    top: 0,
    icon: 'circle',
    itemWidth: 10,
    itemHeight: 10,
    textStyle: {
      color: chartPalette.value.muted,
      fontSize: 12,
    },
  },
  tooltip: {
    trigger: 'axis',
    backgroundColor: chartPalette.value.tooltipBackground,
    borderColor: chartPalette.value.tooltipBorder,
    borderWidth: 1,
    textStyle: {
      color: chartPalette.value.title,
    },
    extraCssText: `box-shadow: 0 10px 24px ${chartPalette.value.isDark ? 'rgb(0 0 0 / 26%)' : 'rgb(15 23 42 / 10%)'}; border-radius: 10px;`,
  },
  grid: {
    left: 12,
    right: 12,
    top: 42,
    bottom: 12,
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    axisLine: {
      lineStyle: {
        color: chartPalette.value.axisLine,
      },
    },
    axisTick: {
      show: false,
    },
    axisLabel: {
      color: chartPalette.value.text,
      margin: 12,
    },
    splitLine: {
      show: false,
    },
  },
  yAxis: {
    type: 'value',
    axisLine: {
      show: false,
    },
    axisTick: {
      show: false,
    },
    axisLabel: {
      color: chartPalette.value.text,
      margin: 12,
    },
    splitLine: {
      lineStyle: {
        color: chartPalette.value.splitLine,
      },
    },
  },
}));

const cpuChartOption = computed(() => ({
  ...baseChartOption.value,
  xAxis: {
    ...baseChartOption.value.xAxis,
    data: chartLabels.value,
  },
  tooltip: {
    ...baseChartOption.value.tooltip,
    formatter: (params: any) => {
      const list = Array.isArray(params) ? params : [params];
      const timeLabel = list[0]?.axisValueLabel || '--';
      const lines = list.map((item: any) => {
        if (item.seriesName === '线程数') {
          return `${item.marker}${item.seriesName} ${formatCount(Number(item.value))}`;
        }
        return `${item.marker}${item.seriesName} ${Number(item.value).toFixed(1)}%`;
      });
      return [timeLabel, ...lines].join('<br/>');
    },
  },
  yAxis: [
    {
      ...baseChartOption.value.yAxis,
      min: 0,
      max: 100,
      axisLabel: {
        ...baseChartOption.value.yAxis.axisLabel,
        formatter: '{value}%',
      },
    },
    {
      ...baseChartOption.value.yAxis,
      axisLabel: {
        ...baseChartOption.value.yAxis.axisLabel,
        formatter: '{value}',
      },
    },
  ],
  series: [
    {
      name: '系统 CPU',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.cpu.systemUsage)),
    },
    {
      name: '进程 CPU',
      type: 'line',
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.cpu.processUsage)),
    },
    {
      name: '线程数',
      type: 'line',
      smooth: true,
      yAxisIndex: 1,
      showSymbol: false,
      data: timeline.value.samples.map((item) => item.jvm.threadCount),
    },
  ],
}));

const memoryChartOption = computed(() => ({
  ...baseChartOption.value,
  xAxis: {
    ...baseChartOption.value.xAxis,
    data: chartLabels.value,
  },
  yAxis: {
    ...baseChartOption.value.yAxis,
    min: 0,
    max: 100,
    axisLabel: {
      ...baseChartOption.value.yAxis.axisLabel,
      formatter: '{value}%',
    },
  },
  series: [
    {
      name: '系统内存',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.systemMemory.usage)),
    },
    {
      name: 'JVM 堆',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.jvmMemory.heapUsage)),
    },
  ],
}));

const diskChartOption = computed(() => ({
  ...baseChartOption.value,
  xAxis: {
    ...baseChartOption.value.xAxis,
    data: chartLabels.value,
  },
  tooltip: {
    ...baseChartOption.value.tooltip,
    formatter: (params: any) => {
      const list = Array.isArray(params) ? params : [params];
      const timeLabel = list[0]?.axisValueLabel || '--';
      const lines = list.map((item: any) => {
        if (item.seriesName === '读取吞吐' || item.seriesName === '写入吞吐') {
          return `${item.marker}${item.seriesName} ${bytes(Number(item.value))}`;
        }
        return `${item.marker}${item.seriesName} ${Number(item.value).toFixed(1)}%`;
      });
      return [timeLabel, ...lines].join('<br/>');
    },
  },
  yAxis: [
    {
      ...baseChartOption.value.yAxis,
      min: 0,
      max: 100,
      axisLabel: {
        ...baseChartOption.value.yAxis.axisLabel,
        formatter: '{value}%',
      },
    },
    latest.value?.disk?.ioAvailable
      ? {
        ...baseChartOption.value.yAxis,
        axisLabel: {
          ...baseChartOption.value.yAxis.axisLabel,
          formatter: (value: number) => bytes(value),
        },
      }
      : {
        ...baseChartOption.value.yAxis,
        show: false,
        splitLine: {
          show: false,
        },
      },
  ],
  series: [
    {
      name: '使用率',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.disk.usage)),
    },
    {
      name: '读取吞吐',
      type: 'line',
      smooth: true,
      yAxisIndex: 1,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.disk.readBytesPerSecond)),
    },
    {
      name: '写入吞吐',
      type: 'line',
      smooth: true,
      yAxisIndex: 1,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.disk.writeBytesPerSecond)),
    },
  ].filter((item) => item.name === '使用率' || latest.value?.disk?.ioAvailable),
}));

const networkChartOption = computed(() => ({
  ...baseChartOption.value,
  tooltip: {
    ...baseChartOption.value.tooltip,
    formatter: (params: any) => {
      const list = Array.isArray(params) ? params : [params];
      const timeLabel = list[0]?.axisValueLabel || '--';
      const lines = list.map((item: any) => `${item.marker}${item.seriesName} ${bytes(item.value)}/s`);
      return [timeLabel, ...lines].join('<br/>');
    },
  },
  xAxis: {
    ...baseChartOption.value.xAxis,
    data: chartLabels.value,
  },
  yAxis: {
    ...baseChartOption.value.yAxis,
    axisLabel: {
      ...baseChartOption.value.yAxis.axisLabel,
      formatter: (value: number) => bytes(value),
    },
  },
  series: [
    {
      name: '入站',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.network.rxBytesPerSecond)),
    },
    {
      name: '出站',
      type: 'line',
      smooth: true,
      showSymbol: false,
      data: timeline.value.samples.map((item) => roundMetricValue(item.network.txBytesPerSecond)),
    },
  ],
}));

function buildCard(
  key: string,
  label: string,
  value: string,
  meta: string,
  theme: 'default' | 'primary' | 'success' | 'warning' | 'danger',
  tag: string,
  progress: number,
) {
  return { key, label, value, meta, theme, tag, progress };
}

function clampPercent(value?: number) {
  return Math.max(0, Math.min(100, Number(value || 0)));
}

function usageTheme(value = 0) {
  if (value >= 90) return 'danger';
  if (value >= 75) return 'warning';
  if (value >= 50) return 'primary';
  return 'success';
}

function usageTag(value = 0) {
  if (value >= 90) return '高压';
  if (value >= 75) return '偏高';
  if (value >= 50) return '正常';
  return '平稳';
}

function formatTime(timestamp?: number) {
  if (!timestamp) return '--:--:--';
  const date = new Date(timestamp);
  return date.toLocaleTimeString('zh-CN', { hour12: false });
}

function formatDateTime(timestamp?: number) {
  if (!timestamp) return '-';
  return new Date(timestamp).toLocaleString('zh-CN', { hour12: false });
}

function bytes(value?: number) {
  if (value === undefined || value === null) return '-';
  const normalized = Number(value);
  if (Number.isNaN(normalized) || normalized < 0) return '-';
  if (normalized === 0) return '0 B';
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let size = normalized;
  let unitIndex = 0;
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex += 1;
  }
  const digits = size >= 100 || unitIndex === 0 ? 0 : 1;
  return `${size.toFixed(digits)} ${units[unitIndex]}`;
}

function toPercent(value?: number) {
  return `${Number(value || 0).toFixed(1)}%`;
}

function formatPercentageValue(value?: number) {
  return Number(Number(value || 0).toFixed(1));
}

function roundMetricValue(value?: number, digits = 2) {
  return Number(Number(value || 0).toFixed(digits));
}

function formatDuration(value?: number) {
  const total = Math.floor((value || 0) / 1000);
  const hours = Math.floor(total / 3600);
  const minutes = Math.floor((total % 3600) / 60);
  const seconds = total % 60;
  if (hours > 0) return `${hours}h ${minutes}m`;
  if (minutes > 0) return `${minutes}m ${seconds}s`;
  return `${seconds}s`;
}

function formatCount(value?: number) {
  return Number(value || 0).toLocaleString('zh-CN');
}

async function loadMonitorData() {
  if (!monitorMeta.value.enabled) {
    timeline.value = {
      generatedAt: Date.now(),
      minutes: 0,
      retentionMinutes: monitorMeta.value.retentionMinutes,
      sampleIntervalSeconds: monitorMeta.value.sampleIntervalSeconds,
      latest: null,
      samples: [],
    };
    return;
  }
  loading.value = true;
  try {
    const res = await request.get<ApiResponse<MonitorTimelineResponse>>({
      url: '/sys/monitor/timeline',
      params: {
        minutes: selectedMinutes.value,
      },
    });
    if (res.code !== 0) {
      MessagePlugin.error(res.msg || '监控数据加载失败');
      return;
    }
    timeline.value = res.data;
  } catch (error) {
    console.error(error);
    MessagePlugin.error('监控数据加载失败');
  } finally {
    loading.value = false;
  }
}

async function loadMonitorMeta() {
  const res = await request.get<ApiResponse<MonitorMetaResponse>>({
    url: '/sys/monitor/meta',
  });
  if (res.code !== 0) {
    throw new Error(res.msg || '监控状态加载失败');
  }
  monitorMeta.value = res.data;
}

async function refreshPageData() {
  loading.value = true;
  try {
    await loadMonitorMeta();
    await loadMonitorData();
  } catch (error) {
    console.error(error);
    MessagePlugin.error('监控状态加载失败');
  } finally {
    loading.value = false;
  }
}

function changeRange(minutes: number) {
  if (selectedMinutes.value === minutes) return;
  selectedMinutes.value = minutes;
  loadMonitorData();
}

function updateTheme() {
  currentTheme.value = themeManager.getCurrentTheme();
  themeConfig.value = themeManager.getCurrentThemeConfig();
  chartThemeVersion.value += 1;
  requestAnimationFrame(() => window.dispatchEvent(new Event('resize')));
}

onMounted(() => {
  refreshPageData();
  refreshTimer = window.setInterval(refreshPageData, 5000);
  themeObserver = new MutationObserver(updateTheme);
  themeObserver.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['theme-mode'],
  });
  window.addEventListener('echarts-theme-change', updateTheme);
});

onUnmounted(() => {
  if (refreshTimer) {
    window.clearInterval(refreshTimer);
  }
  themeObserver?.disconnect();
  window.removeEventListener('echarts-theme-change', updateTheme);
});
</script>

<style lang="less" scoped>
.ops-monitor-page {
  --ops-panel-title-color: var(--td-text-color-primary);
  --ops-panel-subtitle-color: var(--td-text-color-secondary);

  display: flex;
  flex-direction: column;
  width: 100%;
  min-width: 0;
  gap: 16px;
}

.ops-monitor-page :deep(.t-card) {
  border: 1px solid var(--td-component-stroke);
  box-shadow: none;
}

.hero-panel,
.summary-card,
.monitor-card {
  :deep(.t-card__header) {
    padding-bottom: 0;
  }
}

.hero-panel {
  position: relative;
  overflow: hidden;
  padding: 6px 12px;
}

.hero-panel::after {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background: linear-gradient(90deg, transparent, color-mix(in srgb, var(--td-brand-color) 5%, transparent));
  opacity: 0.8;
}

.hero-toolbar {
  position: relative;
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.hero-title-group {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
  flex-wrap: wrap;
}

.hero-inline-stats {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  flex: 1 1 240px;
}

.hero-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font: var(--td-font-title-large);
  font-weight: 600;
  color: var(--td-text-color-primary);
}

.hero-title :deep(.t-icon) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: color-mix(in srgb, var(--td-brand-color) 14%, transparent);
  color: var(--td-brand-color);
}

.hero-subtitle {
  flex: 1 1 320px;
  min-width: 0;
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-medium);
  line-height: 1.4;
  white-space: nowrap;
}

.hero-stat {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  border-radius: 999px;
  background: color-mix(in srgb, var(--td-bg-color-container-hover) 88%, transparent);
  border: 1px solid color-mix(in srgb, var(--td-brand-color) 8%, var(--td-component-stroke));
}

.hero-stat-label {
  color: var(--td-text-color-placeholder);
  font: var(--td-font-body-small);
}

.hero-stat-value {
  color: var(--td-text-color-primary);
  font: var(--td-font-body-medium);
  font-weight: 600;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: flex-end;
  flex-shrink: 0;
  margin-left: auto;
}

.range-switch {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.range-switch :deep(.t-button) {
  min-width: 72px;
}

.hero-actions :deep(.t-button) {
  backdrop-filter: blur(8px);
}

.refresh-button {
  width: 68px;
}

.refresh-button :deep(.t-loading) {
  width: 16px;
  min-width: 16px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 16px;
}

.disabled-panel {
  min-height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 14px;
  border: 1px dashed var(--td-component-stroke);
  border-radius: var(--td-radius-large);
  background: var(--td-bg-color-container);
  color: var(--td-text-color-secondary);
  text-align: center;
  padding: 32px 20px;
}

.disabled-title {
  color: var(--td-text-color-primary);
  font: var(--td-font-title-large);
  font-weight: 600;
}

.disabled-text {
  max-width: 480px;
  font: var(--td-font-body-medium);
  line-height: 1.7;
}

.disabled-text code {
  padding: 2px 6px;
  border-radius: 4px;
  background: var(--td-bg-color-secondarycontainer);
}

.summary-card {
  position: relative;
  overflow: hidden;
  min-height: 132px;
  background: var(--td-bg-color-container);
}

.summary-card--default {
  --summary-accent: var(--td-text-color-placeholder);
}

.summary-card--primary {
  --summary-accent: var(--td-brand-color);
}

.summary-card--success {
  --summary-accent: var(--td-success-color);
}

.summary-card--warning {
  --summary-accent: var(--td-warning-color);
}

.summary-card--danger {
  --summary-accent: var(--td-error-color);
}

.summary-top {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
}

.summary-label {
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-medium);
}

.summary-value {
  margin-top: 14px;
  font-size: 28px;
  line-height: 1.15;
  font-weight: 600;
  color: var(--td-text-color-primary);
}

.summary-meta {
  margin-top: 10px;
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-small);
  line-height: 1.6;
}

.summary-progress {
  margin-top: 16px;
  height: 5px;
  border-radius: 999px;
  overflow: hidden;
  background: color-mix(in srgb, var(--td-text-color-placeholder) 18%, transparent);
}

.summary-progress span {
  display: block;
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, color-mix(in srgb, var(--summary-accent) 70%, #fff), var(--summary-accent));
  transition: width 0.24s ease;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.panel-header {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.panel-title {
  color: var(--ops-panel-title-color);
  font: var(--td-font-title-medium);
  font-weight: 600;
}

.panel-subtitle {
  color: var(--ops-panel-subtitle-color);
  font: var(--td-font-body-small);
  line-height: 1.6;
}

.chart {
  height: 320px;
}

.chart :deep(canvas) {
  background: transparent !important;
}

.jvm-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.metric-row {
  display: grid;
  grid-template-columns: 92px minmax(0, 1fr) 180px;
  gap: 12px;
  align-items: center;
  padding: 12px 14px;
  border-radius: 12px;
  background: color-mix(in srgb, var(--td-bg-color-container-hover) 78%, transparent);
}

.metric-main {
  min-width: 0;
}

.metric-value {
  text-align: right;
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-small);
}

.metric-stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.metric-stat {
  border: 1px solid var(--td-component-stroke);
  border-radius: var(--td-radius-medium);
  padding: 12px;
}

.metric-stat-label {
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-small);
}

.metric-stat-value {
  margin-top: 8px;
  font-size: 22px;
  line-height: 1.2;
  font-weight: 600;
}

.disk-table {
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--td-component-stroke);
}

.disk-head {
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-small);
}

.disk-row {
  display: grid;
  grid-template-columns: minmax(180px, 1.6fr) repeat(4, minmax(88px, 1fr));
  gap: 12px;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid var(--td-component-stroke);
  font: var(--td-font-body-medium);
}

.disk-head .disk-cell-label {
  display: none;
}

.disk-path {
  word-break: break-all;
  color: var(--td-text-color-primary);
}

.disk-cell {
  display: contents;
}

.disk-cell-label {
  display: none;
}

.disk-empty {
  padding: 36px 0;
  text-align: center;
  color: var(--td-text-color-secondary);
}

@media (width <=1400px) {
  .summary-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (width <=1080px) {
  .hero-toolbar {
    display: grid;
    grid-template-columns: minmax(0, 1fr) auto;
    align-items: start;
    gap: 14px 16px;
  }

  .hero-title-group {
    grid-column: 1 / -1;
    gap: 10px 16px;
  }

  .hero-subtitle {
    white-space: normal;
  }

  .hero-inline-stats {
    grid-column: 1 / 2;
    align-self: start;
  }

  .hero-actions {
    grid-column: 2 / 3;
    align-self: start;
    margin-left: 0;
    flex-direction: column;
    align-items: stretch;
  }

  .range-switch {
    justify-content: flex-end;
  }

  .range-switch :deep(.t-button) {
    min-width: 64px;
  }

  .chart-grid,
  .detail-grid {
    grid-template-columns: 1fr;
  }

  .metric-stats {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (width <=768px) {
  .ops-monitor-page {
    gap: 12px;
  }

  .hero-toolbar {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .hero-title-group {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .hero-subtitle {
    white-space: normal;
  }

  .hero-inline-stats {
    flex: none;
    gap: 6px;
  }

  .hero-actions {
    margin-left: 0;
    width: 100%;
    flex-direction: column;
    align-items: stretch;
    justify-content: space-between;
  }

  .range-switch {
    justify-content: flex-start;
  }

  .range-switch :deep(.t-button) {
    flex: 1 1 calc(50% - 4px);
    min-width: 0;
  }

  .refresh-button {
    width: 100%;
  }

  .summary-grid {
    grid-template-columns: 1fr;
  }

  .summary-value {
    font-size: 24px;
  }

  .metric-row {
    grid-template-columns: 1fr;
  }

  .metric-value {
    text-align: left;
  }

  .metric-stats {
    grid-template-columns: 1fr;
  }

  .disk-row {
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 14px 0;
  }

  .disk-head {
    display: none;
  }

  .disk-cell {
    display: flex;
    justify-content: space-between;
    gap: 12px;
    align-items: baseline;
    color: var(--td-text-color-secondary);
  }

  .disk-cell-label {
    display: inline;
    color: var(--td-text-color-placeholder);
    font: var(--td-font-body-small);
  }

  .chart {
    height: 280px;
  }
}
</style>
