import http from '@/api/common/http'

// 获取平台信息
export const getPlatformInfo = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/platform-info',
      success: resolve,
      fail: reject
    })
  })
}

// 获取服务器状态
export const getServerStatus = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/server-status',
      success: resolve,
      fail: reject
    })
  })
}

// 获取系统资源使用情况
export const getSystemResources = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/system-resources',
      success: resolve,
      fail: reject
    })
  })
}

// 获取CPU使用率趋势
export const getCpuTrend = (params: { startTime: string; endTime: string }) => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/cpu-trend',
      data: params,
      success: resolve,
      fail: reject
    })
  })
}

// 获取内存使用率趋势
export const getMemoryTrend = (params: { startTime: string; endTime: string }) => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/memory-trend',
      data: params,
      success: resolve,
      fail: reject
    })
  })
}

// 获取JVM监控数据
export const getJvmMonitor = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/jvm-monitor',
      success: resolve,
      fail: reject
    })
  })
}

// 获取数据库监控数据
export const getDatabaseMonitor = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/database-monitor',
      success: resolve,
      fail: reject
    })
  })
}

// 获取缓存监控数据
export const getCacheMonitor = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/cache-monitor',
      success: resolve,
      fail: reject
    })
  })
}

// 获取告警信息
export const getAlarmInfo = (params: { page: number; pageSize: number }) => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/alarm-info',
      data: params,
      success: resolve,
      fail: reject
    })
  })
}

// 获取服务健康度
export const getServiceHealth = () => {
  return new Promise<any>((resolve, reject) => {
    http.get({
      url: '/ops/service-health',
      success: resolve,
      fail: reject
    })
  })
}
