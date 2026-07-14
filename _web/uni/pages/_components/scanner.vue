<template>
  <scroll-view class="scanner-scroll" scroll-y :show-scrollbar="false">
    <view class="scanner-page">
      <view class="scanner-glow glow-one"></view>
      <view class="scanner-glow glow-two"></view>

      <view class="scanner-header">
        <view class="header-placeholder"></view>
        <view class="scanner-title-copy">
          <text>扫码换电</text>
          <text>对准换电柜二维码即可识别</text>
        </view>
        <view class="scanner-help" @tap="showScanGuide">
          <uv-icon name="question-circle" color="#D9E8FA" size="21"></uv-icon>
        </view>
      </view>

      <view class="scan-area">
        <view class="scan-frame">
          <view class="corner tl"></view>
          <view class="corner tr"></view>
          <view class="corner bl"></view>
          <view class="corner br"></view>
          <view class="scan-line"></view>
          <view class="qr-pattern" aria-label="二维码识别区域">
            <view v-for="(cell, index) in qrCells" :key="index" :class="{ dark: cell }"></view>
          </view>
        </view>
        <text class="scan-tip">请保持二维码完整并置于框内</text>
      </view>

      <view class="scanner-actions">
        <view @tap="toggleTorch">
          <view :class="{ selected: torchOn }"><uv-icon name="eye" color="#D6E7F9" size="22"></uv-icon></view>
          <text>{{ torchOn ? '关闭照明' : '打开照明' }}</text>
        </view>
        <view @tap="startScan">
          <view class="primary-scan"><uv-icon name="scan" color="#FFFFFF" size="31"></uv-icon></view>
          <text>开始扫码</text>
        </view>
        <view @tap="startScan">
          <view><uv-icon name="photo" color="#D6E7F9" size="22"></uv-icon></view>
          <text>相册识别</text>
        </view>
      </view>

      <view class="scan-notice">
        <view class="notice-icon"><uv-icon name="info-circle" color="#63ACFF" size="20"></uv-icon></view>
        <view class="notice-copy">
          <text>换电操作提示</text>
          <text>请先归还旧电池，再领取满电电池</text>
        </view>
      </view>

      <view class="scanner-links">
        <view @tap="manualDeviceEntry">
          <text>手动输入设备编号</text>
          <uv-icon name="arrow-right" color="#B6C7DA" size="12"></uv-icon>
        </view>
        <view class="link-line"></view>
        <view @tap="openRepair">
          <text>换电遇到问题？</text>
          <uv-icon name="arrow-right" color="#B6C7DA" size="12"></uv-icon>
        </view>
      </view>
      <view class="scanner-bottom-spacer"></view>
    </view>
  </scroll-view>
</template>

<script>
export default {
  name: 'EnergyScanner',
  data() {
    return {
      torchOn: false,
      qrCells: [1,1,1,0,1, 1,0,1,1,0, 1,1,1,0,1, 0,1,0,1,1, 1,0,1,1,1]
    }
  },
  methods: {
    startScan() {
      // #ifdef H5
      uni.navigateTo({ url: '/pages/swap/success' })
      // #endif
      // #ifndef H5
      uni.scanCode({
        onlyFromCamera: false,
        success: () => uni.navigateTo({ url: '/pages/swap/success' }),
        fail: () => uni.showToast({ title: '已取消扫码', icon: 'none' })
      })
      // #endif
    },
    toggleTorch() {
      this.torchOn = !this.torchOn
      uni.showToast({ title: this.torchOn ? '照明已开启，请对准柜机二维码' : '照明已关闭', icon: 'none' })
    },
    manualDeviceEntry() {
      uni.showActionSheet({
        itemList: ['GY-KJY-018 · 科技园换电站', 'GY-SD-023 · 深大地铁站换电点'],
        success: () => uni.navigateTo({ url: '/pages/swap/success' })
      })
    },
    showScanGuide() {
      uni.showModal({ title: '扫码换电指引', content: '1. 将车辆停稳\n2. 扫描换电柜二维码\n3. 按柜机提示归还并领取电池', showCancel: false })
    },
    openRepair() { uni.navigateTo({ url: '/pages/repair/create' }) }
  }
}
</script>

<style scoped>
.scanner-scroll { width: 100%; height: 100%; background: #101A2B; }
.scanner-page { min-height: 100%; position: relative; overflow: hidden; padding: 0 0 1rpx; color: #FFFFFF; background: linear-gradient(155deg, #17243A 0%, #101A2B 58%, #172842 100%); }
.scanner-glow { position: absolute; border-radius: 50%; filter: blur(4rpx); }
.glow-one { width: 420rpx; height: 420rpx; left: -220rpx; top: 180rpx; background: rgba(52,139,245,.12); }
.glow-two { width: 360rpx; height: 360rpx; right: -220rpx; bottom: 130rpx; background: rgba(72,158,255,.09); }
.scanner-header { position: relative; z-index: 2; padding: calc(var(--status-bar-height, 0px) + 42rpx) 28rpx 0; display: flex; align-items: center; justify-content: space-between; }
.header-placeholder, .scanner-help { width: 58rpx; height: 58rpx; flex: 0 0 58rpx; }
.scanner-help { display: flex; align-items: center; justify-content: center; border: 1rpx solid rgba(255,255,255,.12); border-radius: 50%; background: rgba(255,255,255,.06); }
.scanner-title-copy { min-width: 0; display: flex; align-items: center; flex-direction: column; }
.scanner-title-copy text:first-child { font-size: 31rpx; font-weight: 700; }
.scanner-title-copy text:last-child { margin-top: 9rpx; color: #9EB0C5; font-size: 22rpx; white-space: nowrap; }
.scan-area { position: relative; z-index: 2; margin-top: 54rpx; display: flex; align-items: center; flex-direction: column; }
.scan-frame { width: 420rpx; height: 420rpx; position: relative; display: flex; align-items: center; justify-content: center; border: 2rpx solid rgba(255,255,255,.1); border-radius: 34rpx; background: rgba(255,255,255,.035); box-shadow: 0 0 80rpx rgba(45,132,239,.1) inset; }
.corner { width: 62rpx; height: 62rpx; position: absolute; border-color: #4EA2FF; border-style: solid; }
.corner.tl { left: -3rpx; top: -3rpx; border-width: 7rpx 0 0 7rpx; border-radius: 25rpx 0 0; }
.corner.tr { right: -3rpx; top: -3rpx; border-width: 7rpx 7rpx 0 0; border-radius: 0 25rpx 0 0; }
.corner.bl { left: -3rpx; bottom: -3rpx; border-width: 0 0 7rpx 7rpx; border-radius: 0 0 0 25rpx; }
.corner.br { right: -3rpx; bottom: -3rpx; border-width: 0 7rpx 7rpx 0; border-radius: 0 0 25rpx; }
.scan-line { width: 350rpx; height: 3rpx; position: absolute; z-index: 2; left: 35rpx; top: 92rpx; background: linear-gradient(90deg, transparent, #4EA2FF, transparent); box-shadow: 0 0 18rpx #4EA2FF; animation: scanMove 2.4s ease-in-out infinite; }
.qr-pattern { width: 180rpx; height: 180rpx; padding: 14rpx; display: flex; flex-wrap: wrap; gap: 6rpx; border-radius: 18rpx; background: rgba(255,255,255,.94); opacity: .22; }
.qr-pattern view { width: calc(20% - 5rpx); height: calc(20% - 5rpx); border-radius: 2rpx; }
.qr-pattern view.dark { background: #17243A; }
.scan-tip { margin-top: 28rpx; color: #B8C6D7; font-size: 24rpx; }
.scanner-actions { position: relative; z-index: 2; margin: 47rpx auto 0; display: flex; align-items: flex-start; justify-content: center; gap: 54rpx; }
.scanner-actions > view { width: 105rpx; display: flex; align-items: center; flex-direction: column; color: #B4C3D5; font-size: 21rpx; }
.scanner-actions > view > view { width: 66rpx; height: 66rpx; display: flex; align-items: center; justify-content: center; border: 1rpx solid rgba(255,255,255,.14); border-radius: 23rpx; background: rgba(255,255,255,.08); }
.scanner-actions > view > view.selected { border-color: rgba(77,158,251,.62); background: rgba(58,139,236,.24); }
.scanner-actions > view > view.primary-scan { width: 92rpx; height: 92rpx; margin-top: -13rpx; border: 7rpx solid rgba(255,255,255,.16); border-radius: 31rpx; background: linear-gradient(145deg, #4CA1FA, #3186F0); box-shadow: 0 12rpx 28rpx rgba(42,126,230,.32); }
.scanner-actions text { margin-top: 13rpx; white-space: nowrap; }
.scan-notice { position: relative; z-index: 2; margin: 43rpx 35rpx 0; padding: 21rpx 22rpx; display: flex; align-items: center; border: 1rpx solid rgba(83,155,240,.18); border-radius: 22rpx; background: rgba(61,132,216,.11); }
.notice-icon { width: 50rpx; height: 50rpx; flex: 0 0 50rpx; display: flex; align-items: center; justify-content: center; border-radius: 16rpx; background: rgba(75,156,250,.13); }
.notice-copy { min-width: 0; margin-left: 14rpx; display: flex; flex-direction: column; }
.notice-copy text:first-child { font-size: 24rpx; font-weight: 600; }
.notice-copy text:last-child { margin-top: 7rpx; overflow: hidden; color: #A8B9CD; font-size: 21rpx; text-overflow: ellipsis; white-space: nowrap; }
.scanner-links { position: relative; z-index: 2; margin-top: 24rpx; display: flex; align-items: center; justify-content: center; color: #B6C7DA; font-size: 21rpx; }
.scanner-links > view:not(.link-line) { display: flex; align-items: center; gap: 4rpx; white-space: nowrap; }
.link-line { width: 1rpx; height: 20rpx; margin: 0 23rpx; background: rgba(255,255,255,.18); }
.scanner-bottom-spacer { height: calc(154rpx + env(safe-area-inset-bottom)); }
@keyframes scanMove { 0%, 100% { transform: translateY(0); opacity: .55; } 50% { transform: translateY(235rpx); opacity: 1; } }
@media screen and (max-width: 360px) {
  .scan-frame { width: 370rpx; height: 370rpx; }
  .scan-line { width: 310rpx; left: 30rpx; }
  .scanner-actions { gap: 31rpx; }
  .scan-notice { margin-right: 25rpx; margin-left: 25rpx; }
  .scanner-title-copy text:last-child { font-size: 20rpx; }
}
@media screen and (max-height: 700px) {
  .scanner-header { padding-top: calc(var(--status-bar-height, 0px) + 25rpx); }
  .scan-area { margin-top: 28rpx; }
  .scan-frame { width: 340rpx; height: 340rpx; }
  .scan-line { width: 280rpx; left: 30rpx; top: 70rpx; }
  .scanner-actions { margin-top: 26rpx; }
  .scan-notice { margin-top: 25rpx; }
  .scanner-links { margin-top: 15rpx; }
  @keyframes scanMove { 0%, 100% { transform: translateY(0); opacity: .55; } 50% { transform: translateY(195rpx); opacity: 1; } }
}

/* visual polish */
.scanner-page{background:radial-gradient(circle at 50% 38%,rgba(49,133,240,.13),transparent 38%),linear-gradient(155deg,#17243A 0%,#101A2B 58%,#172842 100%)}
.scanner-help{width:62rpx;height:62rpx;border-radius:21rpx;background:rgba(255,255,255,.075)}
.scan-frame{border-radius:38rpx;box-shadow:0 0 95rpx rgba(45,132,239,.13) inset,0 22rpx 70rpx rgba(3,12,28,.16)}
.scanner-actions>view>view{width:70rpx;height:70rpx;border-radius:24rpx}.scanner-actions>view>view.primary-scan{width:96rpx;height:96rpx;border-radius:33rpx;box-shadow:0 14rpx 34rpx rgba(42,126,230,.38)}
.scan-notice{padding:24rpx;border-radius:25rpx;background:rgba(61,132,216,.13)}
@media screen and (max-height:700px){.scan-notice{padding:20rpx 22rpx}.scanner-actions>view>view.primary-scan{width:90rpx;height:90rpx}}



/* Keep scanner controls available whenever the fallback content scrolls. */
.scanner-header{position:sticky;z-index:8;top:0;margin-top:calc(-1 * (var(--status-bar-height,0px) + 42rpx));padding:calc(var(--status-bar-height,0px) + 42rpx) 28rpx 14rpx;background:linear-gradient(180deg,rgba(23,36,58,.98) 0%,rgba(23,36,58,.88) 76%,rgba(23,36,58,0) 100%);backdrop-filter:blur(18rpx)}
@media screen and (max-width:360px){.scanner-header{margin-top:calc(-1 * (var(--status-bar-height,0px) + 25rpx));padding:calc(var(--status-bar-height,0px) + 25rpx) 22rpx 12rpx}}



/* Scanner glow remains functional but is deliberately restrained to avoid a skeuomorphic look. */
.scan-frame{box-shadow:0 0 48rpx rgba(45,132,239,.08) inset,0 8rpx 24rpx rgba(3,12,28,.08)}
.scan-line{box-shadow:0 0 10rpx rgba(78,162,255,.65)}
.scanner-actions>view>view.primary-scan{box-shadow:var(--gy-shadow-brand)}

</style>
