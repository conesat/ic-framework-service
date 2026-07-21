<template>
  <view class="detail-page">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#252E3D" size="21"></uv-icon></view>
          <text class="nav-title">站点详情</text>
          <view class="nav-button" @tap="contactService"><uv-icon name="more-dot-fill" color="#252E3D" size="20"></uv-icon></view>
        </view>

    <scroll-view class="detail-scroll" scroll-y :show-scrollbar="false">
      <view class="top-hero">
        
        <view class="station-illustration" aria-label="换电站示意图">
          <view class="illustration-shadow"></view>
          <view class="illustration-cabinet"><view v-for="item in 6" :key="item"></view></view>
          <view class="illustration-battery"><view></view></view>
          <view class="illustration-copy"><text>换电站</text><text>安全 · 便捷 · 高效</text></view>
        </view>
      </view>

      <view class="content">
        <view class="station-summary">
          <view class="summary-heading"><view class="open-tag"><view></view><text>{{ station.status === 1 ? '营业中' : '维护中' }} {{ station.businessHours || '06:00-24:00' }}</text></view><text class="rating">{{ station.rating || '4.8' }}分</text></view>
          <text class="station-name">{{ station.name }}</text>
          <view class="address"><uv-icon name="map-fill" color="#3889F5" size="18"></uv-icon><text>{{ station.city }}{{ station.district }}{{ station.address }}（{{ station.locationHint || '科技园地铁站 A 出口' }}）</text></view>
          <view class="distance-row"><text>距离您 {{ station.distance || '326m' }}</text><view></view><text>步行约 {{ station.eta || '4分钟' }}</text><view class="mini-navigate" @tap="startNavigation"><uv-icon name="map" color="#3688F3" size="17"></uv-icon><text>导航</text></view></view>
        </view>

        <view class="availability-card">
          <view class="availability-item"><text>可用电池</text><view><text>{{ station.availableBatteryCount || 0 }}</text><text>块</text></view><text class="stock-tip">电量充足</text></view>
          <view class="availability-line"></view>
          <view class="availability-item"><text>可还空位</text><view><text>{{ station.availableReturnSlots || 0 }}</text><text>个</text></view><text class="stock-tip">空位充足</text></view>
          <view class="reserve-mini" @tap="reserveBattery"><uv-icon name="calendar" color="#FFFFFF" size="17"></uv-icon><view><text>预约电池</text><text>提前锁定电池</text></view></view>
        </view>

        <view class="service-strip"><view><uv-icon name="calendar" color="#3688F3" size="17"></uv-icon><text>支持预约</text></view><view><uv-icon name="lock" color="#3688F3" size="17"></uv-icon><text>可暂存电池</text></view><view><uv-icon name="home" color="#3688F3" size="17"></uv-icon><text>卫生间</text></view><view><uv-icon name="grid" color="#3688F3" size="17"></uv-icon><text>休息区</text></view><view><uv-icon name="bag" color="#3688F3" size="17"></uv-icon><text>饮水机</text></view><view><uv-icon name="car" color="#3688F3" size="17"></uv-icon><text>停车位</text></view></view>

        <view class="section-block">
          <view class="section-heading"><text>站点信息</text><view @tap="showStationInfo">查看更多 <uv-icon name="arrow-right" color="#A3ADBA" size="13"></uv-icon></view></view>
          <view class="info-card">
            <view class="info-row"><view><uv-icon name="clock" color="#398AF5" size="18"></uv-icon><text>营业时间</text></view><text>{{ station.businessHours || '06:00-24:00' }}（全天营业）</text></view>
            <view class="info-row"><view><uv-icon name="phone" color="#398AF5" size="18"></uv-icon><text>联系电话</text></view><text class="phone" @tap="callStation">{{ station.contactPhone || '0755-8888 8888' }}</text></view>
            <view class="info-row notice"><view><uv-icon name="info-circle" color="#398AF5" size="18"></uv-icon><text>站点公告</text></view><text>{{ station.announcement || '本站支持预约，提前锁定电池，到站即可更换。' }}</text></view>
          </view>
        </view>

        <view class="section-block reviews-block">
          <view class="section-heading"><text>用户评价（{{ reviewCount }}）</text><view @tap="showMoreReviews">查看更多 <uv-icon name="arrow-right" color="#A3ADBA" size="13"></uv-icon></view></view>
          <view class="review-card">
            <view class="rating-line"><text class="big-score">{{ station.rating || '4.8' }}</text><view class="stars"><uv-icon v-for="n in 5" :key="n" name="star-fill" color="#FFB74D" size="16"></uv-icon></view><text>服务热情 · 换电快速</text></view>
            <view class="review-tags"><text v-for="tag in reviewTags" :key="tag">{{ tag }}</text></view>
            <view v-if="reviews[0]" class="review-item"><view class="avatar">{{ reviews[0].avatar }}</view><view><view class="reviewer"><text>{{ reviews[0].name }}</text><text>{{ reviews[0].score }}</text></view><text class="review-text">{{ reviews[0].content }}</text><text class="review-date">{{ reviews[0].date }}</text></view></view>
          </view>
        </view>
        <view class="bottom-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-action-bar">
      <view class="favorite-button" @tap="toggleFavorite"><uv-icon :name="favorite ? 'heart-fill' : 'heart'" :color="favorite ? '#F07076' : '#718096'" size="22"></uv-icon><text>{{ favorite ? '已收藏' : '收藏' }}</text></view>
      <view class="bottom-scan" @tap="scanBattery"><uv-icon name="scan" color="#FFFFFF" size="21"></uv-icon><text>扫码换电</text></view>
      <view class="bottom-navigation" @tap="startNavigation"><uv-icon name="map" color="#3688F3" size="20"></uv-icon><text>导航到这里</text></view>
    </view>

    <view v-if="reviewsVisible" class="reviews-panel">
      <view class="reviews-panel-header">
        <view class="reviews-panel-back" @tap="closeMoreReviews"><uv-icon name="arrow-left" color="#283447" size="21"></uv-icon></view>
        <text>用户评价</text>
        <view class="reviews-panel-spacer"></view>
      </view>
      <scroll-view class="reviews-panel-scroll" scroll-y :show-scrollbar="false">
        <view class="reviews-panel-content">
          <view class="reviews-overview">
            <view class="reviews-score"><text>{{ station.rating || '4.8' }}</text><text>综合评分</text></view>
            <view class="reviews-rating-copy"><view class="stars"><uv-icon v-for="n in 5" :key="n" name="star-fill" color="#FFB74D" size="17"></uv-icon></view><text>来自 {{ reviewCount }} 位骑友的真实反馈</text></view>
            <view class="reviews-write" @tap="writeReview"><uv-icon name="edit-pen" color="#358AF5" size="17"></uv-icon><text>写评价</text></view>
          </view>
          <view class="reviews-tags-grid"><text v-for="tag in reviewTags" :key="tag">{{ tag }}</text></view>
          <scroll-view class="reviews-filter-scroll" scroll-x :show-scrollbar="false"><view class="reviews-filter-row"><view v-for="filter in reviewFilters" :key="filter" class="review-filter" :class="{ active: activeReviewFilter === filter }" @tap="activeReviewFilter = filter">{{ filter }}</view></view></scroll-view>
          <view class="reviews-list-heading"><text>全部评价</text><text>{{ visibleReviews.length }} 条展示</text></view>
          <view v-for="review in visibleReviews" :key="review.id" class="full-review-item">
            <view class="avatar" :class="`avatar-${review.tone}`">{{ review.avatar }}</view>
            <view class="full-review-copy">
              <view class="full-reviewer"><view><text>{{ review.name }}</text><text>{{ review.city }}</text></view><view><uv-icon v-for="n in 5" :key="n" name="star-fill" color="#FFB74D" size="13"></uv-icon><text>{{ review.score }}</text></view></view>
              <text class="full-review-text">{{ review.content }}</text>
              <view v-if="review.hasImage" class="review-photo-row"><view v-for="n in 3" :key="n" :class="`review-photo photo-${(review.id + n) % 3}`"><uv-icon name="image" color="#6EA9E9" size="17"></uv-icon></view></view>
              <view class="full-review-footer"><text>{{ review.date }}</text><view class="review-helpful" :class="{ active: review.helpful }" @tap="toggleHelpful(review)"><uv-icon :name="review.helpful ? 'thumb-up-fill' : 'thumb-up'" :color="review.helpful ? '#358AF5' : '#93A0AF'" size="15"></uv-icon><text>有用 {{ review.helpfulCount }}</text></view></view>
            </view>
          </view>
          <view v-if="!visibleReviews.length" class="review-empty"><uv-icon name="file-text" color="#A5B2C2" size="28"></uv-icon><text>暂无符合条件的评价</text><text>试试切换筛选条件</text></view>
          <view class="reviews-list-bottom"></view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { createSwapOrder, getStationDetail } from '../../api/energy'
import { addLocalOrder } from '../../utils/localOrders'

const DEFAULT_STATION = {
  name: '科技园换电站', city: '深圳市', district: '南山区', address: '科技南路15号',
  businessHours: '06:00-24:00', contactPhone: '0755-8888 8888', status: 1,
  distance: '326m', eta: '4分钟', locationHint: '科技园地铁站A出口，步行约4分钟',
  availableBatteryCount: 12, availableReturnSlots: 8, rating: 4.8,
  announcement: '本站支持预约，提前锁定电池，到站即可更换。'
}

const SAMPLE_REVIEWS = [
  { id: 1, avatar: '骑', tone: 'blue', name: '骑行小达人', city: '南山区', score: '5.0', content: '电池很新，换电速度快，周边停车也方便，推荐！', date: '今天 08:45', hasImage: true, helpful: false, helpfulCount: 18 },
  { id: 2, avatar: '林', tone: 'green', name: '林间骑客', city: '福田区', score: '5.0', content: '早高峰也没有排队，柜门识别很灵敏，站内环境干净。', date: '昨天 19:20', hasImage: false, helpful: false, helpfulCount: 12 },
  { id: 3, avatar: '阿', tone: 'orange', name: '阿杰的电车', city: '宝安区', score: '4.0', content: '满电电池数量比较稳定，导航入口准确，晚上照明也很好。', date: '6月28日', hasImage: true, helpful: false, helpfulCount: 9 },
  { id: 4, avatar: '简', tone: 'purple', name: '简简单单骑', city: '南山区', score: '5.0', content: '客服响应很快，第一次使用也有清晰提示，整体体验不错。', date: '6月25日', hasImage: false, helpful: false, helpfulCount: 6 },
  { id: 5, avatar: '北', tone: 'teal', name: '北环通勤者', city: '龙华区', score: '4.0', content: '位置靠近主路，很好找；希望周末可以再多补充一些电池。', date: '6月21日', hasImage: true, helpful: false, helpfulCount: 4 }
]

export default {
  data() {
    return {
      favorite: false,
      stationId: null,
      station: DEFAULT_STATION,
      reviewCount: 128,
      reviewTags: ['电池新（48）', '服务好（36）', '环境整洁（28）'],
      reviewFilters: ['全部', '好评', '有图'],
      activeReviewFilter: '全部',
      reviewsVisible: false,
      reviews: SAMPLE_REVIEWS.map(item => ({ ...item }))
    }
  },
  computed: {
    visibleReviews() {
      if (this.activeReviewFilter === '好评') return this.reviews.filter(item => Number(item.score) >= 4.5)
      if (this.activeReviewFilter === '有图') return this.reviews.filter(item => item.hasImage)
      return this.reviews
    }
  },
  onLoad({ id }) { this.stationId = id || null; if (this.stationId) this.loadStation() },
  methods: {
    async loadStation() { try { const station = await getStationDetail(this.stationId); if (station) this.station = station } catch (_) {} },
    goBack() { uni.navigateBack({ delta: 1, fail: () => uni.reLaunch({ url: '/pages/index/index?tab=1' }) }) },
    notify(message) { uni.showToast({ title: message, icon: 'none' }) },
    startNavigation() {
      const { latitude, longitude, name, address } = this.station
      if (latitude && longitude) return uni.openLocation({ latitude: Number(latitude), longitude: Number(longitude), name, address, fail: () => this.notify('打开地图失败') })
      uni.showModal({ title: '导航到这里', content: `${name}
${address}
距离您 ${this.station.distance || '326m'}`, showCancel: false })
    },
    async reserveBattery() {
      if ((this.station.availableBatteryCount || 0) < 1) return this.notify('当前暂无可预约电池')
      try {
        if (this.stationId && uni.getStorageSync('energy_token')) {
          const order = await createSwapOrder(this.stationId, 1)
          this.notify(`预约成功，订单号：${order.orderNo}`)
          return this.loadStation()
        }
      } catch (_) {}
      const order = addLocalOrder({ category: 'swap', type: '换电预约', station: this.station.name, detailLabel: '预约电池', detail: '72V 30Ah 满电电池', status: '待到站', statusClass: 'progress', action: '查看详情' })
      uni.showModal({ title: '预约成功', content: `已为您锁定 1 块满电电池。
订单号：${order.no}
请在 30 分钟内到站换电。`, showCancel: false, success: () => uni.navigateTo({ url: `/pages/order/detail?no=${order.no}&category=swap` }) })
    },
    scanBattery() {
      // #ifdef H5
      uni.navigateTo({ url: '/pages/swap/success' })
      // #endif
      // #ifndef H5
      uni.scanCode({ onlyFromCamera: false, success: () => uni.navigateTo({ url: '/pages/swap/success' }), fail: () => this.notify('已取消扫码') })
      // #endif
    },
    callStation() { uni.makePhoneCall({ phoneNumber: (this.station.contactPhone || '075588888888').replace(/\s/g, ''), fail: () => this.notify('请拨打站点联系电话') }) },
    contactService() { uni.showActionSheet({ itemList: ['拨打客服 400-888-8899', '提交故障报修'], success: ({ tapIndex }) => tapIndex === 0 ? uni.makePhoneCall({ phoneNumber: '4008888899' }) : uni.navigateTo({ url: '/pages/repair/create' }) }) },
    showStationInfo() { uni.showModal({ title: '站点信息', content: `营业时间：${this.station.businessHours || '24小时'}
服务范围：换电、暂存、预约
地址：${this.station.address}`, showCancel: false }) },
    showMoreReviews() { this.reviewsVisible = true },
    closeMoreReviews() { this.reviewsVisible = false },
    toggleHelpful(review) {
      review.helpful = !review.helpful
      review.helpfulCount += review.helpful ? 1 : -1
    },
    writeReview() { this.notify('完成一次换电订单后即可发布评价') },
    toggleFavorite() { this.favorite = !this.favorite; this.notify(this.favorite ? '已收藏该换电站' : '已取消收藏') }
  }
}
</script>

<style scoped>
.detail-page{height:100%;position:relative;overflow:hidden;color:#273142;background:#F9FAFC}.detail-scroll{height:calc(100% - 111rpx - env(safe-area-inset-bottom))}.top-hero{height:357rpx;position:relative;box-sizing:border-box;padding-top:calc(var(--status-bar-height,44rpx) + 5rpx);background:linear-gradient(180deg,#F9FBFF 0%,#F4F8FF 100%)}.nav-bar{height:62rpx;position:relative;z-index:3;transform:translateY(80rpx);padding:0 28rpx;display:flex;align-items:center;justify-content:space-between}.nav-button{width:48rpx;height:48rpx;display:flex;align-items:center;justify-content:center;border-radius:50%;background:rgba(255,255,255,.84)}.nav-title{font-size:31rpx;font-weight:700;color:#202A3A}.station-illustration{position:absolute;right:28rpx;bottom:16rpx;left:28rpx;height:205rpx;overflow:hidden;border-radius:27rpx;background:linear-gradient(125deg,#E7F1FF,#D6E7FE)}.illustration-shadow{position:absolute;left:62rpx;right:62rpx;bottom:18rpx;height:21rpx;border-radius:50%;background:rgba(70,136,220,.13)}.illustration-cabinet{position:absolute;left:207rpx;bottom:30rpx;width:112rpx;height:134rpx;padding:13rpx;box-sizing:border-box;display:grid;grid-template-columns:repeat(2,1fr);gap:8rpx;border:5rpx solid #3C8DF6;border-radius:15rpx;background:linear-gradient(145deg,#65AEFF,#2D82EC);transform:skewY(-4deg);box-shadow:14rpx 13rpx 0 rgba(60,138,245,.16)}.illustration-cabinet view{border-radius:4rpx;background:#DCEEFF;opacity:.94}.illustration-battery{position:absolute;left:350rpx;bottom:51rpx;width:43rpx;height:64rpx;border:5rpx solid #3B8CF5;border-radius:9rpx;background:#ECF5FF}.illustration-battery:after{content:'';position:absolute;right:-10rpx;top:20rpx;width:7rpx;height:21rpx;border-radius:0 4rpx 4rpx 0;background:#3B8CF5}.illustration-battery view{position:absolute;left:8rpx;right:8rpx;bottom:7rpx;height:30rpx;border-radius:3rpx;background:#50B89A}.illustration-copy{position:absolute;top:49rpx;left:36rpx;display:flex;flex-direction:column}.illustration-copy text{color:#263450;font-size:37rpx;font-weight:700}.illustration-copy>text:last-child{margin-top:10rpx;color:#7B91AF;font-size:23rpx}.content{padding:0 28rpx;box-sizing:border-box}.station-summary{margin-top:22rpx;padding:0 2rpx}.summary-heading{display:flex;align-items:center;justify-content:space-between}.open-tag{display:flex;align-items:center;color:#26A978;font-size:25rpx}.open-tag view{width:9rpx;height:9rpx;margin-right:8rpx;border-radius:50%;background:#29BF8E}.rating{color:#F1A64D;font-size:24rpx}.station-name{display:block;margin-top:19rpx;color:#222B3B;font-size:38rpx;font-weight:700}.address{display:flex;align-items:flex-start;gap:9rpx;margin-top:14rpx;color:#667286;font-size:24rpx;line-height:1.5}.address text{flex:1;min-width:0;word-break:break-all}.distance-row{display:flex;align-items:center;margin-top:14rpx;color:#8B96A6;font-size:24rpx}.distance-row>view:not(.mini-navigate){width:2rpx;height:20rpx;margin:0 15rpx;background:#DDE3EC}.mini-navigate{margin-left:auto;display:flex;align-items:center;gap:5rpx;color:#3688F3;font-size:24rpx}.availability-card{min-height:177rpx;margin-top:25rpx;padding:22rpx 23rpx;box-sizing:border-box;display:flex;align-items:center;position:relative;border-radius:24rpx;background:#fff;box-shadow:0 10rpx 30rpx rgba(47,76,110,.06)}.availability-item{min-width:0;flex:1;display:flex;align-items:center;flex-direction:column}.availability-item>text:first-child{color:#7F8A9A;font-size:24rpx}.availability-item>view{margin-top:8rpx;display:flex;align-items:baseline}.availability-item>view>text:first-child{color:#3388F5;font-size:44rpx;line-height:1}.availability-item>view>text:last-child{margin-left:4rpx;color:#536174;font-size:24rpx}.stock-tip{margin-top:8rpx;color:#31B486;font-size:22rpx}.availability-line{width:2rpx;height:86rpx;flex:0 0 2rpx;margin:0 10rpx;background:#EDF0F5}.reserve-mini{position:static;flex:0 0 auto;margin-left:14rpx;padding:11rpx 13rpx;border-radius:13rpx;display:flex;align-items:center;gap:7rpx;color:#fff;background:#388AF5;box-shadow:0 8rpx 17rpx rgba(56,138,245,.19)}.reserve-mini>view{display:flex;flex-direction:column}.reserve-mini text{font-size:23rpx;font-weight:600}.reserve-mini>view>text:last-child{margin-top:2rpx;color:#DCEBFF;font-size:20rpx}.service-strip{margin-top:21rpx;padding:8rpx;border-radius:19rpx;display:grid;grid-template-columns:repeat(6,minmax(0,1fr));align-items:start;background:#EFF6FF}.service-strip>view{display:flex;align-items:center;flex-direction:column;min-width:0;color:#647187;font-size:21rpx;text-align:center}.service-strip text{margin-top:7rpx;white-space:nowrap}.section-block{margin-top:15rpx}.section-heading{margin-bottom:18rpx;display:flex;align-items:center;justify-content:space-between}.section-heading>text{color:#252F3F;font-size:31rpx;font-weight:700}.section-heading>view{display:flex;align-items:center;color:#9BA5B4;font-size:23rpx}.info-card,.review-card{border-radius:24rpx;background:#fff;box-shadow:0 10rpx 30rpx rgba(47,76,110,.055)}.info-card{padding:0 24rpx}.info-row{min-height:54rpx;display:flex;align-items:center;justify-content:space-between;border-bottom:2rpx solid #EFF2F6;color:#4E5D70;font-size:24rpx}.info-row:last-child{border:0}.info-row>view{width:150rpx;flex:0 0 150rpx;display:flex;align-items:center;gap:12rpx;color:#59687A}.info-row>text{min-width:0;flex:1;color:#3A4759;text-align:left;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.info-row .phone{color:#3788F4}.info-row.notice{min-height:54rpx;align-items:center}.notice>text{max-width:none;color:#788497;line-height:1.5;text-align:left}.reviews-block{margin-top:15rpx;margin-bottom:15rpx}.review-card{overflow:hidden}.rating-line{padding:17rpx 23rpx 10rpx;display:flex;align-items:center}.big-score{color:#243044;font-size:45rpx;font-weight:700}.stars{margin-left:18rpx;display:flex}.rating-line>text:last-child{margin-left:auto;color:#929CAC;font-size:22rpx}.review-tags{padding:0 23rpx 14rpx;display:flex;flex-wrap:wrap;gap:10rpx;border-bottom:2rpx solid #EFF2F6}.review-tags text{padding:7rpx 10rpx;border-radius:9rpx;color:#3F89E9;background:#EAF3FF;font-size:22rpx}.review-item{padding:17rpx 23rpx;display:flex}.avatar{width:49rpx;height:49rpx;display:flex;align-items:center;justify-content:center;flex-shrink:0;border-radius:50%;color:#fff;background:linear-gradient(145deg,#75B7FF,#3A8EF6);font-size:23rpx}.review-item>view:last-child{margin-left:14rpx;display:flex;flex:1;flex-direction:column}.reviewer{display:flex;align-items:center;justify-content:space-between;color:#3B475A;font-size:24rpx;font-weight:600}.reviewer text:last-child{color:#F2A94B;font-size:22rpx}.review-text{margin-top:9rpx;color:#667386;font-size:24rpx;line-height:1.5}.review-date{margin-top:10rpx;color:#A4ADBA;font-size:21rpx}.bottom-spacer{height:158rpx}.bottom-action-bar{height:calc(111rpx + env(safe-area-inset-bottom));padding:10rpx 28rpx env(safe-area-inset-bottom);box-sizing:border-box;position:absolute;z-index:5;right:0;bottom:0;left:0;display:flex;align-items:center;gap:15rpx;background:rgba(255,255,255,.98);box-shadow:0 -5rpx 24rpx rgba(42,73,112,.07)}.favorite-button{width:83rpx;display:flex;align-items:center;flex-direction:column;color:#718096;font-size:21rpx}.favorite-button text{margin-top:5rpx}.bottom-scan,.bottom-navigation{height:78rpx;display:flex;align-items:center;justify-content:center;gap:7rpx;border-radius:17rpx;font-size:25rpx;font-weight:600}.bottom-scan{width:205rpx;color:#fff;background:#388AF5}.bottom-navigation{flex:1;color:#3688F3;border:2rpx solid #BFD9FA;background:#F5F9FF}

/* visual polish */
.top-hero{box-shadow:0 14rpx 34rpx rgba(41,83,132,.08)}
.nav-button{width:62rpx;height:62rpx;border-radius:21rpx;box-shadow:0 8rpx 22rpx rgba(41,71,106,.07)}
.station-summary{padding:29rpx 28rpx 27rpx;border-radius:29rpx;box-shadow:var(--gy-card-shadow)}
.availability-card{min-height:184rpx;padding:25rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.service-strip{margin-top:24rpx;padding:12rpx 8rpx;border-radius:23rpx}
.section-block{margin-top:23rpx}.info-card,.review-card{border-radius:28rpx;box-shadow:var(--gy-card-shadow)}
.info-card{padding:4rpx 26rpx}.info-row{min-height:62rpx}.rating-line{padding:21rpx 25rpx 13rpx}.review-item{padding:21rpx 25rpx}
.bottom-scan,.bottom-navigation{height:82rpx;border-radius:20rpx}
@media screen and (max-width:360px){.station-summary{padding:24rpx 22rpx}.availability-card{padding:22rpx 19rpx}.service-strip{padding-right:4rpx;padding-left:4rpx}}



/* Persistent page header: kept above the hero instead of inside its scroll container. */
.fixed-page-header{height:calc(var(--status-bar-height, 0px) + 94rpx);position:absolute;z-index:20;top:0;right:0;left:0;padding:var(--status-bar-height, 0px) 28rpx 0;box-sizing:border-box;transform:none;background:linear-gradient(180deg,rgba(249,251,255,.98) 0%,rgba(249,251,255,.88) 74%,rgba(249,251,255,0) 100%);backdrop-filter:blur(18rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}}



/* Flat station information cards: quiet outlines improve density and scanning. */
.illustration-cabinet{box-shadow:none}
.top-hero{box-shadow:none}
.nav-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.station-summary,.availability-card,.info-card,.review-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.reserve-mini{box-shadow:var(--gy-shadow-brand)}
.bottom-action-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}


/* Review directory: replaces the placeholder modal with a readable, filterable in-page view. */
.reviews-panel{position:absolute;z-index:40;inset:0;background:#F7FAFE}
.reviews-panel-header{height:calc(var(--status-bar-height, 0px) + 100rpx);padding:var(--status-bar-height, 0px) 28rpx 0;box-sizing:border-box;display:flex;align-items:center;justify-content:space-between;border-bottom:2rpx solid var(--gy-surface-border);background:rgba(250,252,255,.98)}
.reviews-panel-header>text{color:#273346;font-size:31rpx;font-weight:700}.reviews-panel-back,.reviews-panel-spacer{width:64rpx;height:64rpx}.reviews-panel-back{display:flex;align-items:center;justify-content:center;border:2rpx solid var(--gy-surface-border);border-radius:20rpx;background:#FFFFFF}
.reviews-panel-scroll{height:calc(100% - var(--status-bar-height, 0px) - 100rpx)}.reviews-panel-content{padding:22rpx 28rpx 0}
.reviews-overview{min-height:160rpx;padding:24rpx;display:flex;align-items:center;border:2rpx solid #D8E8FC;border-radius:28rpx;background:linear-gradient(135deg,#F0F7FF,#FFFFFF)}
.reviews-score{width:108rpx;flex:0 0 108rpx;display:flex;align-items:center;flex-direction:column;border-right:2rpx solid #D8E7F8}.reviews-score text:first-child{color:#2C86F2;font-size:48rpx;line-height:1;font-weight:700}.reviews-score text:last-child{margin-top:9rpx;color:#7E90A5;font-size:20rpx}
.reviews-rating-copy{min-width:0;margin-left:21rpx;display:flex;flex:1;flex-direction:column}.reviews-rating-copy .stars{margin-left:0}.reviews-rating-copy>text{margin-top:12rpx;color:#718398;font-size:21rpx}
.reviews-write{min-width:82rpx;min-height:64rpx;margin-left:12rpx;padding:0 12rpx;display:flex;align-items:center;justify-content:center;gap:6rpx;border:2rpx solid #BBD9FB;border-radius:19rpx;color:#358AF5;background:#FFFFFF;font-size:21rpx;font-weight:600}
.reviews-tags-grid{margin-top:15rpx;display:flex;flex-wrap:wrap;gap:10rpx}.reviews-tags-grid text{min-height:56rpx;padding:0 14rpx;display:flex;align-items:center;border-radius:15rpx;color:#3B84E4;background:#EAF3FF;font-size:21rpx}
.reviews-filter-scroll{width:100%;margin-top:19rpx;white-space:nowrap}.reviews-filter-row{display:inline-flex;gap:10rpx;padding-right:8rpx}.review-filter{min-height:62rpx;padding:0 19rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid var(--gy-surface-border);border-radius:18rpx;color:#67778B;background:#FFFFFF;font-size:22rpx}.review-filter.active{border-color:#B9D8FB;color:#3186F2;background:#EAF4FF;font-weight:600}
.reviews-list-heading{margin:25rpx 3rpx 14rpx;display:flex;align-items:center;justify-content:space-between}.reviews-list-heading text:first-child{color:#293547;font-size:28rpx;font-weight:700}.reviews-list-heading text:last-child{color:#92A0AF;font-size:20rpx}
.full-review-item{padding:22rpx 2rpx;display:flex;border-bottom:2rpx solid #E9EEF4}.full-review-copy{min-width:0;margin-left:14rpx;display:flex;flex:1;flex-direction:column}.full-reviewer{display:flex;align-items:center;justify-content:space-between}.full-reviewer>view:first-child{min-width:0;display:flex;align-items:center;gap:8rpx}.full-reviewer>view:first-child text:first-child{overflow:hidden;color:#344155;font-size:24rpx;font-weight:650;text-overflow:ellipsis;white-space:nowrap}.full-reviewer>view:first-child text:last-child{color:#97A4B3;font-size:19rpx}.full-reviewer>view:last-child{display:flex;align-items:center;gap:2rpx}.full-reviewer>view:last-child>text{margin-left:5rpx;color:#E89B39;font-size:21rpx;font-weight:600}
.full-review-text{margin-top:11rpx;color:#526276;font-size:23rpx;line-height:1.58}.review-photo-row{margin-top:13rpx;display:flex;gap:9rpx}.review-photo{width:116rpx;height:86rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid #DCE9F7;border-radius:15rpx}.photo-0{background:#E6F2FF}.photo-1{background:#EFF8F4}.photo-2{background:#FFF5E8}
.full-review-footer{margin-top:14rpx;display:flex;align-items:center;justify-content:space-between;color:#9CA8B6;font-size:19rpx}.review-helpful{min-height:52rpx;padding:0 10rpx;display:flex;align-items:center;gap:5rpx;border-radius:14rpx;color:#8D9AA9;background:#F4F7FA}.review-helpful.active{color:#358AF5;background:#EAF4FF}
.avatar-green{background:linear-gradient(145deg,#77C8A5,#35A879)}.avatar-orange{background:linear-gradient(145deg,#FFC171,#EC9341)}.avatar-purple{background:linear-gradient(145deg,#AE9CFF,#7566D9)}.avatar-teal{background:linear-gradient(145deg,#6ACAC7,#34979A)}
.review-empty{min-height:300rpx;display:flex;align-items:center;justify-content:center;flex-direction:column;color:#63748A;font-size:23rpx}.review-empty text:nth-child(2){margin-top:16rpx;font-weight:600}.review-empty text:last-child{margin-top:9rpx;color:#9AA8B7;font-size:20rpx}.reviews-list-bottom{height:40rpx}
@media screen and (max-width:360px){.reviews-panel-header{padding-right:22rpx;padding-left:22rpx}.reviews-panel-content{padding-right:22rpx;padding-left:22rpx}.reviews-overview{padding:20rpx}.reviews-score{width:90rpx;flex-basis:90rpx}.reviews-rating-copy{margin-left:16rpx}.reviews-write{min-width:auto;padding-right:9rpx;padding-left:9rpx;font-size:20rpx}.review-photo{width:104rpx;height:78rpx}.full-review-text{font-size:22rpx}}



/* Adversarial UI audit: protect 360px readability and touch targets. */
.fixed-page-header{height:calc(var(--status-bar-height, 0px) + 104rpx);padding-right:28rpx;padding-left:28rpx;background:rgba(249,251,255,.96);border-bottom:2rpx solid var(--gy-surface-border);box-shadow:none}
.nav-button{width:88rpx;height:88rpx;flex:0 0 88rpx;border:0;background:transparent}
.service-strip{padding:16rpx 12rpx;grid-template-columns:repeat(3,minmax(0,1fr));gap:12rpx;border:2rpx solid #DCEBFC;border-radius:24rpx;background:#F4F8FF}
.service-strip>view{min-height:74rpx;justify-content:center;font-size:22rpx;line-height:1.25}
.service-strip text{margin-top:7rpx;white-space:normal;line-height:1.25}
.section-heading>view{min-height:64rpx;padding:0 4rpx;font-size:22rpx}
.availability-card,.info-card,.review-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.reserve-mini{min-height:86rpx;padding:12rpx 15rpx;border:2rpx solid rgba(255,255,255,.34);box-shadow:var(--gy-shadow-brand)}
.bottom-action-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.bottom-scan,.bottom-navigation{min-height:88rpx;box-shadow:none}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.service-strip{gap:8rpx;padding-right:8rpx;padding-left:8rpx}.service-strip>view{font-size:21rpx}.reserve-mini{margin-left:9rpx;padding-right:11rpx;padding-left:11rpx}.reserve-mini text{font-size:21rpx}.reserve-mini>view>text:last-child{font-size:19rpx}}

</style>
