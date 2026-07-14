<template>
  <scroll-view class="mine-scroll" scroll-y :show-scrollbar="false">
    <view class="mine-page">
      <view class="profile-section">
        <view class="profile-actions">
          <view class="round-action" @tap="openMessages">
            <uv-icon name="bell" color="#526174" size="20"></uv-icon>
            <view class="message-dot"></view>
          </view>
          <view class="round-action" @tap="openSettings">
            <uv-icon name="setting" color="#526174" size="20"></uv-icon>
          </view>
        </view>

        <view class="profile-main">
          <view class="avatar" aria-label="用户头像">
            <view class="avatar-head"></view>
            <view class="avatar-body"></view>
            <view class="verified-dot"><uv-icon name="checkmark" color="#FFFFFF" size="10"></uv-icon></view>
          </view>
          <view class="profile-copy">
            <text class="phone-number">138 **** 5678</text>
            <view class="verified-label"><uv-icon name="lock-fill" color="#3A91F8" size="11"></uv-icon><text>已实名认证</text></view>
          </view>
          <view class="member-level" @tap="openMember"><uv-icon name="integral-fill" color="#DCA245" size="15"></uv-icon><text>黄金会员</text><uv-icon name="arrow-right" color="#A78043" size="11"></uv-icon></view>
        </view>
      </view>

      <view class="member-card" @tap="openMember">
        <view class="member-mark"><uv-icon name="star-fill" color="#FFF3C7" size="23"></uv-icon></view>
        <view class="member-title-wrap">
          <text class="member-title">我的会员</text>
          <text class="member-date">2027.06.15 到期</text>
        </view>
        <view class="member-benefits"><text>享受 6 项专属权益</text><view class="benefit-dots"><view></view><view></view><view></view></view></view>
        <view class="renew-button" @tap.stop="openMember">续费会员</view>
      </view>

      <view class="asset-panel">
        <view v-for="item in assets" :key="item.label" class="asset-item" @tap="handleAsset(item)">
          <text class="asset-label">{{ item.label }}</text>
          <view class="asset-value"><text>{{ item.value }}</text><text v-if="item.unit">{{ item.unit }}</text></view>
          <view class="asset-action"><text>{{ item.action }}</text><uv-icon name="arrow-right" color="#9CA8B8" size="10"></uv-icon></view>
        </view>
      </view>

      <view class="order-card">
        <view class="section-heading">
          <text>我的订单</text>
          <view class="section-link" @tap="switchOrders"><text>全部订单</text><uv-icon name="arrow-right" color="#9CA7B4" size="12"></uv-icon></view>
        </view>
        <view class="order-grid">
          <view v-for="item in orders" :key="item.label" class="order-item" @tap="switchOrders">
            <view class="order-icon" :class="item.theme">
              <uv-icon :name="item.icon" :color="item.color" size="24"></uv-icon>
              <view v-if="item.badge" class="order-badge">{{ item.badge }}</view>
            </view>
            <text>{{ item.label }}</text>
          </view>
        </view>
      </view>

      <view class="service-card">
        <view v-for="(item, index) in services" :key="item.title" class="service-row" :class="{ 'service-row-highlight': item.highlight }" @tap="handleService(item)">
          <view class="service-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="21"></uv-icon></view>
          <text class="service-title">{{ item.title }}</text>
          <text class="service-value">{{ item.value }}</text>
          <uv-icon name="arrow-right" :color="item.highlight ? '#DAE4F3' : '#B2BCC9'" size="12"></uv-icon>
          <view v-if="index < services.length - 1 && !item.highlight && !services[index + 1].highlight" class="service-divider"></view>
        </view>
      </view>

      <view class="bottom-spacer"></view>
    </view>
  </scroll-view>
</template>

<script>
export default {
  name: 'EnergyMine',
  data() {
    return {
      assets: [
        { label: '余额（元）', value: '68.00', unit: '', action: '去充值' },
        { label: '优惠券', value: '6', unit: '张', action: '去查看' },
        { label: '积分', value: '1280', unit: '', action: '去兑换' },
        { label: '押金（元）', value: '299.00', unit: '', action: '可退还' }
      ],
      orders: [
        { label: '全部', icon: 'order', color: '#3B92F8', theme: 'blue' },
        { label: '换电订单', icon: 'reload', color: '#7A69EF', theme: 'purple' },
        { label: '暂存订单', icon: 'folder', color: '#EDA535', theme: 'orange' },
        { label: '维修订单', icon: 'setting-fill', color: '#3D9AEC', theme: 'cyan' },
        { label: '异常订单', icon: 'warning-fill', color: '#EF6E73', theme: 'red', badge: '1' }
      ],
      services: [
        { title: '我的电池', value: '2 块电池', icon: 'coupon-fill', color: '#378FF7', theme: 'blue' },
        { title: '电池暂存', value: '1 块暂存中', icon: 'folder', color: '#7568EC', theme: 'purple' },
        { title: '常去站点', value: '3 个站点', icon: 'map-fill', color: '#E5A033', theme: 'orange' },
        { title: '常用地址', value: '家 / 公司', icon: 'home-fill', color: '#40A783', theme: 'green' },
        { title: '安全中心', value: '账户安全保障中', icon: 'lock-fill', color: '#378FF7', theme: 'blue' },
        { title: '客服中心', value: '7×24 小时服务', icon: 'kefu-ermai', color: '#FFFFFF', theme: 'dark', highlight: true },
        { title: '帮助与反馈', value: '常见问题 / 意见反馈', icon: 'question-circle-fill', color: '#E49B32', theme: 'orange' },
        { title: '关于我们', value: '版本 1.2.0', icon: 'info-circle-fill', color: '#718095', theme: 'gray' }
      ]
    }
  },
  methods: {
    openMember() { uni.navigateTo({ url: '/pages/member/index' }) },
    switchOrders() { this.$emit('switch-tab', 3) },
    openMessages() { uni.showModal({ title: '消息中心', content: '暂无未读消息。换电、暂存与维修进度会在这里同步通知。', showCancel: false }) },
    openSettings() { uni.showModal({ title: '设置', content: '通知提醒：已开启\n定位服务：已开启\n当前版本：1.2.0', showCancel: false }) },
    handleAsset(item) {
      const messages = {
        '余额（元）': '当前可用余额 ¥68.00。充值服务将在接入支付后自动可用。',
        '优惠券': '当前有 6 张可用优惠券，换电结算时会自动匹配最优优惠。',
        '积分': '当前积分 1280，可用于兑换换电券与会员权益。',
        '押金（元）': '押金 ¥299.00，符合退还条件时可通过客服中心申请退还。'
      }
      uni.showModal({ title: item.label, content: messages[item.label], showCancel: false })
    },
    handleService(item) {
      if (item.title === '我的电池') return uni.navigateTo({ url: '/pages/battery/index' })
      if (item.title === '电池暂存') return uni.navigateTo({ url: '/pages/storage/index' })
      if (item.title === '常去站点') return this.$emit('switch-tab', 1)
      if (item.title === '常用地址') return uni.showActionSheet({ itemList: ['家 · 科技园南区', '公司 · 软件产业基地'], success: () => uni.showToast({ title: '常用地址已选中', icon: 'none' }) })
      if (item.title === '安全中心') return uni.showModal({ title: '安全中心', content: '账户已实名认证，登录设备与异常换电行为均会受到安全保护。', showCancel: false })
      if (item.title === '客服中心') return uni.makePhoneCall({ phoneNumber: '4008888899', fail: () => uni.showToast({ title: '请拨打 400-888-8899', icon: 'none' }) })
      if (item.title === '帮助与反馈') return uni.showModal({ title: '帮助与反馈', content: '常见问题：扫码无响应、无法开柜、电池异常。紧急安全问题请致电 400-888-8899。', showCancel: false })
      if (item.title === '关于我们') return uni.showModal({ title: '电能行', content: '电能行智能换电\n版本 1.2.0\n让每一次出发都有满格能量。', showCancel: false })
    }
  }
}
</script>

<style scoped>
.mine-scroll {
  width: 100%;
  height: 100%;
  background: #F5F8FC;
}
.mine-page {
  min-height: 100%;
  padding: 0 34rpx;
  box-sizing: border-box;
  color: #2A3444;
  background:
    radial-gradient(circle at 53% 74rpx, rgba(61, 151, 255, .13) 0, rgba(61, 151, 255, 0) 110rpx),
    linear-gradient(180deg, #FFFFFF 0, #FFFFFF 410rpx, #F5F8FC 410rpx, #F5F8FC 100%);
}
.profile-section {
  position: relative;
  padding-top: calc(var(--status-bar-height) + 97rpx);
}
.profile-actions {
  position: absolute;
  top: calc(var(--status-bar-height) + 16rpx);
  right: 0;
  display: flex;
  gap: 14rpx;
}
.round-action {
  width: 58rpx;
  height: 58rpx;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1rpx solid #EEF1F5;
  border-radius: 50%;
  background: rgba(255, 255, 255, .86);
}
.message-dot {
  width: 10rpx;
  height: 10rpx;
  position: absolute;
  top: 10rpx;
  right: 9rpx;
  border: 3rpx solid #FFFFFF;
  border-radius: 50%;
  background: #F06A70;
}
.profile-main {
  height: 180rpx;
  display: flex;
  align-items: center;
  transform: translateY(37rpx);
}
.avatar {
  width: 112rpx;
  height: 112rpx;
  position: relative;
  flex: 0 0 112rpx;
  overflow: visible;
  border: 7rpx solid #FFFFFF;
  border-radius: 38rpx;
  background: linear-gradient(145deg, #DCEEFF 0%, #BFDFFF 100%);
  box-shadow: 0 12rpx 30rpx rgba(50, 133, 230, .18);
}
.avatar-head {
  width: 37rpx;
  height: 37rpx;
  position: absolute;
  top: 20rpx;
  left: 31rpx;
  border-radius: 50%;
  background: linear-gradient(180deg, #579EF0, #367ED5);
}
.avatar-body {
  width: 66rpx;
  height: 38rpx;
  position: absolute;
  left: 16rpx;
  bottom: 13rpx;
  border-radius: 38rpx 38rpx 17rpx 17rpx;
  background: linear-gradient(180deg, #579EF0, #367ED5);
}
.verified-dot {
  width: 34rpx;
  height: 34rpx;
  position: absolute;
  right: -10rpx;
  bottom: -7rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 5rpx solid #FFFFFF;
  border-radius: 50%;
  background: #378EF5;
}
.profile-copy {
  min-width: 0;
  margin-left: 28rpx;
  display: flex;
  flex: 1;
  flex-direction: column;
}
.phone-number {
  color: #293343;
  font-size: 30rpx;
  line-height: 42rpx;
  font-weight: 700;
  white-space: nowrap;
}
.verified-label {
  width: fit-content;
  height: 38rpx;
  margin-top: 6rpx;
  padding: 0 13rpx;
  display: flex;
  align-items: center;
  gap: 7rpx;
  border-radius: 19rpx;
  color: #657287;
  background: #EDF5FF;
  font-size: 23rpx;
  white-space: nowrap;
}
.member-level {
  min-width: 0;
  margin-left: 12rpx;
  padding-top: 46rpx;
  display: flex;
  align-items: center;
  gap: 5rpx;
  color: #9C7132;
  font-size: 24rpx;
  white-space: nowrap;
}
.member-card {
  height: 128rpx;
  margin-top: 14rpx;
  padding: 0 24rpx;
  position: relative;
  display: flex;
  align-items: center;
  overflow: hidden;
  border-radius: 24rpx;
  background: linear-gradient(112deg, #303A4D 0%, #283246 60%, #36465E 100%);
  box-shadow: 0 13rpx 32rpx rgba(29, 43, 65, .16);
}
.member-card::after {
  content: '';
  width: 190rpx;
  height: 190rpx;
  position: absolute;
  right: 132rpx;
  top: -109rpx;
  border-radius: 50%;
  background: rgba(255, 233, 175, .07);
}
.member-mark {
  width: 56rpx;
  height: 56rpx;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 56rpx;
  border: 1rpx solid rgba(255, 238, 195, .32);
  border-radius: 18rpx;
  background: rgba(255, 223, 148, .14);
}
.member-title-wrap {
  z-index: 1;
  margin-left: 17rpx;
  display: flex;
  flex-direction: column;
}
.member-title {
  color: #FFF8E5;
  font-size: 27rpx;
  line-height: 36rpx;
  font-weight: 700;
}
.member-date {
  margin-top: -3rpx;
  color: #C8CFDB;
  font-size: 21rpx;
  white-space: nowrap;
}
.member-benefits {
  min-width: 0;
  z-index: 1;
  margin-left: auto;
  display: flex;
  align-items: center;
  color: #D6DCE6;
  font-size: 21rpx;
  white-space: nowrap;
}
.benefit-dots {
  margin-left: 8rpx;
  display: flex;
  gap: 4rpx;
}
.benefit-dots view {
  width: 5rpx;
  height: 5rpx;
  border-radius: 50%;
  background: #D8B76B;
}
.renew-button {
  min-width: 104rpx;
  height: 48rpx;
  z-index: 1;
  margin-left: 18rpx;
  padding: 0 17rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 24rpx;
  color: #5C4726;
  background: linear-gradient(135deg, #FFE8AD, #EEC678);
  font-size: 23rpx;
  font-weight: 600;
  white-space: nowrap;
}
.asset-panel {
  height: 174rpx;
  margin-top: 22rpx;
  padding: 25rpx 4rpx 21rpx;
  display: flex;
  border-radius: 24rpx;
  background: #FFFFFF;
  box-shadow: 0 8rpx 28rpx rgba(35, 62, 94, .055);
}
.asset-item {
  min-width: 0;
  position: relative;
  display: flex;
  flex: 1;
  align-items: center;
  flex-direction: column;
}
.asset-item + .asset-item::before {
  content: '';
  width: 1rpx;
  height: 70rpx;
  position: absolute;
  left: 0;
  top: 23rpx;
  background: #EEF1F5;
}
.asset-label {
  color: #6F7C8E;
  font-size: 23rpx;
  line-height: 28rpx;
  white-space: nowrap;
}
.asset-value {
  height: 43rpx;
  margin-top: 15rpx;
  display: flex;
  align-items: baseline;
  color: #283343;
  line-height: 43rpx;
}
.asset-value > text:first-child {
  font-size: 29rpx;
  font-weight: 700;
  white-space: nowrap;
}
.asset-value > text:last-child {
  margin-left: 3rpx;
  font-size: 23rpx;
  font-weight: 500;
}
.asset-action {
  margin-top: 20rpx;
  display: flex;
  align-items: center;
  color: #8996A8;
  font-size: 21rpx;
  white-space: nowrap;
}
.order-card {
  margin-top: 36rpx;
  padding: 25rpx 24rpx 28rpx;
  border-radius: 24rpx;
  background: #FFFFFF;
  box-shadow: 0 8rpx 28rpx rgba(35, 62, 94, .055);
}
.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.section-heading > text {
  color: #293344;
  font-size: 28rpx;
  font-weight: 700;
}
.section-link {
  display: flex;
  align-items: center;
  color: #929EAE;
  font-size: 22rpx;
}
.order-grid {
  margin-top: 8rpx;
  display: flex;
}
.order-item {
  min-width: 0;
  display: flex;
  flex: 1;
  align-items: center;
  flex-direction: column;
  color: #4F5B6B;
  font-size: 22rpx;
  white-space: nowrap;
}
.order-icon {
  width: 64rpx;
  height: 64rpx;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 21rpx;
}
.order-icon.blue { background: #E7F2FF; }
.order-icon.purple { background: #F0EDFF; }
.order-icon.orange { background: #FFF3DE; }
.order-icon.cyan { background: #E7F6FF; }
.order-icon.red { background: #FFECEE; }
.order-item > text { margin-top: 12rpx; }
.order-badge {
  min-width: 26rpx;
  height: 26rpx;
  padding: 0 7rpx;
  position: absolute;
  right: -7rpx;
  top: -8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3rpx solid #FFFFFF;
  border-radius: 13rpx;
  color: #FFFFFF;
  background: #EF676F;
  font-size: 20rpx;
  line-height: 1;
}
.service-card {
  margin-top: 14rpx;
  padding: 7rpx 0;
  overflow: hidden;
  border-radius: 24rpx;
  background: #FFFFFF;
  box-shadow: 0 8rpx 28rpx rgba(35, 62, 94, .055);
}
.service-row {
  height: 71rpx;
  margin: 0 14rpx;
  padding: 0 12rpx;
  position: relative;
  display: flex;
  align-items: center;
  border-radius: 18rpx;
}
.service-row-highlight {
  height: 68rpx;
  margin: -5rpx 14rpx 0;
  padding: 0 21rpx;
  color: #FFFFFF;
  background: linear-gradient(115deg, #303A4C, #283346 62%, #384960);
  box-shadow: 0 10rpx 24rpx rgba(35, 48, 67, .16);
}
.service-icon {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 48rpx;
  border-radius: 15rpx;
}
.service-icon.blue { background: #E8F2FF; }
.service-icon.purple { background: #F0EDFF; }
.service-icon.orange { background: #FFF3DE; }
.service-icon.green { background: #E7F7F1; }
.service-icon.gray { background: #EEF1F5; }
.service-icon.dark { background: rgba(255, 255, 255, .13); }
.service-title {
  margin-left: 18rpx;
  color: #374354;
  font-size: 24rpx;
  font-weight: 500;
  white-space: nowrap;
}
.service-value {
  min-width: 0;
  margin-left: auto;
  overflow: hidden;
  color: #8995A6;
  font-size: 21rpx;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.service-row-highlight .service-title {
  color: #FFFFFF;
  font-weight: 600;
}
.service-row-highlight .service-value { color: #D6DFEC; }
.service-divider {
  height: 1rpx;
  position: absolute;
  right: 2rpx;
  bottom: 0;
  left: 66rpx;
  background: #EEF1F5;
}
.bottom-spacer { height: 174rpx; }

@media screen and (max-width: 360px) {
  .mine-page { padding-right: 26rpx; padding-left: 26rpx; }
  .profile-copy { margin-left: 20rpx; }
  .phone-number { font-size: 28rpx; }
  .member-level { font-size: 21rpx; }
  .member-benefits { display: none; }
  .renew-button { margin-left: auto; }
  .asset-label { font-size: 21rpx; }
  .asset-value > text:first-child { font-size: 27rpx; }
  .service-card { margin-top: 10rpx; }
  .service-row { height: 68rpx; }
  .service-value { max-width: 230rpx; }
}

/* visual polish */
.mine-page{background:radial-gradient(circle at 53% 74rpx,rgba(61,151,255,.15) 0,rgba(61,151,255,0) 125rpx),linear-gradient(180deg,#FFFFFF 0,#FFFFFF 430rpx,#F5F8FC 430rpx,#F5F8FC 100%)}
.round-action{width:62rpx;height:62rpx;box-shadow:0 7rpx 20rpx rgba(38,68,104,.055)}
.avatar{width:116rpx;height:116rpx;flex-basis:116rpx;border-radius:40rpx;box-shadow:0 14rpx 34rpx rgba(50,133,230,.2)}
.member-card{height:136rpx;border-radius:28rpx;box-shadow:0 16rpx 38rpx rgba(29,43,65,.18)}
.asset-panel{height:184rpx;margin-top:25rpx;padding-top:28rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.order-card{margin-top:40rpx;padding:28rpx 26rpx 30rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.order-grid{margin-top:14rpx}.order-icon{width:68rpx;height:68rpx;border-radius:22rpx}
.service-card{margin-top:18rpx;padding:9rpx 0;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.service-row{height:78rpx;padding:0 15rpx}.service-row-highlight{height:75rpx;margin:0 14rpx;padding:0 22rpx;border-radius:20rpx}
.service-icon{width:52rpx;height:52rpx;flex-basis:52rpx;border-radius:17rpx}
@media screen and (max-width:360px){.avatar{width:110rpx;height:110rpx;flex-basis:110rpx}.member-card{height:132rpx}.asset-panel{height:178rpx}.service-row{height:74rpx}.service-row-highlight{height:72rpx}}



/* Flat profile surfaces: use fine outlines for grouping and reserve elevation for the identity cue. */
.avatar{box-shadow:var(--gy-shadow-brand)}
.member-card{border:2rpx solid rgba(49,73,108,.16);box-shadow:var(--gy-shadow-float)}
.asset-panel,.order-card,.service-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.round-action{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.service-row-highlight{box-shadow:none}

</style>
