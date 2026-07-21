<template>
  <scroll-view class="home-scroll" scroll-y :show-scrollbar="false">
    <view class="home-page">
      <view class="topbar">
        <view class="location" @tap="switchMainTab(1)">
          <uv-icon name="map-fill" color="#2E86F6" size="17"></uv-icon>
          <text>深圳市 · 南山区</text>
          <uv-icon name="arrow-right" color="#9AA5B5" size="12"></uv-icon>
        </view>
        <view class="weather">多云 26°C <text class="air-dot"></text> 空气优</view>
      </view>

      <view class="brand-card">
        <image class="brand-banner-image" src="/static/imgs/banner.png" mode="aspectFill"></image>
        <view class="scan-button" @tap="switchMainTab(2)"><uv-icon name="scan" color="#FFFFFF" size="18"></uv-icon><text>扫一扫</text></view>
        <view class="hero-copy">
          <text>智能换电</text>
          <text>安心续航</text>
          <view class="hero-tip">3 步换电 30 秒满电出发 <uv-icon name="arrow-right" color="#5F83AD" size="13"></uv-icon></view>
        </view>
      </view>

      <view class="service-heading"><text>快捷服务</text></view>
      <view class="quick-grid">
        <view v-for="item in quickActions" :key="item.title" class="quick-item" @tap="handleQuickAction(item)">
          <view class="quick-icon" :class="item.iconClass"><uv-icon :name="item.icon" :color="item.color" size="25"></uv-icon></view>
          <text class="quick-title">{{ item.title }}</text>
          <text class="quick-desc">{{ item.desc }}</text>
        </view>
      </view>

      <view class="station-section">
        <view class="section-header"><text>附近换电站</text><view class="more-link" @tap="switchMainTab(1)">更多站点 <uv-icon name="arrow-right" color="#A4ADBA" size="13"></uv-icon></view></view>
        <view v-if="station" class="station-card" @tap="openStation(station)">
          <image class="station-cover" src="/static/imgs/banner.png" mode="aspectFill"></image>
          <view class="station-info">
            <view class="station-name-row"><text>{{ station.name }}</text><text class="open-tag">{{ station.status === 1 ? '营业中' : '维护中' }}</text></view>
            <view class="station-address"><text>距离 {{ station.distance || '326m' }}</text><text>｜</text><text>{{ station.address }}</text></view>
            <view class="station-stock-row">
              <view class="station-stock"><view class="stock-symbol battery-symbol"></view><text>{{ station.availableBatteryCount || 0 }}</text><text>可用电池</text></view>
              <view class="station-stock"><view class="stock-symbol slot-symbol"></view><text>{{ station.availableReturnSlots || 0 }}</text><text>可还空位</text></view>
            </view>
          </view>
          <view class="station-route" @tap.stop="openStation(station)"><view><uv-icon name="map-fill" color="#FFFFFF" size="15"></uv-icon></view><text>去这里</text></view>
        </view>
        <view v-if="station" class="station-pagination" aria-label="附近站点轮播页码"><view class="active"></view><view></view><view></view></view>
      </view>

      <view class="coupon-banner" @tap="claimCoupon">
        <view class="coupon-badge"><text>20</text><text>元换电券</text></view>
        <view class="coupon-copy"><text>{{ couponClaimed ? '礼包已领取' : '新用户专享礼包' }}</text><text>{{ couponClaimed ? '20 元换电券已放入账户' : '注册即送 20 元换电券' }}</text></view>
        <view class="coupon-action">{{ couponClaimed ? '已领取' : '立即领取' }}</view>
      </view>

      <view class="stats-section">
        <view class="stats-card">
          <view class="ride-header"><text>骑行数据</text><view @tap="showRideData">本月数据 <uv-icon name="arrow-right" color="#A4ADBA" size="12"></uv-icon></view></view>
          <view class="ride-stats-grid">
            <view class="ride-stat-item"><view class="ride-stat-icon green"><uv-icon name="heart-fill" color="#46C697" size="15"></uv-icon></view><view class="ride-stat-value"><text>12</text><text>次</text></view><text>换电次数</text></view>
            <view class="ride-stat-item"><view class="ride-stat-icon blue"><uv-icon name="map-fill" color="#4F91F5" size="15"></uv-icon></view><view class="ride-stat-value"><text>186</text><text>km</text></view><text>骑行里程</text></view>
            <view class="ride-stat-item"><view class="ride-stat-icon purple"><uv-icon name="edit-pen" color="#876AF2" size="15"></uv-icon></view><view class="ride-stat-value"><text>24</text><text>kg</text></view><text>减少碳排</text></view>
            <view class="ride-stat-item"><view class="ride-stat-icon orange"><uv-icon name="coupon-fill" color="#F39B4C" size="15"></uv-icon></view><view class="ride-stat-value"><text>68</text><text>元</text></view><text>节约费用</text></view>
          </view>
        </view>
      </view>
      <view class="bottom-spacer"></view>
    </view>
  </scroll-view>
</template>

<script>
import { getNearbyStations } from '../../api/energy'

const DEFAULT_STATION = {
  name: '科技园换电站', city: '深圳市', district: '南山区', address: '科技园南区科苑路15号',
  distance: '326m', status: 1, availableBatteryCount: 12, availableReturnSlots: 8
}

export default {
  name: 'EnergyHome',
  data() {
    return {
      station: DEFAULT_STATION,
      couponClaimed: Boolean(uni.getStorageSync('energy_new_user_coupon')),
      quickActions: [
        { title: '扫码换电', desc: '30秒换电', icon: 'scan', color: '#3389F7', iconClass: 'blue' },
        { title: '我的电池', desc: '电池状态', icon: 'coupon', color: '#7584F7', iconClass: 'purple' },
        { title: '电池暂存', desc: '安全存放', icon: 'lock', color: '#27B99A', iconClass: 'mint' },
        { title: '故障上报', desc: '快速维修', icon: 'edit-pen', color: '#F29A55', iconClass: 'orange' },
        { title: '我的订单', desc: '历史记录', icon: 'file-text', color: '#F07880', iconClass: 'pink' }
      ]
    }
  },
  mounted() { this.loadStations() },
  methods: {
    async loadStations() {
      try {
        const stations = await getNearbyStations(1)
        if (stations && stations.length) this.station = stations[0]
      } catch (error) { console.warn(error.message) }
    },
    switchMainTab(key) { this.$emit('switch-tab', key) },
    openStation(station = this.station) {
      // 后端暂未返回站点时，仍打开设计稿的默认详情，避免首页入口被拦截。
      const stationId = station?.id
      uni.navigateTo({ url: stationId ? `/pages/station/detail?id=${stationId}` : '/pages/station/detail' })
    },
    handleQuickAction(item) {
      if (item.title === '扫码换电') return this.switchMainTab(2)
      if (item.title === '我的电池') return uni.navigateTo({ url: '/pages/battery/index' })
      if (item.title === '电池暂存') return uni.navigateTo({ url: '/pages/storage/index' })
      if (item.title === '故障上报') return uni.navigateTo({ url: '/pages/repair/create' })
      if (item.title === '我的订单') return this.switchMainTab(3)
      uni.showToast({ title: '服务已打开', icon: 'none' })
    },
    claimCoupon() {
      if (this.couponClaimed) return uni.showToast({ title: '礼包已在优惠券账户中', icon: 'none' })
      this.couponClaimed = true
      uni.setStorageSync('energy_new_user_coupon', true)
      uni.showToast({ title: '20 元换电券已到账', icon: 'success' })
    },
    showRideData() {
      uni.showModal({
        title: '本月骑行数据',
        content: '已换电 12 次\n骑行里程 186 km\n减少碳排 24 kg\n预计节约 68 元',
        showCancel: false
      })
    }
  }
}
</script>

<style scoped>
.home-scroll,.home-page{height:100%;background:#F9FAFC}.home-page{min-height:100%;padding:calc(var(--status-bar-height,0px) + 92rpx) 28rpx 0;box-sizing:border-box;color:#202938}.topbar{height:84rpx;padding:0 2rpx;box-sizing:border-box}.location{display:flex;align-items:center;gap:8rpx;font-size:28rpx;font-weight:600}.weather{margin:12rpx 0 0 40rpx;color:#8A95A5;font-size:24rpx}.air-dot{display:inline-block;width:7rpx;height:7rpx;border-radius:50%;margin:0 5rpx;background:#29C492;vertical-align:middle}.brand-card{height:250rpx;border-radius:26rpx;position:relative;overflow:hidden;background:linear-gradient(120deg,#2D80EE 0%,#4BA0FB 100%);box-shadow:0 16rpx 32rpx rgba(46,132,241,.2)}.brand-banner-image{position:absolute;z-index:1;inset:0;width:100%;height:100%;pointer-events:none}.scan-button{position:absolute;z-index:3;right:20rpx;top:16rpx;height:48rpx;padding:0 16rpx;border-radius:15rpx;background:rgba(255,255,255,.18);display:flex;align-items:center;gap:6rpx;color:#fff;font-size:24rpx}.hero-copy{position:absolute;z-index:2;left:31rpx;top:80rpx;width:470rpx;color:#fff;display:flex;flex-wrap:wrap;align-items:center;column-gap:14rpx;font-size:42rpx;font-weight:700;line-height:1.15;letter-spacing:1rpx}.hero-copy>text{display:block;white-space:nowrap}.hero-tip{flex-basis:100%;margin-top:12rpx;display:flex;align-items:center;color:#DFECFF;font-size:24rpx;font-weight:400;letter-spacing:0}.service-heading{margin:39rpx 4rpx 0;color:#1F2838;font-size:32rpx;font-weight:700}.quick-grid{margin-top:40rpx;display:flex}.quick-item{width:20%;display:flex;align-items:center;flex-direction:column;min-width:0}.quick-icon{width:76rpx;height:76rpx;border-radius:24rpx;display:flex;align-items:center;justify-content:center}.quick-icon.blue{background:#E5F0FF}.quick-icon.purple{background:#F0ECFF}.quick-icon.mint{background:#E3F8F2}.quick-icon.orange{background:#FFF0E3}.quick-icon.pink{background:#FFECEE}.quick-title{margin-top:12rpx;color:#313C4E;font-size:24rpx;white-space:nowrap}.quick-desc{margin-top:5rpx;color:#A0A9B6;font-size:21rpx;white-space:nowrap}.station-section{margin-top:72rpx}.section-header{display:flex;align-items:center;justify-content:space-between;margin:0 4rpx 18rpx}.section-header>text{font-size:30rpx;font-weight:700;color:#202938}.more-link{display:flex;align-items:center;color:#99A3B2;font-size:24rpx}.station-card{padding:25rpx 24rpx 22rpx;border-radius:24rpx;background:#fff;box-shadow:0 10rpx 30rpx rgba(36,63,95,.06)}.station-top{display:flex;align-items:center}.station-icon{width:82rpx;height:82rpx;border-radius:20rpx;display:flex;align-items:center;justify-content:center;background:#E7F2FF;flex-shrink:0}.station-grid{width:42rpx;height:46rpx;padding:5rpx;box-sizing:border-box;display:grid;grid-template-columns:repeat(2,1fr);gap:4rpx;border:3rpx solid #398BF3;border-radius:6rpx}.station-grid view{border-radius:2rpx;background:#398BF3}.station-info{min-width:0;flex:1;margin-left:17rpx}.station-name-row{display:flex;align-items:center;gap:10rpx}.station-name-row>text:first-child{color:#232D3B;font-size:29rpx;font-weight:600}.open-tag{padding:4rpx 8rpx;color:#26A976;border-radius:7rpx;background:#E5F8F0;font-size:21rpx}.station-address{margin-top:11rpx;display:flex;align-items:center;gap:5rpx;color:#929CAA;font-size:22rpx;white-space:nowrap;overflow:hidden}.station-address text{overflow:hidden;text-overflow:ellipsis}.station-line{height:2rpx;margin:23rpx 0 19rpx;background:#F0F2F6}.station-bottom{display:flex;align-items:center}.stock{display:flex;align-items:baseline}.stock:first-child{margin-right:42rpx}.stock text{font-size:35rpx;color:#2D86F3;font-weight:700}.stock>text:last-child{margin-left:7rpx;color:#909AA9;font-size:23rpx}.go-button{margin-left:auto;padding:14rpx 19rpx;border-radius:13rpx;background:#E8F2FF;color:#2D83F1;font-size:24rpx;font-weight:600}.coupon-banner{height:151rpx;margin-top:28rpx;padding:0 25rpx;border-radius:23rpx;box-sizing:border-box;display:flex;align-items:center;overflow:hidden;background:linear-gradient(112deg,#FFF5E8,#FFE8CF)}.coupon-badge{width:98rpx;height:98rpx;box-sizing:border-box;padding-top:13rpx;border-radius:15rpx;display:flex;align-items:center;flex-direction:column;background:#F49A54;color:#fff;line-height:1}.coupon-badge text{font-size:39rpx;font-weight:700}.coupon-badge>text:last-child{margin-top:7rpx;font-size:20rpx}.coupon-copy{margin-left:19rpx;display:flex;flex:1;flex-direction:column}.coupon-copy text{color:#8B552F;font-size:28rpx;font-weight:600}.coupon-copy>text:last-child{margin-top:10rpx;color:#B77D53;font-size:23rpx}.coupon-action{color:#D47B39;font-size:24rpx;font-weight:600}.stats-section{margin-top:40rpx}.stats-card{min-height:142rpx;padding:20rpx 18rpx;box-sizing:border-box;border-radius:23rpx;display:flex;align-items:center;background:#fff;box-shadow:0 10rpx 30rpx rgba(36,63,95,.06)}.main-stat{width:142rpx;padding-left:9rpx;box-sizing:border-box;border-right:2rpx solid #EFF2F7}.main-stat-value{display:flex;align-items:baseline;gap:5rpx;color:#253042}.main-stat-value>text:first-child{font-size:43rpx;font-weight:700}.main-stat-value>text:last-child{font-size:24rpx;font-weight:400}.main-stat>view:last-child{margin-top:8rpx;color:#98A1AE;font-size:22rpx}.stat{flex:1;display:flex;align-items:center;flex-direction:column}.stat text{color:#354257;font-size:25rpx;font-weight:600}.stat>text:last-child{margin-top:10rpx;color:#9CA5B2;font-size:21rpx}.bottom-spacer{height:170rpx}
@media screen and (max-height:700px){.coupon-banner{height:130rpx;margin-top:18rpx}.stats-section{margin-top:20rpx}.stats-card{min-height:132rpx;padding-top:10rpx;padding-bottom:10rpx}}

/* visual polish: keep density while improving hierarchy */
.home-page{background:linear-gradient(180deg,#FBFDFF 0,#F5F8FC 58%,#F5F8FC 100%)}
.brand-card{height:266rpx;border-radius:30rpx;box-shadow:0 18rpx 42rpx rgba(45,126,232,.22)}
.scan-button{height:52rpx;padding:0 18rpx;border:1rpx solid rgba(255,255,255,.16);border-radius:17rpx;backdrop-filter:blur(8rpx)}
.quick-grid{margin-top:34rpx}.quick-icon{width:80rpx;height:80rpx;border-radius:25rpx;box-shadow:0 8rpx 20rpx rgba(49,79,116,.055)}
.quick-title{margin-top:14rpx;font-weight:600}.quick-desc{margin-top:6rpx}
.station-section{margin-top:66rpx}.station-card{padding:28rpx 26rpx 25rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.coupon-banner{height:156rpx;border:1rpx solid rgba(232,157,91,.1);border-radius:27rpx;box-shadow:0 9rpx 25rpx rgba(171,104,46,.06)}
.stats-card{min-height:148rpx;padding:23rpx 20rpx;border-radius:27rpx;box-shadow:var(--gy-card-shadow)}
@media screen and (max-width:360px){.brand-card{height:258rpx}.station-section{margin-top:58rpx}.quick-icon{width:76rpx;height:76rpx}}
@media screen and (max-height:700px){.coupon-banner{height:138rpx}.stats-card{min-height:136rpx;padding-top:14rpx;padding-bottom:14rpx}}



/* Keep the location / weather context available while the home feed scrolls. */
.topbar{height:calc(var(--status-bar-height,0px) + 176rpx);position:sticky;z-index:12;top:0;margin:calc(-1 * (var(--status-bar-height,0px) + 92rpx)) -28rpx 0;padding:calc(var(--status-bar-height,0px) + 92rpx) 28rpx 0;background:rgba(249,250,252,.96);box-shadow:0 8rpx 22rpx rgba(37,66,99,.06);backdrop-filter:blur(18rpx)}
@media screen and (max-width:360px){.topbar{margin-right:-22rpx;margin-left:-22rpx;padding-right:22rpx;padding-left:22rpx}}



/* Flat hierarchy: low-level content is separated by surface borders, not floating shadows. */
.brand-card{box-shadow:var(--gy-shadow-brand)}
.station-card,.stats-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.coupon-banner{border:2rpx solid rgba(239,218,193,.72);box-shadow:none}
.quick-icon,.topbar{box-shadow:none}.topbar{border-bottom:2rpx solid rgba(229,235,243,.82)}

/* Nearby-station card follows the compact image-led home design. */
.station-section{margin-top:48rpx}
.station-section .section-header{margin-bottom:14rpx}
.station-section .section-header>text{font-size:28rpx}
.station-section .more-link{min-height:0;color:#A7B0BD;font-size:21rpx}
.station-card{min-height:176rpx;padding:16rpx 18rpx;display:flex;align-items:center;border-radius:22rpx;box-sizing:border-box;background:#FFFFFF}
.station-cover{width:130rpx;height:130rpx;flex:0 0 130rpx;border-radius:13rpx;background:#E7F2FF}
.station-info{min-width:0;margin-left:16rpx;align-self:stretch;display:flex;flex:1;flex-direction:column;justify-content:center}
.station-name-row{gap:8rpx}.station-name-row>text:first-child{font-size:25rpx;line-height:1.25}.open-tag{padding:3rpx 7rpx;border-radius:6rpx;font-size:18rpx;line-height:1.2}
.station-address{height:28rpx;margin-top:8rpx;gap:0;color:#919DAC;font-size:19rpx;line-height:28rpx}.station-address text{white-space:nowrap}.station-address text:last-child{min-width:0;overflow:hidden;text-overflow:ellipsis}
.station-stock-row{margin-top:14rpx;display:flex;align-items:center;gap:18rpx}
.station-stock{display:flex;align-items:center;white-space:nowrap;color:#98A4B3;font-size:18rpx}.station-stock>text:nth-of-type(1){margin-left:6rpx;color:#263449;font-size:29rpx;font-weight:700;line-height:1}.station-stock>text:last-child{margin-left:4rpx}
.stock-symbol{width:16rpx;height:20rpx;border-radius:4rpx;position:relative}.battery-symbol{background:#55C69C}.battery-symbol:after{content:'';width:5rpx;height:7rpx;position:absolute;top:-4rpx;left:5.5rpx;border-radius:2rpx 2rpx 0 0;background:#55C69C}.slot-symbol{background:#67A9F3}.slot-symbol:after{content:'';width:8rpx;height:8rpx;position:absolute;top:6rpx;left:4rpx;border-radius:2rpx;background:rgba(255,255,255,.9)}
.station-route{width:56rpx;margin-left:10rpx;flex:0 0 56rpx;display:flex;align-items:center;flex-direction:column;color:#6D7E91;font-size:18rpx}.station-route>view{width:44rpx;height:44rpx;display:flex;align-items:center;justify-content:center;border-radius:50%;background:#3188F5;box-shadow:0 5rpx 14rpx rgba(49,136,245,.2)}.station-route>text{margin-top:5rpx}
.station-pagination{height:10rpx;margin-top:-2rpx;display:flex;align-items:center;justify-content:center;gap:7rpx}.station-pagination view{width:8rpx;height:5rpx;border-radius:5rpx;background:#D7E0EA}.station-pagination .active{width:19rpx;background:#2F87F5}

/* Riding data card groups the period control and all four metrics in one compact surface. */
.stats-section{margin-top:42rpx}
.stats-card{min-height:0;padding:19rpx 17rpx 18rpx;display:block;border-radius:23rpx;box-sizing:border-box;background:#FFFFFF}
.ride-header{height:35rpx;display:flex;align-items:center;justify-content:space-between}.ride-header>text{color:#273448;font-size:28rpx;font-weight:700}.ride-header>view{min-height:44rpx;display:flex;align-items:center;color:#A3ADBB;font-size:20rpx}
.ride-stats-grid{margin-top:10rpx;display:flex;align-items:flex-start}.ride-stat-item{min-width:0;flex:1;display:flex;align-items:center;flex-direction:column}.ride-stat-icon{width:34rpx;height:34rpx;display:flex;align-items:center;justify-content:center;border-radius:10rpx}.ride-stat-icon.green{background:#E2F8F0}.ride-stat-icon.blue{background:#E7F0FF}.ride-stat-icon.purple{background:#F0EBFF}.ride-stat-icon.orange{background:#FFF0E2}
.ride-stat-value{height:32rpx;margin-top:7rpx;display:flex;align-items:baseline;color:#334054;line-height:1}.ride-stat-value>text:first-child{font-size:26rpx;font-weight:700}.ride-stat-value>text:last-child{margin-left:3rpx;font-size:17rpx}.ride-stat-item>text{margin-top:4rpx;color:#9AA5B4;font-size:18rpx;white-space:nowrap}

/* The banner artwork has transparency; let the page show through instead of adding a blue card background. */
.brand-card{background:transparent;box-shadow:none}
.hero-copy{color:#286EBC}
.hero-tip{color:#5F83AD}
.scan-button{background:#3188F5;box-shadow:0 7rpx 16rpx rgba(49,136,245,.2)}



/* Adversarial UI audit: preserve information density while lifting secondary copy above the tiny-text threshold. */
.quick-desc{font-size:22rpx;line-height:1.35}
.station-address,.coupon-copy>text:last-child,.main-stat>view:last-child,.stat>text:last-child{font-size:22rpx;line-height:1.4}
.more-link,.coupon-action{min-height:60rpx;display:flex;align-items:center}
.quick-item{min-height:152rpx;justify-content:flex-start}
@media screen and (max-width:360px){.quick-desc{font-size:21rpx}.station-address{font-size:21rpx}}

</style>
