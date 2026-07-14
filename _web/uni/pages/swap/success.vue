<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#FFFFFF" size="21"></uv-icon></view>
          <text class="nav-title">换电完成</text>
          <view class="nav-button" @tap="shareTrip"><uv-icon name="share" color="#FFFFFF" size="20"></uv-icon></view>
        </view>

    <scroll-view class="page-scroll" scroll-y :show-scrollbar="false">
      <view class="success-page">
        

        <view class="success-hero">
          <view class="hero-glow glow-one"></view>
          <view class="hero-glow glow-two"></view>
          <view class="success-badge"><uv-icon name="checkmark" color="#FFFFFF" size="25"></uv-icon></view>
          <text class="success-title">换电成功</text>
          <text class="success-subtitle">满电已就位，祝您一路畅行</text>

          <view class="battery-stage">
            <view class="battery-shadow"></view>
            <view class="battery-shell">
              <view class="battery-cap"></view>
              <view class="battery-screen">
                <view class="battery-light"></view>
                <text>READY</text>
              </view>
              <view class="battery-energy"><view></view><view></view><view></view><view></view></view>
              <view class="battery-bolt"></view>
            </view>
            <view class="power-ring"><text>100</text><text>%</text></view>
          </view>

          <view class="range-row">
            <view><text>58</text><text>km</text><text>预计续航</text></view>
            <view class="range-divider"></view>
            <view><text>99</text><text>%</text><text>电池健康度</text></view>
          </view>
        </view>

        <view class="floating-status">
          <view class="status-icon"><uv-icon name="car-fill" color="#3389F7" size="22"></uv-icon></view>
          <view class="status-copy"><text>电池使用中</text><text>已为您开启全程安全监测</text></view>
          <view class="status-pill"><view></view><text>正常</text></view>
        </view>

        <view class="info-card">
          <view class="section-heading"><text>本次换电</text><text>2026-07-13 14:26</text></view>
          <view class="info-row"><text>换电站</text><view @tap="openStation"><text>科技园换电站</text><uv-icon name="arrow-right" color="#A4AFBC" size="11"></uv-icon></view></view>
          <view class="info-row"><text>电池型号</text><text>72V 30Ah 智能锂电池</text></view>
          <view class="info-row"><text>电池编号</text><text class="serial">GY72V30-10682</text></view>
          <view class="info-row"><text>订单编号</text><text class="serial">HD202607130018</text></view>
          <view class="info-row amount-row"><text>本次费用</text><view><text>会员畅换</text><text>¥0.00</text></view></view>
        </view>

        <view class="safety-card">
          <view class="safety-icon"><uv-icon name="lock-fill" color="#3C8FF6" size="22"></uv-icon></view>
          <view class="safety-copy">
            <text>骑行前安全检查</text>
            <text>请确认电池锁扣牢固、车辆电源与仪表显示正常</text>
          </view>
          <view class="safety-link" @tap="showSafetyGuide"><text>查看</text><uv-icon name="arrow-right" color="#438FF2" size="10"></uv-icon></view>
        </view>

        <view class="benefit-card">
          <view class="benefit-icon"><uv-icon name="integral-fill" color="#F0A13A" size="21"></uv-icon></view>
          <view class="benefit-copy"><text>本次获得 20 积分</text><text>连续安全骑行还可领取额外奖励</text></view>
          <view class="benefit-button" @tap="openMember">去看看</view>
        </view>
        <view class="content-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-actions">
      <view class="secondary" @tap="openOrder">查看订单</view>
      <view class="primary" @tap="goHome"><uv-icon name="home-fill" color="#FFFFFF" size="19"></uv-icon><text>返回首页</text></view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    goBack() { uni.navigateBack({ fail: () => this.goHome() }) },
    goHome() { uni.reLaunch({ url: '/pages/index/index' }) },
    openOrder() { uni.navigateTo({ url: '/pages/order/detail?no=HD202607130018' }) },
    openStation() { uni.navigateTo({ url: '/pages/station/detail' }) },
    shareTrip() {
      uni.setClipboardData({ data: '我刚在电能行完成换电，30 秒满电出发！', success: () => uni.showToast({ title: '分享文案已复制', icon: 'success' }) })
    },
    showSafetyGuide() { uni.showModal({ title: '安全骑行指南', content: '出发前请确认电池已锁紧；骑行中如发现异响、异味或异常发热，请立即停车并联系平台。', showCancel: false }) },
    openMember() { uni.navigateTo({ url: '/pages/member/index' }) }
  }
}
</script>

<style scoped>
.page-shell,.page-scroll{width:100%;height:100%;background:#F5F8FC}.page-shell{position:relative;overflow:hidden}.success-page{min-height:100%;padding:0 28rpx;color:#283445;background:linear-gradient(180deg,#3288F3 0,#4A9BF7 620rpx,#F5F8FC 620rpx);box-sizing:border-box}.nav-bar{height:calc(var(--status-bar-height,0px) + 96rpx);padding-top:var(--status-bar-height,0px);display:flex;align-items:center;justify-content:space-between;color:#fff}.nav-button{width:58rpx;height:58rpx;display:flex;align-items:center;justify-content:center;border:1rpx solid rgba(255,255,255,.22);border-radius:50%;background:rgba(255,255,255,.12)}.nav-title{font-size:29rpx;font-weight:700}.success-hero{height:524rpx;position:relative;display:flex;align-items:center;flex-direction:column;overflow:hidden;color:#fff}.hero-glow{position:absolute;border:2rpx solid rgba(255,255,255,.14);border-radius:50%}.glow-one{width:380rpx;height:380rpx;left:-260rpx;top:50rpx}.glow-two{width:500rpx;height:500rpx;right:-340rpx;top:-60rpx}.success-badge{width:54rpx;height:54rpx;z-index:1;display:flex;align-items:center;justify-content:center;border:6rpx solid rgba(255,255,255,.3);border-radius:50%;background:#2EC994;box-shadow:0 10rpx 24rpx rgba(13,106,86,.2)}.success-title{z-index:1;margin-top:12rpx;font-size:38rpx;font-weight:700;letter-spacing:2rpx}.success-subtitle{z-index:1;margin-top:7rpx;color:#DDEEFF;font-size:23rpx}.battery-stage{width:420rpx;height:222rpx;z-index:1;margin-top:18rpx;position:relative;display:flex;align-items:flex-end;justify-content:center}.battery-shadow{width:230rpx;height:31rpx;position:absolute;left:91rpx;bottom:1rpx;border-radius:50%;background:rgba(22,84,158,.25);filter:blur(5rpx)}.battery-shell{width:122rpx;height:184rpx;position:relative;padding:18rpx 14rpx 12rpx;border:5rpx solid rgba(255,255,255,.9);border-radius:22rpx;background:linear-gradient(145deg,#DCEEFF,#FFFFFF);box-shadow:0 18rpx 35rpx rgba(20,86,168,.25);transform:rotate(-4deg)}.battery-cap{width:46rpx;height:11rpx;position:absolute;left:33rpx;top:-14rpx;border-radius:6rpx 6rpx 0 0;background:#EAF4FF}.battery-screen{height:40rpx;display:flex;align-items:center;justify-content:center;gap:7rpx;border-radius:9rpx;color:#407CBF;background:#CAE2FC;font-size:13rpx;font-weight:700}.battery-light{width:8rpx;height:8rpx;border-radius:50%;background:#27C491;box-shadow:0 0 8rpx rgba(39,196,145,.8)}.battery-energy{margin-top:13rpx;display:flex;gap:5rpx}.battery-energy view{height:48rpx;flex:1;border-radius:5rpx;background:linear-gradient(180deg,#49A0FB,#2F84EF)}.battery-bolt{width:16rpx;height:30rpx;margin:11rpx auto 0;border-radius:3rpx;background:#5AA4F5;transform:skewX(-18deg)}.power-ring{width:112rpx;height:112rpx;position:absolute;right:33rpx;top:22rpx;display:flex;align-items:baseline;justify-content:center;padding-top:28rpx;border:7rpx solid rgba(255,255,255,.82);border-left-color:rgba(255,255,255,.25);border-radius:50%;background:rgba(255,255,255,.12);box-shadow:0 8rpx 24rpx rgba(28,91,165,.12)}.power-ring text:first-child{font-size:37rpx;font-weight:700}.power-ring text:last-child{font-size:22rpx}.range-row{width:100%;z-index:1;margin-top:10rpx;display:flex;align-items:center;justify-content:center}.range-row>view:not(.range-divider){width:210rpx;display:flex;align-items:baseline;justify-content:center;flex-wrap:wrap}.range-row>view text:first-child{font-size:35rpx;font-weight:700}.range-row>view text:nth-child(2){margin-left:4rpx;font-size:21rpx}.range-row>view text:last-child{flex-basis:100%;margin-top:2rpx;color:#DCEBFC;font-size:20rpx;text-align:center}.range-divider{width:1rpx;height:48rpx;margin:0 20rpx;background:rgba(255,255,255,.28)}.floating-status{min-height:102rpx;margin-top:-8rpx;padding:18rpx 20rpx;position:relative;z-index:3;display:flex;align-items:center;border-radius:24rpx;background:#fff;box-shadow:0 13rpx 34rpx rgba(37,76,122,.12)}.status-icon{width:56rpx;height:56rpx;flex:0 0 56rpx;display:flex;align-items:center;justify-content:center;border-radius:18rpx;background:#EAF4FF}.status-copy{min-width:0;margin-left:14rpx;display:flex;flex:1;flex-direction:column}.status-copy text:first-child{font-size:25rpx;font-weight:700}.status-copy text:last-child{margin-top:6rpx;overflow:hidden;color:#8B98A8;font-size:20rpx;text-overflow:ellipsis;white-space:nowrap}.status-pill{height:38rpx;padding:0 12rpx;display:flex;align-items:center;gap:7rpx;border-radius:19rpx;color:#248D6D;background:#E9F8F2;font-size:20rpx}.status-pill view{width:8rpx;height:8rpx;border-radius:50%;background:#2DBB8C}.info-card,.safety-card,.benefit-card{margin-top:20rpx;border-radius:24rpx;background:#fff;box-shadow:0 8rpx 26rpx rgba(42,68,99,.05)}.info-card{padding:22rpx}.section-heading{padding-bottom:16rpx;display:flex;align-items:center;justify-content:space-between;border-bottom:2rpx solid #F0F3F7}.section-heading text:first-child{font-size:27rpx;font-weight:700}.section-heading text:last-child{color:#98A3B1;font-size:20rpx}.info-row{min-height:66rpx;display:flex;align-items:center;justify-content:space-between;border-bottom:2rpx solid #F2F4F7;color:#8995A5;font-size:22rpx}.info-row>text:last-child,.info-row>view{max-width:70%;color:#3E4B5D;text-align:right}.info-row>view{display:flex;align-items:center;gap:5rpx}.serial{overflow-wrap:anywhere;word-break:break-all}.amount-row{border-bottom:0}.amount-row>view{flex-direction:column;align-items:flex-end;gap:2rpx}.amount-row>view text:first-child{color:#278F70;font-size:20rpx}.amount-row>view text:last-child{color:#3389F7;font-size:28rpx;font-weight:700}.safety-card,.benefit-card{padding:20rpx;display:flex;align-items:center}.safety-icon,.benefit-icon{width:52rpx;height:52rpx;flex:0 0 52rpx;display:flex;align-items:center;justify-content:center;border-radius:17rpx;background:#EBF4FF}.benefit-icon{background:#FFF4E4}.safety-copy,.benefit-copy{min-width:0;margin-left:13rpx;display:flex;flex:1;flex-direction:column}.safety-copy text:first-child,.benefit-copy text:first-child{font-size:24rpx;font-weight:600}.safety-copy text:last-child,.benefit-copy text:last-child{margin-top:6rpx;color:#8A97A7;font-size:20rpx;line-height:1.45}.safety-link{margin-left:10rpx;display:flex;align-items:center;color:#438FF2;font-size:20rpx;white-space:nowrap}.benefit-button{height:44rpx;margin-left:10rpx;padding:0 13rpx;display:flex;align-items:center;border-radius:22rpx;color:#D9892F;background:#FFF1DC;font-size:20rpx;white-space:nowrap}.content-spacer{height:calc(160rpx + env(safe-area-inset-bottom))}.bottom-actions{height:calc(128rpx + env(safe-area-inset-bottom));padding:16rpx 28rpx env(safe-area-inset-bottom);position:absolute;z-index:8;left:0;right:0;bottom:0;display:flex;gap:16rpx;background:rgba(255,255,255,.98);box-shadow:0 -6rpx 24rpx rgba(35,61,91,.08)}.bottom-actions>view{height:86rpx;display:flex;align-items:center;justify-content:center;border-radius:24rpx;font-size:25rpx;font-weight:600}.secondary{width:38%;border:2rpx solid #DCE4EE;color:#526174;background:#fff}.primary{flex:1;gap:7rpx;color:#fff;background:linear-gradient(135deg,#4A9DF8,#3185F0);box-shadow:0 10rpx 22rpx rgba(49,133,240,.22)}
@media screen and (max-width:360px){.success-page{padding-right:22rpx;padding-left:22rpx}.success-hero{height:500rpx}.battery-stage{width:380rpx}.power-ring{right:17rpx}.range-row>view:not(.range-divider){width:175rpx}.bottom-actions{padding-right:22rpx;padding-left:22rpx}.safety-copy text:last-child{display:-webkit-box;overflow:hidden;-webkit-box-orient:vertical;-webkit-line-clamp:2}}

/* visual polish */
.success-page{padding-right:30rpx;padding-left:30rpx}.nav-button{width:62rpx;height:62rpx}
.success-badge{width:58rpx;height:58rpx;box-shadow:0 12rpx 28rpx rgba(13,106,86,.24)}
.floating-status{min-height:110rpx;padding:21rpx 22rpx;border-radius:28rpx;box-shadow:0 15rpx 38rpx rgba(37,76,122,.13)}
.info-card{padding:25rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}.info-row{min-height:72rpx}
.safety-card,.benefit-card{padding:23rpx;border-radius:26rpx;box-shadow:var(--gy-card-shadow)}
.bottom-actions>view{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.success-page{padding-right:22rpx;padding-left:22rpx}.floating-status{min-height:104rpx;padding:19rpx}.info-card{padding:22rpx}.info-row{min-height:68rpx}.bottom-actions>view{height:86rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;color:#FFFFFF;background:rgba(49,137,243,.97);box-shadow:0 8rpx 22rpx rgba(24,91,172,.18);backdrop-filter:blur(18rpx)}
.success-page{padding-top:calc(var(--status-bar-height, 0px) + 96rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.success-page{padding-right:22rpx;padding-left:22rpx}}



/* Flat success screen: keep the status focus but remove decorative card depth. */
.success-badge{box-shadow:0 3rpx 10rpx rgba(13,106,86,.12)}
.battery-shell{box-shadow:0 6rpx 15rpx rgba(20,86,168,.13)}
.power-ring,.floating-status,.info-card,.safety-card,.benefit-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-actions{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.primary{box-shadow:var(--gy-shadow-brand)}

</style>
