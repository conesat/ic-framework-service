<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#283344" size="21"></uv-icon></view>
          <text class="nav-title">订单详情</text>
          <view class="nav-button" @tap="openMoreActions"><uv-icon name="more-dot-fill" color="#283344" size="20"></uv-icon></view>
        </view>

    <scroll-view class="detail-scroll" scroll-y :show-scrollbar="false">
      <view class="detail-page">
        

        <view class="status-card">
          <view class="status-icon"><uv-icon name="checkmark" color="#FFFFFF" size="26"></uv-icon></view>
          <view class="status-copy">
            <text>换电完成</text>
            <text>电池已成功领取，祝您一路顺风</text>
          </view>
          <view class="status-decoration"></view>
        </view>

        <view class="benefit-card">
          <view class="benefit-icon"><uv-icon name="coupon" color="#418AF0" size="21"></uv-icon></view>
          <view class="benefit-copy">
            <text>本次使用月卡权益</text>
            <text>套餐内换电免单，已为您节省 ¥5.00</text>
          </view>
          <text class="benefit-price">¥0.00</text>
        </view>

        <view class="section-card progress-card">
          <view class="section-title"><text>换电进度</text><text>用时 1分18秒</text></view>
          <view class="timeline">
            <view v-for="(step, index) in steps" :key="step.title" class="timeline-item">
              <view class="timeline-axis">
                <view class="timeline-dot" :class="{ current: index === steps.length - 1 }"><uv-icon name="checkmark" color="#FFFFFF" size="11"></uv-icon></view>
                <view v-if="index < steps.length - 1" class="timeline-line"></view>
              </view>
              <view class="timeline-copy">
                <text>{{ step.title }}</text>
                <text>{{ step.desc }}</text>
              </view>
              <text class="timeline-time">{{ step.time }}</text>
            </view>
          </view>
        </view>

        <view class="section-card battery-card">
          <view class="section-title"><text>电池信息</text><view @tap="showBatteryReport">检测报告 <uv-icon name="arrow-right" color="#9AA6B5" size="12"></uv-icon></view></view>
          <view class="battery-swap">
            <view class="battery-item old">
              <view class="battery-visual"><view></view></view>
              <text>归还电池</text>
              <text>GY72V30-06182</text>
              <text>剩余 18%</text>
            </view>
            <view class="swap-arrow"><uv-icon name="arrow-rightward" color="#4A91F2" size="25"></uv-icon></view>
            <view class="battery-item new">
              <view class="battery-visual"><view></view></view>
              <text>领取电池</text>
              <text>GY72V30-09265</text>
              <text>电量 100%</text>
            </view>
          </view>
        </view>

        <view class="section-card info-card">
          <view class="section-title"><text>订单信息</text></view>
          <view class="info-row"><text>换电站点</text><view @tap="openStation"><text>科技园换电站</text><uv-icon name="arrow-right" color="#A6B0BC" size="12"></uv-icon></view></view>
          <view class="info-row"><text>订单编号</text><text class="long-value">{{ orderNo }}</text></view>
          <view class="info-row"><text>创建时间</text><text>2026-07-13 09:26:18</text></view>
          <view class="info-row"><text>完成时间</text><text>2026-07-13 09:27:36</text></view>
          <view class="info-row"><text>支付方式</text><text>月卡权益</text></view>
          <view class="info-row amount-row"><text>订单金额</text><text>¥0.00</text></view>
        </view>

        <view class="service-tip">
          <uv-icon name="kefu-ermai" color="#4A91F2" size="20"></uv-icon>
          <view><text>换电后发现异常？</text><text>请在 30 分钟内联系客服，我们将优先为您处理</text></view>
        </view>
        <view class="content-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-actions">
      <view class="secondary" @tap="applyAfterSales">申请售后</view>
      <view class="primary" @tap="contactService"><uv-icon name="kefu-ermai" color="#FFFFFF" size="18"></uv-icon><text>联系客服</text></view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderNo: 'HD202607130018',
      category: 'swap',
      steps: [
        { title: '扫码开柜', desc: '设备编号 GY-KJY-018', time: '09:26:18' },
        { title: '归还旧电池', desc: '仓门 06 · 检测正常', time: '09:26:42' },
        { title: '领取满电电池', desc: '仓门 12 · 电量 100%', time: '09:27:21' },
        { title: '订单完成', desc: '柜门已关闭', time: '09:27:36' }
      ]
    }
  },
  onLoad(options) { if (options?.no) this.orderNo = options.no; if (options?.category) this.category = options.category },
  methods: {
    goBack() { uni.navigateBack({ fail: () => uni.reLaunch({ url: '/pages/index/index?tab=3' }) }) },
    openStation() { uni.navigateTo({ url: '/pages/station/detail' }) },
    contactService() { uni.makePhoneCall({ phoneNumber: '4008888899', fail: () => uni.showToast({ title: '请拨打 400-888-8899', icon: 'none' }) }) },
    openMoreActions() {
      uni.showActionSheet({
        itemList: ['复制订单编号', '联系客服'],
        success: ({ tapIndex }) => {
          if (tapIndex === 0) return uni.setClipboardData({ data: this.orderNo, success: () => uni.showToast({ title: '订单编号已复制', icon: 'success' }) })
          this.contactService()
        }
      })
    },
    showBatteryReport() {
      uni.showModal({ title: '电池检测报告', content: '健康度：99%\n电芯温度：28°C\n充放电状态：正常\n安全检测：未发现异常', showCancel: false })
    },
    applyAfterSales() {
      uni.navigateTo({ url: `/pages/repair/create?orderNo=${this.orderNo}` })
    }
  }
}
</script>

<style scoped>
.page-shell, .detail-scroll { width: 100%; height: 100%; background: #F5F8FC; }
.page-shell { position: relative; overflow: hidden; }
.detail-page { min-height: 100%; padding: 0 28rpx; color: #283344; background: linear-gradient(180deg, #F7FAFF 0, #F5F8FC 340rpx); }
.nav-bar { height: calc(var(--status-bar-height, 0px) + 100rpx); padding-top: var(--status-bar-height, 0px); display: flex; align-items: center; justify-content: space-between; }
.nav-button { width: 62rpx; height: 62rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #FFFFFF; box-shadow: 0 7rpx 20rpx rgba(42,71,103,.08); }
.nav-title { font-size: 31rpx; font-weight: 700; }
.status-card { height: 154rpx; position: relative; overflow: hidden; padding: 0 28rpx; display: flex; align-items: center; border-radius: 27rpx; color: #FFFFFF; background: linear-gradient(120deg, #3387F1, #4A9DF8); box-shadow: 0 15rpx 33rpx rgba(49,132,239,.21); }
.status-icon { width: 70rpx; height: 70rpx; flex: 0 0 70rpx; display: flex; align-items: center; justify-content: center; border: 6rpx solid rgba(255,255,255,.25); border-radius: 24rpx; background: rgba(255,255,255,.18); }
.status-copy { margin-left: 20rpx; display: flex; flex-direction: column; }
.status-copy text:first-child { font-size: 31rpx; font-weight: 700; }
.status-copy text:last-child { margin-top: 10rpx; color: #E2EFFF; font-size: 23rpx; }
.status-decoration { width: 180rpx; height: 180rpx; position: absolute; right: -67rpx; bottom: -107rpx; border: 2rpx solid rgba(255,255,255,.17); border-radius: 50%; }
.benefit-card { margin-top: 20rpx; padding: 20rpx 22rpx; display: flex; align-items: center; border: 2rpx solid #E7EEF7; border-radius: 22rpx; background: #FFFFFF; }
.benefit-icon { width: 52rpx; height: 52rpx; flex: 0 0 52rpx; display: flex; align-items: center; justify-content: center; border-radius: 17rpx; background: #EAF3FF; }
.benefit-copy { min-width: 0; margin-left: 13rpx; display: flex; flex: 1; flex-direction: column; }
.benefit-copy text:first-child { font-size: 24rpx; font-weight: 600; }
.benefit-copy text:last-child { margin-top: 5rpx; color: #8C98A8; font-size: 21rpx; }
.benefit-price { margin-left: 10rpx; color: #3589F3; font-size: 28rpx; font-weight: 700; }
.section-card { margin-top: 20rpx; padding: 23rpx 22rpx; border: 2rpx solid #EEF1F5; border-radius: 24rpx; background: #FFFFFF; box-shadow: 0 8rpx 25rpx rgba(40,66,96,.04); }
.section-title { display: flex; align-items: center; justify-content: space-between; }
.section-title > text:first-child { font-size: 28rpx; font-weight: 700; }
.section-title > text:last-child, .section-title > view { display: flex; align-items: center; gap: 4rpx; color: #8F9BAB; font-size: 21rpx; }
.timeline { margin-top: 25rpx; }
.timeline-item { min-height: 83rpx; position: relative; display: flex; }
.timeline-axis { width: 34rpx; flex: 0 0 34rpx; display: flex; align-items: center; flex-direction: column; }
.timeline-dot { width: 28rpx; height: 28rpx; z-index: 2; display: flex; align-items: center; justify-content: center; border: 4rpx solid #D8E9FF; border-radius: 50%; background: #4A91F2; }
.timeline-dot.current { border-color: #CFE3FF; background: #3388F4; box-shadow: 0 0 0 7rpx #EDF5FF; }
.timeline-line { width: 2rpx; flex: 1; background: #DDE8F5; }
.timeline-copy { min-width: 0; margin-left: 13rpx; display: flex; flex: 1; flex-direction: column; }
.timeline-copy text:first-child { color: #344153; font-size: 24rpx; font-weight: 600; }
.timeline-copy text:last-child { margin-top: 6rpx; overflow: hidden; color: #929EAD; font-size: 21rpx; text-overflow: ellipsis; white-space: nowrap; }
.timeline-time { margin-left: 8rpx; color: #9AA5B3; font-size: 20rpx; }
.battery-swap { margin-top: 25rpx; display: flex; align-items: center; justify-content: space-around; }
.battery-item { min-width: 0; display: flex; align-items: center; flex: 1; flex-direction: column; }
.battery-visual { width: 62rpx; height: 88rpx; position: relative; padding: 8rpx; border: 4rpx solid #8F9BAB; border-radius: 10rpx; }
.battery-visual:after { content: ''; width: 15rpx; height: 6rpx; position: absolute; left: 19rpx; top: -9rpx; border-radius: 4rpx 4rpx 0 0; background: #8F9BAB; }
.battery-visual view { width: 100%; height: 24%; position: absolute; left: 0; bottom: 0; border-radius: 4rpx; background: #F0A05D; }
.battery-item.new .battery-visual { border-color: #398AF4; }
.battery-item.new .battery-visual:after { background: #398AF4; }
.battery-item.new .battery-visual view { height: 100%; background: #56A1F8; }
.battery-item > text:nth-child(2) { margin-top: 13rpx; font-size: 23rpx; font-weight: 600; }
.battery-item > text:nth-child(3) { max-width: 100%; margin-top: 7rpx; overflow: hidden; color: #8996A6; font-size: 20rpx; text-overflow: ellipsis; white-space: nowrap; }
.battery-item > text:nth-child(4) { margin-top: 5rpx; color: #E38E45; font-size: 20rpx; }
.battery-item.new > text:nth-child(4) { color: #2D9B77; }
.swap-arrow { width: 58rpx; height: 58rpx; flex: 0 0 58rpx; display: flex; align-items: center; justify-content: center; border-radius: 50%; background: #EDF5FF; }
.info-card { padding-bottom: 11rpx; }
.info-row { min-height: 70rpx; display: flex; align-items: center; justify-content: space-between; border-bottom: 2rpx solid #F1F3F6; color: #8A96A6; font-size: 23rpx; }
.info-row > text:last-child, .info-row > view { max-width: 68%; color: #3F4C5E; text-align: right; }
.info-row > view { display: flex; align-items: center; gap: 5rpx; }
.info-row .long-value { overflow-wrap: anywhere; word-break: break-all; }
.info-row.amount-row { border-bottom: 0; }
.amount-row > text:last-child { color: #2F86F2; font-size: 28rpx; font-weight: 700; }
.service-tip { margin-top: 20rpx; padding: 20rpx 22rpx; display: flex; align-items: center; border-radius: 22rpx; background: #EAF3FF; }
.service-tip > view { min-width: 0; margin-left: 13rpx; display: flex; flex-direction: column; }
.service-tip text:first-child { color: #3D5879; font-size: 23rpx; font-weight: 600; }
.service-tip text:last-child { margin-top: 5rpx; color: #7F94AC; font-size: 20rpx; }
.content-spacer { height: calc(174rpx + env(safe-area-inset-bottom)); }
.bottom-actions { height: calc(132rpx + env(safe-area-inset-bottom)); padding: 17rpx 28rpx env(safe-area-inset-bottom); position: absolute; z-index: 5; left: 0; right: 0; bottom: 0; display: flex; gap: 16rpx; background: rgba(255,255,255,.98); box-shadow: 0 -6rpx 24rpx rgba(35,61,91,.08); }
.bottom-actions > view { height: 86rpx; display: flex; align-items: center; justify-content: center; border-radius: 24rpx; font-size: 25rpx; font-weight: 600; }
.bottom-actions .secondary { width: 40%; border: 2rpx solid #D8E2EF; color: #4E5D70; background: #FFFFFF; }
.bottom-actions .primary { flex: 1; gap: 7rpx; color: #FFFFFF; background: linear-gradient(135deg, #4A9DF8, #3185F0); box-shadow: 0 10rpx 22rpx rgba(49,133,240,.22); }
@media screen and (max-width: 360px) {
  .detail-page { padding-right: 22rpx; padding-left: 22rpx; }
  .benefit-copy text:last-child { max-width: 290rpx; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .timeline-copy text:last-child { max-width: 270rpx; }
  .bottom-actions { padding-right: 22rpx; padding-left: 22rpx; }
}

/* visual polish */
.detail-page{padding-right:30rpx;padding-left:30rpx;background:linear-gradient(180deg,#FAFCFF,#F5F8FC 360rpx)}
.nav-button{width:62rpx;height:62rpx;border-radius:21rpx;box-shadow:0 8rpx 22rpx rgba(39,69,104,.07)}
.status-card{padding:28rpx 25rpx;border-radius:29rpx;box-shadow:0 17rpx 38rpx rgba(49,132,239,.2)}
.benefit-card{padding:23rpx 24rpx;border-radius:25rpx}.section-card{margin-top:23rpx;padding:26rpx 25rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.info-row{min-height:76rpx}.service-tip{padding:23rpx 24rpx;border-radius:24rpx}
.bottom-actions>view{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.detail-page{padding-right:22rpx;padding-left:22rpx}.section-card{padding:23rpx 21rpx}.bottom-actions>view{height:86rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;background:rgba(248,251,255,.97);box-shadow:0 8rpx 22rpx rgba(37,66,99,.08);backdrop-filter:blur(18rpx)}
.detail-page{padding-top:calc(var(--status-bar-height, 0px) + 100rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.detail-page{padding-right:22rpx;padding-left:22rpx}}



/* Flat detail hierarchy: completed state and primary action are the only lightly raised elements. */
.nav-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.status-card{box-shadow:var(--gy-shadow-brand)}
.section-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-actions{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.bottom-actions .primary{box-shadow:var(--gy-shadow-brand)}



/* Adversarial UI audit: flat header and reliable hit areas. */
.fixed-page-header{padding-right:28rpx;padding-left:28rpx;background:rgba(248,251,255,.97);box-shadow:none;border-bottom:2rpx solid var(--gy-surface-border)}
.nav-button{width:88rpx;height:88rpx;flex:0 0 88rpx;border:0;background:transparent;box-shadow:none}
.status-card,.progress-card,.order-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.submit-button{min-height:88rpx;box-shadow:var(--gy-shadow-brand)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}}

</style>
