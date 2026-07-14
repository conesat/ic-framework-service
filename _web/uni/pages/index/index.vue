<template>
  <view class="index-page">
    <home v-if="activeTab === 0" @switch-tab="switchTab"></home>
    <energy-map v-else-if="activeTab === 1"></energy-map>
    <energy-scanner v-else-if="activeTab === 2"></energy-scanner>
    <energy-orders v-else-if="activeTab === 3" @switch-tab="switchTab"></energy-orders>
    <mine v-else-if="activeTab === 4" @switch-tab="switchTab"></mine>

    <view class="tabbar">
      <view
        v-for="tab in tabs"
        :key="tab.key"
        class="tab-item"
        :class="{ active: activeTab === tab.key, scan: tab.key === 2 }"
        @tap="switchTab(tab.key)"
      >
        <view v-if="tab.key === 2" class="scan-tab-icon">
          <uv-icon name="scan" color="#FFFFFF" size="27"></uv-icon>
        </view>
        <uv-icon
          v-else
          :name="activeTab === tab.key ? tab.activeIcon : tab.icon"
          :color="activeTab === tab.key ? '#3389F7' : '#8F9AA8'"
          size="23"
        ></uv-icon>
        <text>{{ tab.title }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import Home from '../_components/home.vue'
import EnergyMap from '../_components/map.vue'
import EnergyScanner from '../_components/scanner.vue'
import EnergyOrders from '../_components/orders.vue'
import Mine from '../_components/my.vue'

export default {
  onLoad(options) {
    const tab = Number(options && options.tab)
    if (Number.isInteger(tab) && tab >= 0 && tab <= 4) this.activeTab = tab
  },
  components: { Home, EnergyMap, EnergyScanner, EnergyOrders, Mine },
  data() {
    return {
      activeTab: 0,
      tabs: [
        { key: 0, title: '首页', icon: 'home', activeIcon: 'home-fill' },
        { key: 1, title: '地图', icon: 'map', activeIcon: 'map-fill' },
        { key: 2, title: '扫码换电', icon: 'scan', activeIcon: 'scan' },
        { key: 3, title: '订单', icon: 'file-text', activeIcon: 'file-text-fill' },
        { key: 4, title: '我的', icon: 'account', activeIcon: 'account-fill' }
      ]
    }
  },
  methods: {
    switchTab(key) { this.activeTab = Number(key) }
  }
}
</script>

<style scoped>
.index-page { width: 100%; height: 100%; position: relative; overflow: hidden; background: #F9FAFC; }
.tabbar { height: calc(128rpx + env(safe-area-inset-bottom)); padding: 12rpx 16rpx env(safe-area-inset-bottom); position: absolute; z-index: 20; left: 0; right: 0; bottom: 0; display: flex; align-items: flex-start; background: rgba(255,255,255,.98); box-shadow: 0 -4rpx 24rpx rgba(24,48,79,.08); }
.tab-item { min-width: 0; flex: 1; display: flex; align-items: center; flex-direction: column; color: #8B96A6; font-size: 22rpx; line-height: 1; }
.tab-item text { margin-top: 8rpx; white-space: nowrap; }
.tab-item.active { color: #3389F7; font-weight: 600; }
.tab-item.scan { margin-top: -39rpx; color: #3389F7; }
.scan-tab-icon { width: 88rpx; height: 88rpx; display: flex; align-items: center; justify-content: center; border: 7rpx solid #FFFFFF; border-radius: 30rpx; background: linear-gradient(145deg, #4AA0FA, #2F82EE); box-shadow: 0 12rpx 24rpx rgba(47,130,238,.26); }
.tab-item.scan text { margin-top: 8rpx; }


/* Flat persistent navigation: a hairline separator replaces the floating dock shadow. */
.tabbar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.scan-tab-icon{box-shadow:var(--gy-shadow-brand)}

</style>
