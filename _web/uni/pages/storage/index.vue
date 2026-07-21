<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#283344" size="21"></uv-icon></view>
          <text class="nav-title">电池暂存</text>
          <view class="nav-button" @tap="showStorageGuide"><uv-icon name="question-circle" color="#283344" size="20"></uv-icon></view>
        </view>

    <scroll-view class="page-scroll" scroll-y :show-scrollbar="false">
      <view class="storage-page">
        

        <view class="hero-card">
          <view class="hero-glow"></view>
          <view class="hero-icon"><uv-icon name="lock" color="#FFFFFF" size="29"></uv-icon></view>
          <view class="hero-copy"><text>让电池安全休息一下</text><text>支持 1—7 天灵活暂存，全程智能监测</text></view>
        </view>

        <view class="process-card">
          <view v-for="(step, index) in process" :key="step.title" class="process-item">
            <view class="process-icon" :class="{ active: index === 0 }"><uv-icon :name="step.icon" :color="index === 0 ? '#FFFFFF' : '#6C91BC'" size="18"></uv-icon></view>
            <text>{{ step.title }}</text>
            <view v-if="index < process.length - 1" class="process-line"></view>
          </view>
        </view>

        <view class="section-title"><text>选择暂存站点</text><text>距离优先</text></view>
        <view class="station-card" @tap="chooseStation">
          <view class="station-logo"><view class="cabinet"><view v-for="n in 6" :key="n"></view></view></view>
          <view class="station-copy">
            <view><text>{{ station.name }}</text><text>营业中</text></view>
            <text>{{ station.address }}</text>
            <view><text>距离 326m</text><text>可暂存空位 6 个</text></view>
          </view>
          <uv-icon name="arrow-right" color="#A6B1BE" size="14"></uv-icon>
        </view>

        <view class="section-title"><text>选择暂存时长</text><text>最长 7 天</text></view>
        <view class="duration-grid">
          <view v-for="item in durations" :key="item.days" class="duration-item" :class="{ active: selectedDays === item.days }" @tap="selectedDays = item.days">
            <text>{{ item.days }}天</text>
            <text>{{ item.price }}</text>
            <view v-if="item.tag" class="duration-tag">{{ item.tag }}</view>
          </view>
        </view>

        <view class="section-title"><text>待暂存电池</text><text @tap="chooseBattery">更换</text></view>
        <view class="battery-card">
          <view class="battery-visual"><view></view></view>
          <view class="battery-copy">
            <text>{{ batteryName }}</text>
            <text>编号 GY72V30-09265</text>
            <view><text>电量 68%</text><text>健康度 96%</text></view>
          </view>
          <view class="checked"><uv-icon name="checkmark" color="#FFFFFF" size="13"></uv-icon></view>
        </view>

        <view class="notice-card">
          <view><uv-icon name="info-circle" color="#4A91F2" size="19"></uv-icon><text>暂存须知</text></view>
          <text>暂存期间电池将保持安全断电状态；到期前 2 小时提醒取回，超时按 ¥2/天续费。</text>
        </view>

        <view class="agreement" @tap="agreed = !agreed">
          <view :class="{ active: agreed }"><uv-icon v-if="agreed" name="checkmark" color="#FFFFFF" size="12"></uv-icon></view>
          <text>我已阅读并同意《电池暂存服务协议》</text>
        </view>
        <view class="content-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-bar">
      <view class="price-copy"><text>合计</text><view><text>¥</text><text>{{ totalPrice }}</text></view><text>含智能监测服务</text></view>
      <view class="submit-button" :class="{ disabled: !agreed }" @tap="submitStorage">确认暂存</view>
    </view>
  </view>
</template>

<script>
import { addLocalOrder } from '../../utils/localOrders'

export default {
  data() {
    return {
      selectedDays: 3,
      agreed: true,
      station: { name: '科技园换电站', address: '深圳市南山区科技园南区科苑路15号' },
      batteryName: '72V 30Ah 智能锂电池',
      process: [
        { title: '选择方案', icon: 'calendar' },
        { title: '到站存放', icon: 'map-fill' },
        { title: '按时取回', icon: 'lock-open' }
      ],
      durations: [
        { days: 1, price: '¥2', tag: '' },
        { days: 3, price: '¥5', tag: '推荐' },
        { days: 5, price: '¥8', tag: '' },
        { days: 7, price: '¥10', tag: '更省' }
      ]
    }
  },
  computed: {
    totalPrice() { return ({ 1: '2.00', 3: '5.00', 5: '8.00', 7: '10.00' })[this.selectedDays] }
  },
  methods: {
    goBack() { uni.navigateBack({ fail: () => uni.reLaunch({ url: '/pages/index/index' }) }) },
    chooseStation() {
      const stationList = [
        { name: '科技园换电站', address: '深圳市南山区科技园南区科苑路15号' },
        { name: '深大地铁站换电点', address: '深圳市南山区深南大道深大地铁站A口' }
      ]
      uni.showActionSheet({ itemList: stationList.map(item => item.name), success: ({ tapIndex }) => { this.station = stationList[tapIndex] } })
    },
    chooseBattery() {
      uni.showActionSheet({ itemList: ['GY72V30-10682 · 当前使用中', 'GY72V30-10531 · 备用电池'], success: ({ tapIndex }) => { this.batteryName = tapIndex === 0 ? '72V 30Ah 智能锂电池' : '72V 30Ah 备用锂电池'; uni.showToast({ title: '暂存电池已更新', icon: 'none' }) } })
    },
    showStorageGuide() { uni.showModal({ title: '电池暂存说明', content: '到站后扫描换电柜二维码，按指引将电池存入指定仓门；到期前可在订单中申请取回。', showCancel: false }) },
    submitStorage() {
      if (!this.agreed) return uni.showToast({ title: '请先同意服务协议', icon: 'none' })
      const order = addLocalOrder({ category: 'storage', station: this.station.name, detail: `${this.selectedDays} 天暂存 · ${this.batteryName}`, status: '暂存中', amount: `¥${this.totalPrice}`, action: '立即取回', primary: true })
      uni.showModal({ title: '暂存预约已提交', content: `请在 30 分钟内到 ${this.station.name} 完成电池暂存。`, showCancel: false, success: () => uni.navigateTo({ url: `/pages/order/detail?no=${order.no}&category=storage` }) })
    }
  }
}
</script>

<style scoped>
.page-shell, .page-scroll { width: 100%; height: 100%; background: #F5F8FC; }
.page-shell { position: relative; overflow: hidden; }
.storage-page { min-height: 100%; padding: 0 28rpx; color: #283344; background: linear-gradient(180deg, #F8FBFF 0, #F5F8FC 350rpx); }
.nav-bar { height: calc(var(--status-bar-height, 0px) + 100rpx); padding-top: var(--status-bar-height, 0px); display: flex; align-items: center; justify-content: space-between; }
.nav-button { width: 62rpx; height: 62rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #FFFFFF; box-shadow: 0 7rpx 20rpx rgba(42,71,103,.08); }
.nav-title { font-size: 31rpx; font-weight: 700; }
.hero-card { height: 162rpx; position: relative; overflow: hidden; padding: 0 27rpx; display: flex; align-items: center; border-radius: 27rpx; color: #FFFFFF; background: linear-gradient(120deg, #3285F0, #4A9DF8); box-shadow: 0 15rpx 33rpx rgba(49,132,239,.2); }
.hero-icon { width: 78rpx; height: 78rpx; flex: 0 0 78rpx; display: flex; align-items: center; justify-content: center; border: 7rpx solid rgba(255,255,255,.22); border-radius: 27rpx; background: rgba(255,255,255,.16); }
.hero-copy { margin-left: 20rpx; display: flex; flex-direction: column; }
.hero-copy text:first-child { font-size: 29rpx; font-weight: 700; }
.hero-copy text:last-child { margin-top: 10rpx; color: #E0EDFF; font-size: 22rpx; }
.hero-glow { width: 210rpx; height: 210rpx; position: absolute; right: -86rpx; bottom: -130rpx; border: 2rpx solid rgba(255,255,255,.17); border-radius: 50%; }
.process-card { margin-top: 20rpx; padding: 22rpx 25rpx 18rpx; display: flex; border: 2rpx solid #EDF1F6; border-radius: 23rpx; background: #FFFFFF; }
.process-item { min-width: 0; position: relative; flex: 1; display: flex; align-items: center; flex-direction: column; color: #7F8D9F; font-size: 21rpx; }
.process-icon { width: 48rpx; height: 48rpx; position: relative; z-index: 2; display: flex; align-items: center; justify-content: center; border-radius: 16rpx; background: #EDF3FA; }
.process-icon.active { background: #3C8DF4; box-shadow: 0 7rpx 16rpx rgba(60,141,244,.22); }
.process-item > text { margin-top: 9rpx; }
.process-line { height: 2rpx; position: absolute; z-index: 1; left: 67%; right: -33%; top: 24rpx; background: #DCE5EF; }
.section-title { margin: 28rpx 4rpx 14rpx; display: flex; align-items: center; justify-content: space-between; }
.section-title > text:first-child { font-size: 28rpx; font-weight: 700; }
.section-title > text:last-child { color: #8F9BAB; font-size: 21rpx; }
.station-card, .battery-card { padding: 20rpx 21rpx; display: flex; align-items: center; border: 2rpx solid #EEF1F5; border-radius: 23rpx; background: #FFFFFF; box-shadow: 0 8rpx 24rpx rgba(40,66,96,.04); }
.station-logo { width: 76rpx; height: 76rpx; flex: 0 0 76rpx; display: flex; align-items: center; justify-content: center; border-radius: 20rpx; background: #E8F3FF; }
.cabinet { width: 38rpx; height: 44rpx; padding: 5rpx; display: flex; flex-wrap: wrap; gap: 4rpx; border: 3rpx solid #398BF4; border-radius: 7rpx; }
.cabinet view { width: calc(50% - 2rpx); height: calc(33.333% - 3rpx); border-radius: 2rpx; background: #398BF4; }
.station-copy { min-width: 0; margin-left: 15rpx; display: flex; flex: 1; flex-direction: column; }
.station-copy > view:first-child { display: flex; align-items: center; gap: 8rpx; }
.station-copy > view:first-child text:first-child { min-width: 0; overflow: hidden; font-size: 25rpx; font-weight: 600; text-overflow: ellipsis; white-space: nowrap; }
.station-copy > view:first-child text:last-child { padding: 3rpx 7rpx; flex-shrink: 0; border-radius: 6rpx; color: #28A97A; background: #E6F8F1; font-size: 15rpx; }
.station-copy > text { margin-top: 7rpx; overflow: hidden; color: #919DAC; font-size: 20rpx; text-overflow: ellipsis; white-space: nowrap; }
.station-copy > view:last-child { margin-top: 8rpx; display: flex; color: #66768A; font-size: 20rpx; }
.station-copy > view:last-child text + text { margin-left: 13rpx; padding-left: 13rpx; border-left: 2rpx solid #E5E9EE; }
.duration-grid { display: flex; gap: 12rpx; }
.duration-item { height: 107rpx; min-width: 0; position: relative; flex: 1; display: flex; align-items: center; justify-content: center; flex-direction: column; border: 2rpx solid #E4E9F0; border-radius: 20rpx; color: #384557; background: #FFFFFF; }
.duration-item > text:first-child { font-size: 25rpx; font-weight: 600; }
.duration-item > text:nth-child(2) { margin-top: 7rpx; color: #8B97A7; font-size: 21rpx; }
.duration-item.active { border-color: #80B7F9; color: #3187F2; background: #EBF4FF; box-shadow: 0 7rpx 18rpx rgba(49,135,242,.1); }
.duration-item.active > text:nth-child(2) { color: #3187F2; }
.duration-tag { position: absolute; right: -2rpx; top: -2rpx; padding: 3rpx 8rpx; border-radius: 0 18rpx 0 10rpx; color: #FFFFFF; background: #F19B54; font-size: 14rpx; }
.battery-visual { width: 58rpx; height: 82rpx; position: relative; flex: 0 0 58rpx; padding: 7rpx; border: 4rpx solid #398AF4; border-radius: 9rpx; }
.battery-visual:after { content: ''; width: 14rpx; height: 6rpx; position: absolute; left: 18rpx; top: -9rpx; border-radius: 4rpx 4rpx 0 0; background: #398AF4; }
.battery-visual view { width: 100%; height: 68%; position: absolute; left: 0; bottom: 0; background: #57A1F8; }
.battery-copy { min-width: 0; margin-left: 17rpx; display: flex; flex: 1; flex-direction: column; }
.battery-copy > text:first-child { font-size: 25rpx; font-weight: 600; }
.battery-copy > text:nth-child(2) { margin-top: 7rpx; color: #909CAB; font-size: 20rpx; }
.battery-copy > view { margin-top: 9rpx; display: flex; gap: 18rpx; color: #4B7FBF; font-size: 20rpx; }
.checked { width: 35rpx; height: 35rpx; flex: 0 0 35rpx; display: flex; align-items: center; justify-content: center; border-radius: 50%; background: #398BF4; }
.notice-card { margin-top: 20rpx; padding: 20rpx 21rpx; border-radius: 22rpx; background: #EBF4FF; }
.notice-card > view { display: flex; align-items: center; gap: 8rpx; color: #426A9A; font-size: 23rpx; font-weight: 600; }
.notice-card > text { margin-top: 10rpx; display: block; color: #7088A3; font-size: 20rpx; line-height: 1.6; }
.agreement { margin: 21rpx 3rpx 0; display: flex; align-items: center; color: #7F8C9D; font-size: 20rpx; }
.agreement > view { width: 31rpx; height: 31rpx; flex: 0 0 31rpx; display: flex; align-items: center; justify-content: center; border: 2rpx solid #C9D2DD; border-radius: 9rpx; }
.agreement > view.active { border-color: #398BF4; background: #398BF4; }
.agreement > text { margin-left: 9rpx; }
.content-spacer { height: calc(170rpx + env(safe-area-inset-bottom)); }
.bottom-bar { height: calc(132rpx + env(safe-area-inset-bottom)); padding: 15rpx 28rpx env(safe-area-inset-bottom); position: absolute; z-index: 5; left: 0; right: 0; bottom: 0; display: flex; align-items: center; background: rgba(255,255,255,.98); box-shadow: 0 -6rpx 24rpx rgba(35,61,91,.08); }
.price-copy { min-width: 0; display: flex; align-items: baseline; flex-wrap: wrap; color: #7F8C9D; font-size: 20rpx; }
.price-copy > view { margin-left: 9rpx; color: #3187F2; }
.price-copy > view text:first-child { font-size: 23rpx; }
.price-copy > view text:last-child { font-size: 36rpx; font-weight: 700; }
.price-copy > text:last-child { flex-basis: 100%; margin-top: 4rpx; color: #9AA5B3; font-size: 15rpx; }
.submit-button { width: 250rpx; height: 86rpx; margin-left: auto; flex: 0 0 250rpx; display: flex; align-items: center; justify-content: center; border-radius: 24rpx; color: #FFFFFF; background: linear-gradient(135deg, #4A9DF8, #3185F0); box-shadow: 0 10rpx 22rpx rgba(49,133,240,.22); font-size: 25rpx; font-weight: 600; }
.submit-button.disabled { opacity: .5; }
@media screen and (max-width: 360px) {
  .storage-page { padding-right: 22rpx; padding-left: 22rpx; }
  .duration-grid { gap: 8rpx; }
  .duration-item { height: 96rpx; }
  .station-copy > view:last-child { font-size: 15rpx; }
  .bottom-bar { padding-right: 22rpx; padding-left: 22rpx; }
  .submit-button { width: 215rpx; flex-basis: 215rpx; }
}

/* visual polish */
.storage-page{padding-right:30rpx;padding-left:30rpx;background:linear-gradient(180deg,#FAFCFF,#F5F8FC 360rpx)}
.nav-button{width:62rpx;height:62rpx;border-radius:21rpx;box-shadow:0 8rpx 22rpx rgba(39,69,104,.07)}
.hero-card{min-height:166rpx;padding:27rpx;border-radius:29rpx;box-shadow:0 16rpx 36rpx rgba(49,132,239,.2)}
.process-card{padding:24rpx 20rpx;border-radius:26rpx;box-shadow:var(--gy-card-shadow)}
.station-card,.battery-card{padding:24rpx;border-radius:26rpx;box-shadow:var(--gy-card-shadow)}
.duration-item{height:104rpx;border-radius:22rpx}.notice-card{padding:23rpx;border-radius:24rpx}
.submit-button{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.storage-page{padding-right:22rpx;padding-left:22rpx}.hero-card{min-height:158rpx}.station-card,.battery-card{padding:20rpx}.duration-item{height:98rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;background:rgba(248,251,255,.97);box-shadow:0 8rpx 22rpx rgba(37,66,99,.08);backdrop-filter:blur(18rpx)}
.storage-page{padding-top:calc(var(--status-bar-height, 0px) + 100rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.storage-page{padding-right:22rpx;padding-left:22rpx}}



/* Flat storage flow: active state relies on blue fill/border, not elevation. */
.nav-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.hero-card{box-shadow:var(--gy-shadow-brand)}
.process-icon.active,.duration-item.active{box-shadow:none}
.process-card,.station-card,.battery-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.submit-button{box-shadow:var(--gy-shadow-brand)}



/* Adversarial UI audit: remove ornamental elevation and enlarge compact actions. */
.fixed-page-header{padding-right:28rpx;padding-left:28rpx;background:rgba(248,251,255,.97);box-shadow:none;border-bottom:2rpx solid var(--gy-surface-border)}
.nav-button{width:88rpx;height:88rpx;flex:0 0 88rpx;border:0;background:transparent;box-shadow:none}
.station-card,.storage-option,.notice-card,.service-notice{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.option-card{min-height:104rpx}
.bottom-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.submit-button{min-height:88rpx;box-shadow:var(--gy-shadow-brand)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}}



.station-copy>view:first-child text:last-child{font-size:20rpx;line-height:1.35}
.station-copy>text,.station-copy>view:last-child,.battery-copy>text:nth-child(2),.battery-copy>view,.notice-card>text{font-size:21rpx;line-height:1.45}
.duration-tag{min-height:34rpx;padding:4rpx 9rpx;display:flex;align-items:center;font-size:19rpx;line-height:1}
.price-copy>text:last-child{font-size:20rpx;line-height:1.35}
@media screen and (max-width:360px){.station-copy>view:last-child{font-size:20rpx}.duration-tag{font-size:18rpx}}

</style>
