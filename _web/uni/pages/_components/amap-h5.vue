<template>
  <view class="amap-wrapper">
    <view ref="mapElement" class="amap-canvas"></view>

    <view v-if="status !== 'ready'" class="amap-state" :class="{ error: status === 'error' }">
      <view v-if="status === 'loading'" class="state-card">
        <view class="loading-dot"></view>
        <text>正在加载地图</text>
      </view>
      <view v-else class="state-card error-card">
        <uv-icon name="map" color="#358AF5" size="26"></uv-icon>
        <text class="state-title">地图暂时未加载</text>
        <text class="state-copy">{{ errorMessage }}</text>
        <view class="state-action" @tap="$emit('open-station-list')">查看站点列表</view>
      </view>
    </view>
  </view>
</template>

<script>
import { amapConfig } from '../../utils/amap-config'

const AMAP_SCRIPT_ID = 'guangyu-amap-js-api'
const AMAP_SCRIPT_URL = 'https://webapi.amap.com/maps?v=2.0&plugin=AMap.Scale,AMap.ToolBar,AMap.Geolocation'

function readAmapConfig() {
  return {
    key: String(amapConfig.key || '').trim(),
    securityJsCode: String(amapConfig.securityJsCode || '').trim()
  }
}

function loadAmap(config) {
  if (window.AMap) return Promise.resolve(window.AMap)

  if (!config.key) {
    return Promise.reject(new Error('未检测到高德 Web JS API Key，请检查 utils/amap-config.js。'))
  }

  if (config.securityJsCode) {
    window._AMapSecurityConfig = {
      ...(window._AMapSecurityConfig || {}),
      securityJsCode: config.securityJsCode
    }
  }

  const existing = document.getElementById(AMAP_SCRIPT_ID)
  if (existing) {
    return new Promise((resolve, reject) => {
      existing.addEventListener('load', () => window.AMap ? resolve(window.AMap) : reject(new Error('高德地图脚本未成功初始化。')), { once: true })
      existing.addEventListener('error', () => reject(new Error('高德地图脚本加载失败，请检查 Key、Referer 白名单与网络。')), { once: true })
    })
  }

  return new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.id = AMAP_SCRIPT_ID
    script.async = true
    script.src = `${AMAP_SCRIPT_URL}&key=${encodeURIComponent(config.key)}`
    script.onload = () => window.AMap ? resolve(window.AMap) : reject(new Error('高德地图脚本未成功初始化。'))
    script.onerror = () => reject(new Error('高德地图脚本加载失败，请检查 Key、Referer 白名单与网络。'))
    document.head.appendChild(script)
  })
}

export default {
  name: 'AmapH5',
  props: {
    center: { type: Object, required: true },
    zoom: { type: Number, default: 14 },
    markers: { type: Array, default: () => [] }
  },
  data() {
    return {
      status: 'loading',
      errorMessage: '',
      amap: null,
      instance: null,
      markerInstances: []
    }
  },
  watch: {
    center: {
      deep: true,
      handler() { this.syncViewport() }
    },
    zoom() { this.syncViewport() },
    markers: {
      deep: true,
      handler() { this.renderMarkers() }
    }
  },
  mounted() {
    this.initializeMap()
  },
  beforeUnmount() {
    this.markerInstances.forEach(marker => marker?.setMap?.(null))
    this.markerInstances = []
    this.instance?.destroy?.()
    this.instance = null
  },
  methods: {
    async initializeMap() {
      try {
        this.status = 'loading'
        this.amap = await loadAmap(readAmapConfig())
        if (!this.$refs.mapElement) return

        this.instance = new this.amap.Map(this.$refs.mapElement, {
          zoom: this.zoom,
          center: [Number(this.center.longitude), Number(this.center.latitude)],
          viewMode: '2D',
          resizeEnable: true,
          zoomEnable: true,
          dragEnable: true
        })
        this.instance.addControl(new this.amap.Scale({ position: { right: '18px', bottom: '212px' } }))
        this.renderMarkers()
        this.status = 'ready'
        this.$emit('ready')
      } catch (error) {
        this.status = 'error'
        this.errorMessage = error?.message || '请检查高德地图配置后重试。'
        this.$emit('error', error)
      }
    },
    syncViewport() {
      if (!this.instance || !this.center) return
      const longitude = Number(this.center.longitude)
      const latitude = Number(this.center.latitude)
      if (!Number.isFinite(longitude) || !Number.isFinite(latitude)) return
      this.instance.setZoomAndCenter(this.zoom, [longitude, latitude])
    },
    renderMarkers() {
      if (!this.instance || !this.amap) return
      this.markerInstances.forEach(marker => marker?.setMap?.(null))
      this.markerInstances = this.markers
        .filter(marker => Number.isFinite(Number(marker.longitude)) && Number.isFinite(Number(marker.latitude)))
        .map(marker => {
          const isSelected = marker.callout?.display === 'ALWAYS'
          const markerInstance = new this.amap.Marker({
            position: [Number(marker.longitude), Number(marker.latitude)],
            anchor: 'bottom-center',
            content: `<div class="gy-amap-marker ${isSelected ? 'is-selected' : ''}"><span>${marker.id}</span></div>`,
            offset: new this.amap.Pixel(-17, -38)
          })
          markerInstance.on('click', () => this.$emit('markertap', { detail: { markerId: marker.id } }))
          markerInstance.setMap(this.instance)
          return markerInstance
        })
    }
  }
}
</script>

<style>
.amap-wrapper,.amap-canvas{width:100%;height:100%;position:absolute;inset:0}.amap-wrapper{overflow:hidden;background:#EAF2F9}.amap-state{position:absolute;z-index:2;inset:0;display:flex;align-items:center;justify-content:center;pointer-events:none}.state-card{min-width:190rpx;padding:24rpx 30rpx;display:flex;align-items:center;gap:12rpx;border:2rpx solid #E3EBF4;border-radius:22rpx;color:#66778B;background:rgba(255,255,255,.94);font-size:23rpx}.amap-state.error{background:linear-gradient(180deg,rgba(234,242,249,.3),rgba(234,242,249,.72))}.error-card{width:460rpx;max-width:calc(100% - 80rpx);padding:34rpx 32rpx;align-items:center;flex-direction:column;text-align:center;box-sizing:border-box}.state-title{margin-top:5rpx;color:#314054;font-size:28rpx;font-weight:700}.state-copy{margin-top:11rpx;color:#77879A;font-size:21rpx;line-height:1.55}.state-action{height:64rpx;margin-top:22rpx;padding:0 24rpx;display:flex;align-items:center;border:2rpx solid #B9D7FB;border-radius:18rpx;color:#3186F1;background:#F0F7FF;font-size:22rpx;font-weight:600;pointer-events:auto}.loading-dot{width:15rpx;height:15rpx;border-radius:50%;background:#358AF5;box-shadow:20rpx 0 0 rgba(53,138,245,.45),40rpx 0 0 rgba(53,138,245,.2);animation:gy-map-loading 1.2s ease-in-out infinite}@keyframes gy-map-loading{50%{opacity:.35;transform:translateX(8rpx)}}.gy-amap-marker{width:34px;height:38px;position:relative;display:flex;align-items:center;justify-content:center;border:3px solid #fff;border-radius:18px 18px 18px 3px;transform:rotate(-45deg);box-sizing:border-box;color:#fff;background:#5D9DF2;box-shadow:0 2px 8px rgba(31,101,190,.18)}.gy-amap-marker.is-selected{width:40px;height:44px;background:#3389F7}.gy-amap-marker span{font:600 13px/1 Arial,sans-serif;transform:rotate(45deg)}
</style>
