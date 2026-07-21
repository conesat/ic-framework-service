<template>
  <view class="message-page">
    <view class="fixed-page-header page-header">
      <view class="header-back" @tap="goBack" aria-label="返回">
        <uv-icon name="arrow-left" color="#2B3748" size="22"></uv-icon>
      </view>
      <view class="header-copy">
        <text class="header-title">消息中心</text>
        <text class="header-subtitle">换电、暂存和维修进度会及时同步</text>
      </view>
      <view class="header-action" @tap="markAllRead">全部已读</view>
    </view>

    <scroll-view class="message-scroll" scroll-y :show-scrollbar="false">
      <view class="message-content">
        <view class="notice-card">
          <view class="notice-icon"><uv-icon name="bell-fill" color="#3389F7" size="22"></uv-icon></view>
          <view class="notice-copy"><text>消息提醒已开启</text><text>重要状态变化会第一时间通知你</text></view>
          <uv-icon name="checkmark-circle-fill" color="#39B78B" size="18"></uv-icon>
        </view>

        <scroll-view class="filter-scroll" scroll-x :show-scrollbar="false">
          <view class="filter-row">
            <view v-for="item in filters" :key="item.key" class="filter-item" :class="{ active: activeFilter === item.key }" @tap="activeFilter = item.key">
              <text>{{ item.label }}</text><text v-if="item.key === 'unread' && unreadCount" class="filter-count">{{ unreadCount }}</text>
            </view>
          </view>
        </scroll-view>

        <view class="group-label"><text>最近消息</text><text>{{ filteredMessages.length }} 条</text></view>
        <view v-if="filteredMessages.length" class="message-list">
          <view v-for="item in filteredMessages" :key="item.id" class="message-card" :class="{ unread: !item.read, expanded: item.expanded }" @tap="toggleMessage(item)">
            <view class="message-type-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="20"></uv-icon></view>
            <view class="message-main">
              <view class="message-title-row"><text>{{ item.title }}</text><text>{{ item.time }}</text></view>
              <text class="message-summary">{{ item.summary }}</text>
              <view v-if="item.expanded" class="message-detail">
                <text>{{ item.detail }}</text>
                <view v-if="item.action" class="message-link" @tap.stop="openAction(item)">{{ item.action }}<uv-icon name="arrow-right" color="#3389F7" size="12"></uv-icon></view>
              </view>
            </view>
            <view v-if="!item.read" class="unread-dot"></view>
          </view>
        </view>
        <view v-else class="empty-state"><uv-icon name="bell" color="#B8C4D2" size="54"></uv-icon><text>暂时没有未读消息</text><text>新的服务通知会显示在这里</text></view>
        <view class="bottom-spacer"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
const MESSAGE_KEY = 'energy_message_center'
const DEFAULT_MESSAGES = [
  { id: 'swap-complete', category: 'service', title: '换电已完成', summary: '科技园换电站 · 72V 30Ah 满电电池已出柜', detail: '本次换电于 2026-07-18 09:26 完成，订单金额 ¥0.00。请确认电池已稳固安装后再出发。', time: '09:26', read: false, icon: 'scan', color: '#3389F7', theme: 'blue', action: '查看订单', url: '/pages/order/detail?no=HD202607180018&category=swap' },
  { id: 'storage-remind', category: 'service', title: '电池暂存提醒', summary: '深大地铁站换电点 · 还可暂存 18 小时', detail: '你的备用电池将于 2026-07-19 18:42 到期。请及时前往站点取回，避免产生额外费用。', time: '昨天', read: false, icon: 'clock', color: '#7568EC', theme: 'purple', action: '查看暂存订单', url: '/pages/order/detail?no=HD202607110096&category=storage' },
  { id: 'repair-progress', category: 'service', title: '维修工单处理中', summary: '工程师已接单，预计 2 小时内与你联系', detail: '工单 HD202607080063 已由平台工程师受理。请保持电话畅通，如遇电池发热、异味等安全问题请停止使用。', time: '07-17', read: true, icon: 'edit-pen', color: '#EA9851', theme: 'orange', action: '查看工单', url: '/pages/order/detail?no=HD202607080063&category=repair' },
  { id: 'coupon', category: 'system', title: '新换电券已到账', summary: '20 元换电券 · 有效期至 2026-08-18', detail: '新用户礼包中的换电券已发放到你的账户。结算时系统会自动匹配最优优惠，具体以结算页为准。', time: '07-16', read: true, icon: 'coupon-fill', color: '#E59B36', theme: 'gold', action: '前往使用' },
  { id: 'security', category: 'system', title: '账户安全提示', summary: '当前账号已完成实名认证与设备保护', detail: '我们会对异常登录、异常换电行为进行风险检测。请勿向他人透露验证码或授权他人使用你的账号。', time: '07-13', read: true, icon: 'lock-fill', color: '#39AF8B', theme: 'green' }
]

export default {
  data() { return { activeFilter: 'all', messages: [] } },
  computed: {
    unreadCount() { return this.messages.filter(item => !item.read).length },
    filters() { return [{ key: 'all', label: '全部' }, { key: 'unread', label: '未读' }, { key: 'service', label: '服务通知' }, { key: 'system', label: '系统消息' }] },
    filteredMessages() {
      if (this.activeFilter === 'all') return this.messages
      if (this.activeFilter === 'unread') return this.messages.filter(item => !item.read)
      return this.messages.filter(item => item.category === this.activeFilter)
    }
  },
  onLoad() {
    const saved = uni.getStorageSync(MESSAGE_KEY)
    this.messages = Array.isArray(saved) && saved.length ? saved : DEFAULT_MESSAGES.map(item => ({ ...item, expanded: false }))
  },
  methods: {
    persist() { uni.setStorageSync(MESSAGE_KEY, this.messages) },
    goBack() { uni.navigateBack({ delta: 1, fail: () => uni.reLaunch({ url: '/pages/index/index?tab=4' }) }) },
    markAllRead() { this.messages = this.messages.map(item => ({ ...item, read: true })); this.persist(); uni.showToast({ title: '已全部标记为已读', icon: 'none' }) },
    toggleMessage(item) { item.read = true; item.expanded = !item.expanded; this.persist() },
    openAction(item) {
      if (item.url) return uni.navigateTo({ url: item.url })
      uni.showToast({ title: '优惠将在换电结算时自动使用', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.message-page{height:100%;background:#F5F8FC;color:var(--gy-text-primary)}
.page-header{height:calc(var(--status-bar-height,0px) + 128rpx);padding:calc(var(--status-bar-height,0px) + 24rpx) 28rpx 18rpx;position:fixed;z-index:10;top:0;left:0;right:0;display:flex;align-items:center;background:rgba(255,255,255,.96);backdrop-filter:blur(18rpx)}
.header-back{width:64rpx;height:64rpx;display:flex;align-items:center;justify-content:center;border:1rpx solid var(--gy-surface-border);border-radius:20rpx;background:#fff}.header-copy{min-width:0;flex:1;margin-left:18rpx;display:flex;flex-direction:column}.header-title{font-size:32rpx;font-weight:700;line-height:40rpx}.header-subtitle{margin-top:2rpx;overflow:hidden;color:var(--gy-text-muted);font-size:21rpx;text-overflow:ellipsis;white-space:nowrap}.header-action{height:64rpx;padding:0 17rpx;display:flex;align-items:center;justify-content:center;color:#3389F7;font-size:24rpx;white-space:nowrap}
.message-scroll{height:100%;padding-top:calc(var(--status-bar-height,0px) + 128rpx);box-sizing:border-box}.message-content{padding:24rpx 28rpx 0}.notice-card{min-height:110rpx;padding:18rpx 20rpx;display:flex;align-items:center;border:1rpx solid #DCEBFD;border-radius:24rpx;background:#F7FBFF}.notice-icon{width:62rpx;height:62rpx;display:flex;align-items:center;justify-content:center;border-radius:20rpx;background:#E9F3FF}.notice-copy{min-width:0;flex:1;margin-left:16rpx;display:flex;flex-direction:column}.notice-copy text:first-child{font-size:26rpx;font-weight:650}.notice-copy text:last-child{margin-top:5rpx;color:#79879A;font-size:22rpx}.filter-scroll{margin-top:26rpx;width:100%;white-space:nowrap}.filter-row{display:inline-flex;gap:14rpx;padding-right:4rpx}.filter-item{min-width:112rpx;height:62rpx;padding:0 18rpx;display:flex;align-items:center;justify-content:center;gap:7rpx;border:1rpx solid var(--gy-surface-border);border-radius:19rpx;background:#fff;color:#778497;font-size:24rpx}.filter-item.active{border-color:#D1E5FE;background:#EAF4FF;color:#3389F7;font-weight:650}.filter-count{min-width:28rpx;height:28rpx;display:flex;align-items:center;justify-content:center;border-radius:14rpx;background:#F06A70;color:#fff;font-size:18rpx}.group-label{margin:32rpx 4rpx 16rpx;display:flex;align-items:center;justify-content:space-between}.group-label text:first-child{font-size:28rpx;font-weight:700}.group-label text:last-child{color:#98A4B3;font-size:22rpx}.message-list{display:flex;flex-direction:column;gap:16rpx}.message-card{position:relative;padding:22rpx 20rpx;display:flex;border:1rpx solid var(--gy-surface-border);border-radius:25rpx;background:#fff}.message-card.unread{border-color:#DCEBFE;background:#FCFDFF}.message-type-icon{width:62rpx;height:62rpx;flex:0 0 62rpx;display:flex;align-items:center;justify-content:center;border-radius:20rpx}.message-type-icon.blue{background:#E9F3FF}.message-type-icon.purple{background:#F0EEFF}.message-type-icon.orange{background:#FFF2E7}.message-type-icon.gold{background:#FFF6E7}.message-type-icon.green{background:#E9F8F3}.message-main{min-width:0;flex:1;margin-left:16rpx}.message-title-row{display:flex;align-items:center;justify-content:space-between;gap:14rpx}.message-title-row text:first-child{overflow:hidden;font-size:26rpx;font-weight:650;text-overflow:ellipsis;white-space:nowrap}.message-title-row text:last-child{flex:0 0 auto;color:#9AA6B5;font-size:21rpx}.message-summary{display:block;margin-top:7rpx;overflow:hidden;color:#68778A;font-size:23rpx;line-height:34rpx;text-overflow:ellipsis;white-space:nowrap}.message-detail{margin-top:16rpx;padding-top:16rpx;border-top:1rpx solid #EDF1F6;color:#617084;font-size:23rpx;line-height:37rpx}.message-link{width:max-content;height:54rpx;margin-top:13rpx;padding:0 14rpx;display:flex;align-items:center;gap:4rpx;border-radius:16rpx;background:#EDF6FF;color:#3389F7;font-size:22rpx}.unread-dot{width:12rpx;height:12rpx;position:absolute;top:21rpx;right:21rpx;border:3rpx solid #fff;border-radius:50%;background:#F06A70}.empty-state{margin-top:84rpx;display:flex;align-items:center;flex-direction:column}.empty-state text:nth-child(2){margin-top:20rpx;color:#5F6F82;font-size:27rpx;font-weight:650}.empty-state text:nth-child(3){margin-top:8rpx;color:#9AA6B5;font-size:22rpx}.bottom-spacer{height:calc(54rpx + env(safe-area-inset-bottom))}@media screen and (max-width:360px){.message-content{padding-right:22rpx;padding-left:22rpx}.header-action{padding-right:4rpx}.header-subtitle{max-width:290rpx}.filter-item{min-width:104rpx;padding:0 14rpx}.message-card{padding:19rpx 17rpx}}
</style>
