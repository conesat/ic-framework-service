<template>
  <scroll-view class="orders-scroll" scroll-y :show-scrollbar="false">
    <view class="orders-page">
      <view class="orders-header">
        <view class="orders-heading-copy">
          <text class="orders-title">我的订单</text>
          <text class="orders-subtitle">每一次换电，都有迹可循</text>
        </view>
        <view class="calendar-button" @tap="openCalendar">
          <uv-icon name="calendar" color="#4C5C70" size="21"></uv-icon>
        </view>
      </view>

      <view class="summary-card">
        <view class="summary-item">
          <text>本月换电</text>
          <view><text class="summary-value">12</text><text class="summary-unit">次</text></view>
        </view>
        <view class="summary-divider"></view>
        <view class="summary-item">
          <text>累计节省</text>
          <view><text class="summary-value">268</text><text class="summary-unit">元</text></view>
        </view>
        <view class="summary-divider"></view>
        <view class="summary-item">
          <text>骑行里程</text>
          <view><text class="summary-value">386</text><text class="summary-unit">km</text></view>
        </view>
      </view>

      <scroll-view class="filter-tabs" scroll-x :show-scrollbar="false">
        <view class="filter-tabs-inner">
          <view
            v-for="item in filters"
            :key="item.key"
            class="filter-tab"
            :class="{ active: activeFilter === item.key }"
            @tap="activeFilter = item.key"
          >
            <text>{{ item.label }}</text>
            <text>{{ item.count }}</text>
          </view>
        </view>
      </scroll-view>

      <view class="list-heading">
        <text>{{ activeFilterLabel }}订单</text>
        <view @tap="showOrderGuide">
          <text>订单说明</text>
          <uv-icon name="question-circle" color="#9AA6B5" size="14"></uv-icon>
        </view>
      </view>

      <view
        v-for="order in filteredOrders"
        :key="order.no"
        class="order-card"
        @tap="openOrder(order)"
      >
        <view class="order-top">
          <view class="order-kind">
            <view class="kind-icon" :class="order.iconTheme">
              <uv-icon :name="order.icon" :color="order.iconColor" size="18"></uv-icon>
            </view>
            <text>{{ order.type }}</text>
          </view>
          <text class="order-status" :class="order.statusClass">{{ order.status }}</text>
        </view>

        <view class="order-station">
          <text>{{ order.station }}</text>
          <uv-icon name="arrow-right" color="#A7B1BE" size="13"></uv-icon>
        </view>

        <view class="order-info">
          <view><text>订单编号</text><text>{{ order.no }}</text></view>
          <view><text>订单时间</text><text>{{ order.time }}</text></view>
          <view><text>{{ order.detailLabel }}</text><text>{{ order.detail }}</text></view>
        </view>

        <view class="order-bottom">
          <view class="order-amount"><text>实付</text><text>{{ order.amount }}</text></view>
          <view class="order-action" :class="{ primary: order.primary }" @tap.stop="openOrder(order)">{{ order.action }}</view>
        </view>
      </view>

      <view class="empty-orders" v-if="!filteredOrders.length">
        <uv-icon name="empty-order" color="#C5CED9" size="56"></uv-icon>
        <text>暂无相关订单</text>
      </view>
      <view v-else class="end-tip"><view></view><text>已展示全部订单</text><view></view></view>
      <view class="bottom-spacer"></view>
    </view>
  </scroll-view>
</template>

<script>
import { getLocalOrders, updateLocalOrder } from '../../utils/localOrders'

const BUILTIN_ORDERS = [
  { no: 'HD202607130018', category: 'swap', type: '换电订单', icon: 'scan', iconColor: '#398BF4', iconTheme: 'blue', status: '已完成', statusClass: 'done', station: '科技园换电站', time: '2026-07-13 09:26', detailLabel: '换电电池', detail: '72V 30Ah 满电电池', amount: '¥0.00', action: '查看详情', primary: false },
  { no: 'HD202607110096', category: 'storage', type: '电池暂存', icon: 'lock', iconColor: '#796FF1', iconTheme: 'purple', status: '暂存中', statusClass: 'progress', station: '深大地铁站换电点', time: '2026-07-11 18:42', detailLabel: '预计取回', detail: '2026-07-14 18:42', amount: '¥6.00', action: '立即取回', primary: true },
  { no: 'HD202607080063', category: 'repair', type: '维修工单', icon: 'edit-pen', iconColor: '#EC9650', iconTheme: 'orange', status: '处理中', statusClass: 'warning', station: '软件产业基地站', time: '2026-07-08 14:18', detailLabel: '故障类型', detail: '电池无法正常充电', amount: '¥0.00', action: '查看进度', primary: false },
  { no: 'HD202607030027', category: 'swap', type: '换电订单', icon: 'scan', iconColor: '#398BF4', iconTheme: 'blue', status: '已完成', statusClass: 'done', station: '高新园北区站', time: '2026-07-03 21:05', detailLabel: '换电电池', detail: '72V 30Ah 满电电池', amount: '¥5.00', action: '再次换电', primary: false }
]

function normalizeLocalOrder(order) {
  const presets = {
    storage: { type: '电池暂存', icon: 'lock', iconColor: '#796FF1', iconTheme: 'purple', status: '暂存中', statusClass: 'progress', detailLabel: '预计取回', action: '立即取回', primary: true },
    repair: { type: '维修工单', icon: 'edit-pen', iconColor: '#EC9650', iconTheme: 'orange', status: '处理中', statusClass: 'warning', detailLabel: '故障类型', action: '查看进度', primary: false }
  }
  return { ...presets[order.category], amount: '¥0.00', station: '科技园换电站', time: order.createdAt, ...order }
}

export default {
  name: 'EnergyOrders',
  data() {
    return {
      activeFilter: 'all',
      filters: [
        { key: 'all', label: '全部', count: 18 },
        { key: 'swap', label: '换电', count: 12 },
        { key: 'storage', label: '暂存', count: 3 },
        { key: 'repair', label: '维修', count: 3 }
      ],
      orders: [...getLocalOrders().map(normalizeLocalOrder), ...BUILTIN_ORDERS]
    }
  },
  computed: {
    activeFilterLabel() {
      const current = this.filters.find(item => item.key === this.activeFilter)
      return current ? current.label : '全部'
    },
    filteredOrders() {
      return this.activeFilter === 'all' ? this.orders : this.orders.filter(item => item.category === this.activeFilter)
    }
  },
  methods: {
    openOrder(order) {
      if (order.category === 'storage' && order.status === '暂存中') return this.retrieveStorage(order)
      if (order.category === 'swap' && order.action === '再次换电') return this.$emit('switch-tab', 2)
      uni.navigateTo({ url: `/pages/order/detail?no=${order.no}&category=${order.category || 'swap'}` })
    },
    retrieveStorage(order) {
      uni.showModal({
        title: '确认取回电池',
        content: '请在 30 分钟内到站扫描换电柜二维码取回电池。',
        confirmText: '确认取回',
        success: ({ confirm }) => {
          if (!confirm) return
          order.status = '待取回'; order.statusClass = 'warning'; order.action = '查看详情'; order.primary = false
          if (order.id?.startsWith('local-')) updateLocalOrder(order.no, { status: '待取回', statusClass: 'warning', action: '查看详情', primary: false })
          uni.showToast({ title: '取回指令已生成', icon: 'success' })
        }
      })
    },
    openCalendar() {
      uni.showActionSheet({ itemList: ['查看全部订单', '查看换电记录', '查看暂存记录', '查看维修记录'], success: ({ tapIndex }) => { this.activeFilter = ['all', 'swap', 'storage', 'repair'][tapIndex] } })
    },
    showOrderGuide() {
      uni.showModal({ title: '订单说明', content: '换电订单完成后可查看明细；暂存订单可在有效期内申请取回；维修工单将同步处理进度。', showCancel: false })
    }
  }
}
</script>

<style scoped>
.orders-scroll { width: 100%; height: 100%; background: #F5F8FC; }
.orders-page { min-height: 100%; padding: calc(var(--status-bar-height, 0px) + 50rpx) 28rpx 0; color: #283344; background: linear-gradient(180deg, #F8FBFF 0, #F5F8FC 340rpx); }
.orders-header { display: flex; align-items: center; justify-content: space-between; }
.orders-heading-copy { display: flex; flex-direction: column; }
.orders-title { font-size: 36rpx; font-weight: 700; }
.orders-subtitle { margin-top: 7rpx; color: #8B97A8; font-size: 23rpx; }
.calendar-button { width: 64rpx; height: 64rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #FFFFFF; box-shadow: 0 8rpx 24rpx rgba(44,74,108,.08); }
.summary-card { height: 142rpx; margin-top: 25rpx; padding: 20rpx 15rpx; display: flex; align-items: center; border-radius: 25rpx; color: #FFFFFF; background: linear-gradient(120deg, #3285F0, #4A9DF8); box-shadow: 0 14rpx 32rpx rgba(49,132,239,.2); }
.summary-item { min-width: 0; flex: 1; display: flex; align-items: center; flex-direction: column; }
.summary-item > text { color: #DCEBFF; font-size: 21rpx; }
.summary-item > view { margin-top: 9rpx; display: flex; align-items: baseline; }
.summary-value { font-size: 34rpx; font-weight: 700; }
.summary-unit { margin-left: 4rpx; font-size: 21rpx; }
.summary-divider { width: 1rpx; height: 63rpx; background: rgba(255,255,255,.24); }
.filter-tabs { width: 100%; margin-top: 26rpx; white-space: nowrap; }
.filter-tabs-inner { display: inline-flex; padding-right: 20rpx; gap: 12rpx; }
.filter-tab { height: 58rpx; padding: 0 22rpx; display: flex; align-items: center; gap: 7rpx; border: 2rpx solid #E5EAF0; border-radius: 19rpx; color: #68768A; background: #FFFFFF; font-size: 24rpx; }
.filter-tab > text:last-child { min-width: 28rpx; height: 28rpx; padding: 0 7rpx; display: flex; align-items: center; justify-content: center; border-radius: 14rpx; color: #8A96A8; background: #F0F3F7; font-size: 20rpx; }
.filter-tab.active { border-color: #C9E0FD; color: #3488F4; background: #EAF3FF; font-weight: 600; }
.filter-tab.active > text:last-child { color: #FFFFFF; background: #3A8EF5; }
.list-heading { margin: 29rpx 4rpx 17rpx; display: flex; align-items: center; justify-content: space-between; }
.list-heading > text { font-size: 28rpx; font-weight: 700; }
.list-heading > view { display: flex; align-items: center; gap: 6rpx; color: #8D99A9; font-size: 21rpx; }
.order-card { margin-bottom: 18rpx; padding: 22rpx 23rpx 19rpx; border: 2rpx solid #F0F2F6; border-radius: 24rpx; background: #FFFFFF; box-shadow: 0 8rpx 26rpx rgba(39,65,96,.05); }
.order-top { display: flex; align-items: center; justify-content: space-between; }
.order-kind { min-width: 0; display: flex; align-items: center; color: #3A4658; font-size: 24rpx; font-weight: 600; }
.kind-icon { width: 52rpx; height: 52rpx; flex: 0 0 52rpx; display: flex; align-items: center; justify-content: center; border-radius: 17rpx; background: #E9F3FF; }
.kind-icon.purple { background: #F0EDFF; }
.kind-icon.orange { background: #FFF2E3; }
.order-kind > text { margin-left: 12rpx; }
.order-status { flex-shrink: 0; padding: 7rpx 12rpx; border-radius: 10rpx; font-size: 21rpx; }
.order-status.done { color: #2AA27A; background: #E8F7F2; }
.order-status.progress { color: #4188E8; background: #EAF3FF; }
.order-status.warning { color: #D98632; background: #FFF2E3; }
.order-station { margin-top: 19rpx; display: flex; align-items: center; }
.order-station text { min-width: 0; flex: 1; overflow: hidden; font-size: 28rpx; font-weight: 600; text-overflow: ellipsis; white-space: nowrap; }
.order-info { margin-top: 17rpx; padding: 13rpx 16rpx; border-radius: 16rpx; background: #F7F9FC; }
.order-info > view { min-height: 39rpx; display: flex; align-items: center; justify-content: space-between; }
.order-info text { color: #8A96A7; font-size: 22rpx; }
.order-info > view > text:last-child { max-width: 65%; overflow: hidden; color: #505E71; text-align: right; text-overflow: ellipsis; white-space: nowrap; }
.order-bottom { margin-top: 17rpx; display: flex; align-items: center; }
.order-amount { display: flex; align-items: baseline; color: #8A96A6; font-size: 21rpx; }
.order-amount text:last-child { margin-left: 9rpx; color: #273244; font-size: 28rpx; font-weight: 700; }
.order-action { height: 50rpx; margin-left: auto; padding: 0 18rpx; display: flex; align-items: center; justify-content: center; border: 2rpx solid #C9DCF4; border-radius: 16rpx; color: #4188E8; font-size: 22rpx; }
.order-action.primary { border: 0; color: #FFFFFF; background: #398BF5; }
.empty-orders { padding: 90rpx 0; display: flex; align-items: center; flex-direction: column; color: #9BA6B5; font-size: 24rpx; }
.empty-orders text { margin-top: 18rpx; }
.end-tip { margin: 30rpx 0; display: flex; align-items: center; justify-content: center; color: #ABB4C0; font-size: 20rpx; }
.end-tip view { width: 70rpx; height: 1rpx; margin: 0 13rpx; background: #DDE2E9; }
.bottom-spacer { height: calc(160rpx + env(safe-area-inset-bottom)); }
@media screen and (max-width: 360px) {
  .orders-page { padding-right: 22rpx; padding-left: 22rpx; }
  .summary-card { height: 128rpx; }
  .order-card { padding-right: 18rpx; padding-left: 18rpx; }
  .order-info > view > text:last-child { max-width: 61%; }
}

/* visual polish */
.orders-page{padding-right:30rpx;padding-left:30rpx;background:linear-gradient(180deg,#FAFCFF 0,#F5F8FC 360rpx)}
.calendar-button{width:68rpx;height:68rpx;border-radius:23rpx;box-shadow:0 10rpx 28rpx rgba(44,74,108,.09)}
.summary-card{height:150rpx;margin-top:28rpx;padding:23rpx 17rpx;border-radius:28rpx;box-shadow:0 17rpx 38rpx rgba(49,132,239,.22)}
.filter-tabs{margin-top:29rpx}.filter-tab{height:62rpx;padding:0 24rpx;border-radius:21rpx}
.order-card{margin-bottom:22rpx;padding:25rpx 25rpx 22rpx;border-radius:27rpx;box-shadow:var(--gy-card-shadow)}
.kind-icon{width:56rpx;height:56rpx;flex-basis:56rpx;border-radius:18rpx}.order-info{padding:16rpx 18rpx;border-radius:18rpx}.order-action{height:54rpx;padding:0 20rpx;border-radius:18rpx}
@media screen and (max-width:360px){.orders-page{padding-right:22rpx;padding-left:22rpx}.summary-card{height:136rpx}.order-card{padding:22rpx 20rpx 20rpx}}



/* Keep order context and filters visually anchored in the scrolling list. */
.orders-header{position:sticky;z-index:12;top:0;margin:calc(-1 * (var(--status-bar-height,0px) + 50rpx)) -30rpx 0;padding:calc(var(--status-bar-height,0px) + 50rpx) 30rpx 16rpx;background:rgba(248,251,255,.96);box-shadow:0 8rpx 22rpx rgba(37,66,99,.07);backdrop-filter:blur(18rpx)}
@media screen and (max-width:360px){.orders-header{margin-right:-22rpx;margin-left:-22rpx;padding-right:22rpx;padding-left:22rpx}}



/* Flat orders list: cards and controls are outlined; blue summary keeps just a soft brand elevation. */
.calendar-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.summary-card{box-shadow:var(--gy-shadow-brand)}
.order-card{border-color:var(--gy-surface-border);box-shadow:none}
.orders-header{box-shadow:var(--gy-shadow-divider)}

</style>
