<template>
  <div class="ops-dashboard">
    <!-- 头部导航 -->
    <t-card class="dashboard-header" :bordered="false">
      <div class="header-content">
        <div class="header-left">
          <div class="title">
            <t-icon name="server" size="24px" />
            <t-text variant="title" size="large">运维监控看板</t-text>
          </div>
          <t-tabs v-model="activeTab" class="nav-tabs">
            <t-tab-panel value="overview" label="系统概况" />
            <t-tab-panel value="service" label="服务监控" />
            <t-tab-panel value="alarm" label="告警信息" />
          </t-tabs>
        </div>
        <div class="header-right">
          <t-text variant="body" size="small">{{ currentTime }}</t-text>
          <div class="user-info">
            <t-icon name="user" size="16px" />
            <t-text variant="body" size="small">管理员</t-text>
          </div>
        </div>
      </div>
    </t-card>

    <!-- 主要内容区域 -->
    <div class="dashboard-content">
      <!-- 第一行 -->
      <div class="content-row">
        <!-- 平台信息 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">平台信息</t-text>
          </template>
          <div class="platform-grid">
            <t-card v-for="item in platformInfo" :key="item.key" class="platform-item" :bordered="false"
              :class="item.colorClass">
              <div class="item-content">
                <t-icon :name="item.icon" size="24px" />
                <t-text variant="body" size="small">{{ item.label }}</t-text>
                <t-text variant="title" size="large" class="value">{{ item.value }}</t-text>
              </div>
            </t-card>
          </div>
        </t-card>

        <!-- 服务器运行状态 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">服务器运行状态</t-text>
          </template>
          <div class="status-content">
            <div class="chart-container">
              <v-chart class="status-chart" :option="serverStatusOption" />
            </div>
            <div class="status-legend">
              <div v-for="item in serverStatusLegend" :key="item.name" class="legend-item">
                <t-tag :theme="item.theme" :variant="item.variant" size="small">
                  {{ item.name }} ({{ item.count }})
                </t-tag>
              </div>
            </div>
          </div>
        </t-card>

        <!-- 系统资源 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">系统资源</t-text>
          </template>
          <div class="resources-grid">
            <t-card v-for="item in systemResources" :key="item.key" class="resource-item" :bordered="false"
              :class="item.colorClass">
              <div class="item-content">
                <t-icon :name="item.icon" size="24px" />
                <t-text variant="body" size="small">{{ item.label }}</t-text>
                <t-text variant="title" size="large" class="value">{{ item.value }}</t-text>
              </div>
            </t-card>
          </div>
        </t-card>
      </div>

      <!-- 第二行 -->
      <div class="content-row">
        <!-- CPU使用率曲线 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">CPU使用率趋势</t-text>
          </template>
          <div class="chart-container">
            <v-chart class="chart" :option="cpuChartOption" />
          </div>
        </t-card>

        <!-- 内存使用率曲线 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">内存使用率趋势</t-text>
          </template>
          <div class="chart-container">
            <v-chart class="chart" :option="memoryChartOption" />
          </div>
        </t-card>
      </div>

      <!-- 第三行 -->
      <div class="content-row">
        <!-- JVM监控 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">JVM监控</t-text>
          </template>
          <div class="jvm-content">
            <div v-for="item in jvmMetrics" :key="item.key" class="metric-item">
              <t-text variant="body" size="small">{{ item.label }}</t-text>
              <t-progress class="progress-bar" :percentage="item.value" :color="item.color" />
              <t-text variant="body" size="small" class="value">{{ item.displayValue }}</t-text>
            </div>
          </div>
        </t-card>

        <!-- 数据库监控 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">数据库监控</t-text>
          </template>
          <div class="db-content">
            <div v-for="item in dbMetrics" :key="item.key" class="metric-item">
              <t-text variant="body" size="small">{{ item.label }}</t-text>
              <t-text variant="body" size="small" class="value">{{ item.value }}</t-text>
            </div>
          </div>
        </t-card>

        <!-- 缓存监控 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">缓存监控</t-text>
          </template>
          <div class="cache-content">
            <div v-for="item in cacheMetrics" :key="item.key" class="metric-item">
              <t-text variant="body" size="small">{{ item.label }}</t-text>
              <t-progress v-if="item.showProgress" class="progress-bar" :percentage="item.value" :color="item.color" />
              <t-text v-else variant="body" size="small" class="value">{{ item.value }}</t-text>
              <t-text variant="body" size="small" class="value">{{ item.displayValue }}</t-text>
            </div>
          </div>
        </t-card>
      </div>

      <!-- 第四行 -->
      <div class="content-row">
        <!-- 告警信息 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">告警信息</t-text>
          </template>
          <div class="alarm-content">
            <div class="alarm-stats">
              <div v-for="item in alarmStats" :key="item.key" class="stat-item">
                <div class="stat-content">
                  <t-icon :name="item.icon" size="24px" />
                  <t-text variant="body" size="small">{{ item.label }}</t-text>
                  <t-text variant="title" size="large" class="value">{{ item.value }}</t-text>
                </div>
              </div>
            </div>
            <t-table class="alarm-table" :data="alarmList" :columns="alarmColumns" :pagination="alarmPagination" />
          </div>
        </t-card>

        <!-- 服务健康度 -->
        <t-card class="dashboard-card" :bordered="false">
          <template #header>
            <t-text variant="title" size="medium">服务健康度</t-text>
          </template>
          <div class="health-content">
            <div class="health-chart">
              <v-chart class="health-chart-component" :option="healthChartOption" />
            </div>
            <div class="service-list">
              <t-tag v-for="service in serviceHealth" :key="service.name" :theme="service.theme"
                :variant="service.variant" class="service-item">
                {{ service.name }}: {{ service.status }}
              </t-tag>
            </div>
          </div>
        </t-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, nextTick } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from 'echarts/components'
import { themeManager } from '@/utils/echarts-themes'

use([
  CanvasRenderer,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
])

const currentTime = ref('')
const activeTab = ref('overview')

// 主题状态
const isDarkTheme = ref(false)

// 响应式主题配置
const currentThemeConfig = ref(themeManager.getCurrentThemeConfig())

// 监听主题变化
const watchThemeChange = () => {
  const observer = new MutationObserver(() => {
    const html = document.documentElement
    const newTheme = html.getAttribute('theme-mode') === 'dark'
    console.log('页面检测到主题变化:', newTheme ? 'dark' : 'light')
    if (newTheme !== isDarkTheme.value) {
      isDarkTheme.value = newTheme
      // 更新主题配置
      currentThemeConfig.value = themeManager.getCurrentThemeConfig()
      console.log('更新主题配置:', currentThemeConfig.value)
      // 强制触发图表重新渲染
      nextTick(() => {
        // 触发图表重新渲染
        window.dispatchEvent(new Event('resize'))
      })
    }
  })

  observer.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['theme-mode']
  })

  return observer
}

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 生成模拟数据
const generateTimeData = () => {
  const hours = []
  const cpuData = []
  const memoryData = []

  for (let i = 0; i < 24; i++) {
    hours.push(`${i.toString().padStart(2, '0')}:00`)
    cpuData.push(Math.floor(Math.random() * 30) + 40) // 40-70%
    memoryData.push(Math.floor(Math.random() * 20) + 65) // 65-85%
  }

  return { hours, cpuData, memoryData }
}

const { hours, cpuData, memoryData } = generateTimeData()

// 平台信息数据
const platformInfo = ref([
  { key: 'servers', label: '服务器总数', value: '24 台', icon: 'server' as any, colorClass: 'platform-primary' },
  { key: 'databases', label: '数据库实例', value: '8 个', icon: 'database' as any, colorClass: 'platform-secondary' },
  { key: 'caches', label: '缓存节点', value: '12 个', icon: 'storage' as any, colorClass: 'platform-success' },
  { key: 'services', label: '应用服务', value: '36 个', icon: 'application' as any, colorClass: 'platform-warning' },
])

// 服务器状态图例
const serverStatusLegend = ref([
  { name: '运行中', count: 18, theme: 'success' as const, variant: 'light' as const },
  { name: '待机', count: 4, theme: 'warning' as const, variant: 'light' as const },
  { name: '告警', count: 2, theme: 'danger' as const, variant: 'light' as const },
  { name: '离线', count: 0, theme: 'default' as const, variant: 'light' as const },
])

// 系统资源数据
const systemResources = ref([
  { key: 'cpu', label: 'CPU使用率', value: '67%', icon: 'cpu' as any, colorClass: 'resource-primary' },
  { key: 'memory', label: '内存使用率', value: '78%', icon: 'memory' as any, colorClass: 'resource-secondary' },
  { key: 'disk', label: '磁盘使用率', value: '45%', icon: 'storage' as any, colorClass: 'resource-success' },
  { key: 'network', label: '网络流量', value: '2.3G', icon: 'wifi' as any, colorClass: 'resource-warning' },
])

// JVM监控数据
const jvmMetrics = ref([
  { key: 'heap', label: '堆内存使用', value: 65, showProgress: true, color: '#0052d9', displayValue: '65%' },
  { key: 'nonHeap', label: '非堆内存', value: 42, showProgress: true, color: '#00a870', displayValue: '42%' },
])

// 数据库监控数据
const dbMetrics = ref([
  { key: 'connections', label: '连接数', value: '45/100' },
  { key: 'responseTime', label: '查询响应时间', value: '23ms' },
])

// 缓存监控数据
const cacheMetrics = ref([
  { key: 'hitRate', label: '命中率', value: 89, showProgress: true, color: '#0052d9', displayValue: '89%' },
  { key: 'memoryUsage', label: '内存使用', value: 73, showProgress: true, color: '#00a870', displayValue: '73%' },
])

// 告警统计数据
const alarmStats = ref([
  { key: 'total', label: '总告警数', value: '156', icon: 'error-circle' as any },
  { key: 'critical', label: '严重告警', value: '12', icon: 'error-circle' as any },
  { key: 'warning', label: '警告告警', value: '89', icon: 'help-circle' as any },
])

// 告警列表数据
const alarmList = ref([
  { id: 1, level: '严重', message: 'CPU使用率超过90%', time: '2024-01-15 10:30:00', status: '未处理' },
  { id: 2, level: '警告', message: '内存使用率超过85%', time: '2024-01-15 10:25:00', status: '已处理' },
  { id: 3, level: '严重', message: '数据库连接数超限', time: '2024-01-15 10:20:00', status: '未处理' },
])

// 告警表格列配置
const alarmColumns = ref([
  { colKey: 'level', title: '级别', width: 80 },
  { colKey: 'message', title: '告警信息', width: 200 },
  { colKey: 'time', title: '时间', width: 150 },
  { colKey: 'status', title: '状态', width: 100 },
])

// 告警分页配置
const alarmPagination = ref({
  current: 1,
  pageSize: 10,
  total: 156,
})

// 服务健康度数据
const serviceHealth = ref([
  { name: '用户服务', status: '正常', theme: 'success' as const, variant: 'light' as const },
  { name: '订单服务', status: '警告', theme: 'warning' as const, variant: 'light' as const },
  { name: '支付服务', status: '正常', theme: 'success' as const, variant: 'light' as const },
  { name: '库存服务', status: '异常', theme: 'danger' as const, variant: 'light' as const },
])

// 服务器运行状态图表配置
const serverStatusOption = computed(() => ({
  ...currentThemeConfig.value,
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  series: [
    {
      name: '服务器状态',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      data: [
        { value: 18, name: '运行中' },
        { value: 4, name: '待机' },
        { value: 2, name: '告警' },
        { value: 0, name: '离线' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}))

// CPU使用率图表配置
const cpuChartOption = computed(() => ({
  ...currentThemeConfig.value,
  tooltip: {
    trigger: 'axis',
    formatter: '{b}<br/>CPU使用率: {c}%'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: hours
  },
  yAxis: {
    type: 'value',
    min: 0,
    max: 100,
    axisLabel: {
      formatter: '{value}%'
    }
  },
  series: [
    {
      name: 'CPU使用率',
      type: 'line',
      smooth: true,
      data: cpuData,
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0, 82, 217, 0.3)' },
            { offset: 1, color: 'rgba(0, 82, 217, 0.1)' }
          ]
        }
      }
    }
  ]
}))

// 内存使用率图表配置
const memoryChartOption = computed(() => ({
  ...currentThemeConfig.value,
  tooltip: {
    trigger: 'axis',
    formatter: '{b}<br/>内存使用率: {c}%'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: hours
  },
  yAxis: {
    type: 'value',
    min: 0,
    max: 100,
    axisLabel: {
      formatter: '{value}%'
    }
  },
  series: [
    {
      name: '内存使用率',
      type: 'line',
      smooth: true,
      data: memoryData,
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0, 168, 112, 0.3)' },
            { offset: 1, color: 'rgba(0, 168, 112, 0.1)' }
          ]
        }
      }
    }
  ]
}))

// 服务健康度图表配置
const healthChartOption = computed(() => ({
  ...currentThemeConfig.value,
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '服务健康度',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      data: [
        { value: 85, name: '健康' },
        { value: 12, name: '警告' },
        { value: 3, name: '异常' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}))

let timer: NodeJS.Timeout
let themeObserver: MutationObserver

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)

  // 初始化主题状态
  const html = document.documentElement
  isDarkTheme.value = html.getAttribute('theme-mode') === 'dark'
  currentThemeConfig.value = themeManager.getCurrentThemeConfig()

  // 监听主题变化
  themeObserver = watchThemeChange()

  // 监听ECharts主题变化事件
  window.addEventListener('echarts-theme-change', (event) => {
    const customEvent = event as CustomEvent
    console.log('ECharts主题变化事件触发:', customEvent.detail)
    // 更新主题配置
    currentThemeConfig.value = themeManager.getCurrentThemeConfig()
    // 强制触发图表重新渲染
    nextTick(() => {
      window.dispatchEvent(new Event('resize'))
    })
  })

  // 调试信息
  console.log('当前主题:', themeManager.getCurrentTheme())
  console.log('主题配置:', themeManager.getCurrentThemeConfig())
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
  if (themeObserver) {
    themeObserver.disconnect()
  }
})
</script>

<style scoped lang="less">
.ops-dashboard {
  padding: var(--td-comp-margin-xs);
  background: var(--td-bg-color-page);
  min-height: 100vh;

  .dashboard-header {
    margin-bottom: var(--td-comp-margin-xl);
    border-radius: var(--td-radius-large);
    transition: all 0.3s ease;

    &:hover {
      // 保持悬停效果但不添加阴影
    }

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: var(--td-comp-margin-lg);

      .header-left {
        display: flex;
        align-items: center;
        gap: var(--td-comp-margin-xl);

        .title {
          display: flex;
          align-items: center;
          gap: var(--td-comp-margin-sm);
        }

        .nav-tabs {
          :deep(.t-tabs__nav) {
            background: transparent;
          }
        }
      }

      .header-right {
        display: flex;
        align-items: center;
        gap: var(--td-comp-margin-xl);

        .user-info {
          display: flex;
          align-items: center;
          gap: var(--td-comp-margin-sm);
        }
      }
    }
  }

  .dashboard-content {
    display: flex;
    flex-direction: column;
    gap: var(--td-comp-margin-xl);

    .content-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: var(--td-comp-margin-xl);

      &:nth-child(1) {
        grid-template-columns: 1fr 1fr 1fr;
      }

      &:nth-child(2) {
        grid-template-columns: 1fr 1fr;
      }

      &:nth-child(3) {
        grid-template-columns: 1fr 1fr 1fr;
      }

      &:nth-child(4) {
        grid-template-columns: 2fr 1fr;
      }
    }

    .dashboard-card {
      border-radius: var(--td-radius-large);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
      }

      .platform-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: var(--td-comp-margin-lg);

        .platform-item {
          border-radius: var(--td-radius-default);
          transition: all 0.3s ease;

          &:hover {
            transform: scale(1.02);
          }

          .item-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            padding: var(--td-comp-margin-lg);

            .value {
              margin-top: var(--td-comp-margin-sm);
              font-weight: bold;
              font-size: 1.2em;
            }
          }

          &.platform-primary {
            background: var(--td-brand-color-light);
            color: var(--td-brand-color);
          }

          &.platform-secondary {
            background: var(--td-success-color-light);
            color: var(--td-success-color);
          }

          &.platform-success {
            background: var(--td-warning-color-light);
            color: var(--td-warning-color);
          }

          &.platform-warning {
            background: var(--td-error-color-light);
            color: var(--td-error-color);
          }
        }
      }

      .status-content {
        display: flex;
        align-items: center;
        gap: var(--td-comp-margin-xl);

        .chart-container {
          flex: 1;
          height: 200px;

          .status-chart {
            height: 100%;
            width: 100%;
          }
        }

        .status-legend {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: var(--td-comp-margin-md);
        }
      }

      .resources-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: var(--td-comp-margin-lg);

        .resource-item {
          border-radius: var(--td-radius-default);
          transition: all 0.3s ease;

          &:hover {
            transform: scale(1.02);
          }

          .item-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            padding: var(--td-comp-margin-lg);

            .value {
              margin-top: var(--td-comp-margin-sm);
              font-weight: bold;
              font-size: 1.2em;
            }
          }

          &.resource-primary {
            background: var(--td-brand-color-light);
            color: var(--td-brand-color);
          }

          &.resource-secondary {
            background: var(--td-success-color-light);
            color: var(--td-success-color);
          }

          &.resource-success {
            background: var(--td-warning-color-light);
            color: var(--td-warning-color);
          }

          &.resource-warning {
            background: var(--td-error-color-light);
            color: var(--td-error-color);
          }
        }
      }

      .chart-container {
        height: 200px;

        .chart {
          height: 100%;
          width: 100%;
        }
      }

      .jvm-content,
      .db-content,
      .cache-content {
        .metric-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: var(--td-comp-margin-lg);
          padding: var(--td-comp-margin-md);
          border-radius: var(--td-radius-default);
          background: var(--td-bg-color-container-hover);
          transition: all 0.3s ease;

          &:hover {
            background: var(--td-bg-color-container-active);
            transform: translateX(4px);
          }

          .progress-bar {
            flex: 1;
            margin: 0 var(--td-comp-margin-lg);
          }

          .value {
            min-width: 80px;
            text-align: right;
            font-weight: bold;
            font-size: 1.1em;
          }
        }
      }

      .alarm-content {
        .alarm-stats {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: var(--td-comp-margin-lg);
          margin-bottom: var(--td-comp-margin-xl);

          .stat-item {
            .stat-content {
              display: flex;
              flex-direction: column;
              align-items: center;
              text-align: center;
              padding: var(--td-comp-margin-lg);
              background: var(--td-brand-color-light);
              border-radius: var(--td-radius-default);
              transition: all 0.3s ease;

              &:hover {
                transform: scale(1.05);
              }

              .value {
                margin-top: var(--td-comp-margin-sm);
                font-weight: bold;
                font-size: 1.2em;
                color: var(--td-brand-color);
              }
            }
          }
        }

        .alarm-table {
          :deep(.t-table) {
            font-size: var(--td-font-size-body-small);
          }
        }
      }

      .health-content {
        .health-chart {
          margin-bottom: var(--td-comp-margin-xl);
          height: 200px;

          .health-chart-component {
            height: 100%;
            width: 100%;
          }
        }

        .service-list {
          display: flex;
          flex-direction: column;
          gap: var(--td-comp-margin-md);

          .service-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: var(--td-comp-margin-md);
            border-radius: var(--td-radius-default);
            background: var(--td-bg-color-container-hover);
            transition: all 0.3s ease;

            &:hover {
              background: var(--td-bg-color-container-active);
              transform: translateX(4px);
            }
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .ops-dashboard {
    .dashboard-content {
      gap: var(--td-comp-margin-lg);

      .content-row {
        gap: var(--td-comp-margin-lg);

        &:nth-child(1),
        &:nth-child(3) {
          grid-template-columns: 1fr 1fr;
        }

        &:nth-child(4) {
          grid-template-columns: 1fr;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .ops-dashboard {
    padding: var(--td-comp-margin-sm);

    .dashboard-header {
      margin-bottom: var(--td-comp-margin-md);

      .header-content {
        padding: var(--td-comp-margin-md);
        flex-direction: column;
        gap: var(--td-comp-margin-md);

        .header-left {
          flex-direction: column;
          gap: var(--td-comp-margin-md);
        }
      }
    }

    .dashboard-content {
      gap: var(--td-comp-margin-md);

      .content-row {
        grid-template-columns: 1fr !important;
        gap: var(--td-comp-margin-md);

        .dashboard-card {

          .platform-grid,
          .resources-grid {
            gap: var(--td-comp-margin-md);
          }

          .status-content {
            gap: var(--td-comp-margin-md);
            flex-direction: column;
          }
        }
      }
    }
  }
}
</style>
