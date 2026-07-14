<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#283344" size="21"></uv-icon></view>
          <text class="nav-title">故障上报</text>
          <view class="nav-button" @tap="openRepairRecords"><uv-icon name="list" color="#283344" size="20"></uv-icon></view>
        </view>

    <scroll-view class="page-scroll" scroll-y :show-scrollbar="false">
      <view class="repair-page">
        

        <view class="hero-card">
          <view class="hero-icon"><uv-icon name="edit-pen" color="#FFFFFF" size="28"></uv-icon></view>
          <view class="hero-copy"><text>遇到问题，快速告诉我们</text><text>工作日平均 10 分钟响应，紧急问题优先处理</text></view>
          <view class="hero-glow"></view>
        </view>

        <view class="section-title"><text>故障对象</text><text>请选择一项</text></view>
        <view class="object-tabs">
          <view v-for="item in objects" :key="item.key" :class="{ active: objectType === item.key }" @tap="objectType = item.key">
            <view><uv-icon :name="item.icon" :color="objectType === item.key ? '#FFFFFF' : '#5382B8'" size="21"></uv-icon></view>
            <text>{{ item.label }}</text>
          </view>
        </view>

        <view class="section-title"><text>故障类型</text><text>可单选</text></view>
        <view class="issue-grid">
          <view v-for="item in issues" :key="item" :class="{ active: issueType === item }" @tap="issueType = item">
            <view class="radio"><view></view></view>
            <text>{{ item }}</text>
          </view>
        </view>

        <view class="section-title"><text>发生位置</text><text>自动定位</text></view>
        <view class="location-card" @tap="chooseLocation">
          <view class="location-icon"><uv-icon name="map-fill" color="#3D8DF4" size="21"></uv-icon></view>
          <view class="location-copy"><text>{{ station.name }}</text><text>{{ station.address }}</text></view>
          <uv-icon name="arrow-right" color="#A5B0BD" size="14"></uv-icon>
        </view>

        <view class="section-title"><text>问题描述</text><text>{{ description.length }}/200</text></view>
        <view class="description-card">
          <textarea v-model="description" maxlength="200" placeholder="请描述故障现象、发生时间等信息，便于维修人员快速判断" placeholder-class="textarea-placeholder"></textarea>
          <view class="quick-descriptions">
            <text v-for="item in quickDescriptions" :key="item" @tap="appendDescription(item)">{{ item }}</text>
          </view>
        </view>

        <view class="section-title"><text>上传现场照片</text><text>最多 3 张</text></view>
        <view class="upload-row">
          <view class="upload-item" @tap="chooseImage"><uv-icon name="camera" color="#7790AA" size="25"></uv-icon><text>{{ imagePaths.length ? `已添加 ${imagePaths.length} 张` : '添加照片' }}</text></view>
          <view class="upload-example"><view class="cabinet-photo"><view v-for="n in 6" :key="n"></view></view><text>示例：拍清柜门编号</text></view>
        </view>

        <view class="section-title"><text>联系信息</text><text>用于处理反馈</text></view>
        <view class="contact-card">
          <view><uv-icon name="phone" color="#468FF1" size="19"></uv-icon><text>联系电话</text></view>
          <input v-model="phone" type="number" maxlength="11" placeholder="请输入手机号" />
        </view>

        <view class="urgency-card" @tap="urgent = !urgent">
          <view class="urgency-icon"><uv-icon name="warning" color="#E58E43" size="20"></uv-icon></view>
          <view class="urgency-copy"><text>需要紧急处理</text><text>如存在冒烟、异味、漏液等安全风险，请立即勾选</text></view>
          <view class="switch" :class="{ active: urgent }"><view></view></view>
        </view>

        <view class="service-notice"><uv-icon name="kefu-ermai" color="#4A91F2" size="19"></uv-icon><text>安全紧急情况请远离设备并拨打 400-888-8899</text></view>
        <view class="content-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-bar"><view class="submit-button" @tap="submitRepair">提交报修</view></view>
  </view>
</template>

<script>
import { createRepairOrder } from '../../api/energy'
import { addLocalOrder } from '../../utils/localOrders'

export default {
  data() {
    return {
      objectType: 'battery',
      issueType: '无法正常充电',
      description: '',
      phone: '13800138000',
      urgent: false,
      imagePaths: [],
      station: { name: '科技园换电站', address: '深圳市南山区科技园南区科苑路15号' },
      objects: [
        { key: 'battery', label: '电池故障', icon: 'coupon' },
        { key: 'cabinet', label: '换电柜故障', icon: 'grid' },
        { key: 'station', label: '站点环境', icon: 'home' }
      ],
      issues: ['无法正常充电', '电池发热', '柜门无法开启', '扫码无响应', '电池有异响', '其他问题'],
      quickDescriptions: ['无法开柜', '电量异常', '设备无响应']
    }
  },
  onLoad(options) { if (options?.orderNo) this.description = `订单 ${options.orderNo}：` },
  methods: {
    goBack() { uni.navigateBack({ fail: () => uni.reLaunch({ url: '/pages/index/index' }) }) },
    appendDescription(text) { this.description = this.description ? `${this.description}；${text}` : text },
    openRepairRecords() { uni.reLaunch({ url: '/pages/index/index?tab=3' }) },
    chooseLocation() {
      const stations = [
        { name: '科技园换电站', address: '深圳市南山区科技园南区科苑路15号' },
        { name: '深大地铁站换电点', address: '深圳市南山区深南大道深大地铁站A口' }
      ]
      uni.showActionSheet({ itemList: stations.map(item => item.name), success: ({ tapIndex }) => { this.station = stations[tapIndex] } })
    },
    chooseImage() {
      uni.chooseImage({
        count: Math.max(1, 3 - this.imagePaths.length),
        success: ({ tempFilePaths }) => { this.imagePaths = [...this.imagePaths, ...tempFilePaths].slice(0, 3) },
        fail: () => {}
      })
    },
    async submitRepair() {
      if (!this.description || this.description === '订单 ：') return uni.showToast({ title: '请填写问题描述', icon: 'none' })
      if (!/^1\d{10}$/.test(this.phone)) return uni.showToast({ title: '请输入正确的联系电话', icon: 'none' })
      try {
        if (uni.getStorageSync('energy_token')) {
          const order = await createRepairOrder({ repairType: this.issueType, description: this.description, images: this.imagePaths.join(','), contactPhone: this.phone })
          return uni.showModal({ title: '报修已提交', content: `工单号：${order.orderNo || order.id}
我们将尽快与您联系。`, showCancel: false, success: () => uni.reLaunch({ url: '/pages/index/index?tab=3' }) })
        }
      } catch (_) {}
      const order = addLocalOrder({ category: 'repair', station: this.station.name, detail: this.issueType, description: this.description, status: this.urgent ? '紧急处理中' : '处理中', statusClass: 'warning', action: '查看进度' })
      uni.showModal({ title: '报修已提交', content: `工单号：${order.no}
我们将尽快与您联系。`, showCancel: false, success: () => uni.navigateTo({ url: `/pages/order/detail?no=${order.no}&category=repair` }) })
    }
  }
}
</script>

<style scoped>
.page-shell, .page-scroll { width: 100%; height: 100%; background: #F5F8FC; }
.page-shell { position: relative; overflow: hidden; }
.repair-page { min-height: 100%; padding: 0 28rpx; color: #283344; background: linear-gradient(180deg, #F8FBFF 0, #F5F8FC 350rpx); }
.nav-bar { height: calc(var(--status-bar-height, 0px) + 100rpx); padding-top: var(--status-bar-height, 0px); display: flex; align-items: center; justify-content: space-between; }
.nav-button { width: 62rpx; height: 62rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #FFFFFF; box-shadow: 0 7rpx 20rpx rgba(42,71,103,.08); }
.nav-title { font-size: 31rpx; font-weight: 700; }
.hero-card { height: 162rpx; position: relative; overflow: hidden; padding: 0 27rpx; display: flex; align-items: center; border-radius: 27rpx; color: #FFFFFF; background: linear-gradient(120deg, #3285F0, #4A9DF8); box-shadow: 0 15rpx 33rpx rgba(49,132,239,.2); }
.hero-icon { width: 78rpx; height: 78rpx; flex: 0 0 78rpx; display: flex; align-items: center; justify-content: center; border: 7rpx solid rgba(255,255,255,.22); border-radius: 27rpx; background: rgba(255,255,255,.16); }
.hero-copy { min-width: 0; margin-left: 20rpx; display: flex; flex-direction: column; }
.hero-copy text:first-child { font-size: 28rpx; font-weight: 700; }
.hero-copy text:last-child { margin-top: 10rpx; color: #E0EDFF; font-size: 21rpx; }
.hero-glow { width: 210rpx; height: 210rpx; position: absolute; right: -86rpx; bottom: -130rpx; border: 2rpx solid rgba(255,255,255,.17); border-radius: 50%; }
.section-title { margin: 28rpx 4rpx 14rpx; display: flex; align-items: center; justify-content: space-between; }
.section-title > text:first-child { font-size: 28rpx; font-weight: 700; }
.section-title > text:last-child { color: #8F9BAB; font-size: 21rpx; }
.object-tabs { display: flex; gap: 12rpx; }
.object-tabs > view { height: 105rpx; min-width: 0; flex: 1; display: flex; align-items: center; justify-content: center; flex-direction: column; border: 2rpx solid #E5EAF0; border-radius: 21rpx; color: #65758A; background: #FFFFFF; font-size: 21rpx; }
.object-tabs > view > view { width: 45rpx; height: 45rpx; display: flex; align-items: center; justify-content: center; border-radius: 15rpx; background: #EDF3FA; }
.object-tabs > view > text { margin-top: 8rpx; }
.object-tabs > view.active { border-color: #8DBDFC; color: #3187F2; background: #EBF4FF; font-weight: 600; }
.object-tabs > view.active > view { background: #3D8DF4; }
.issue-grid { display: flex; flex-wrap: wrap; gap: 12rpx; }
.issue-grid > view { width: calc(50% - 6rpx); height: 64rpx; padding: 0 15rpx; display: flex; align-items: center; border: 2rpx solid #E7EBF1; border-radius: 18rpx; color: #5D6B7D; background: #FFFFFF; font-size: 22rpx; }
.radio { width: 28rpx; height: 28rpx; flex: 0 0 28rpx; display: flex; align-items: center; justify-content: center; border: 2rpx solid #C6D0DC; border-radius: 50%; }
.radio view { width: 13rpx; height: 13rpx; border-radius: 50%; }
.issue-grid > view > text { margin-left: 9rpx; }
.issue-grid > view.active { border-color: #8DBDFC; color: #3187F2; background: #EDF5FF; }
.issue-grid > view.active .radio { border-color: #398BF4; }
.issue-grid > view.active .radio view { background: #398BF4; }
.location-card, .contact-card { padding: 20rpx 21rpx; display: flex; align-items: center; border: 2rpx solid #EEF1F5; border-radius: 22rpx; background: #FFFFFF; }
.location-icon { width: 54rpx; height: 54rpx; flex: 0 0 54rpx; display: flex; align-items: center; justify-content: center; border-radius: 18rpx; background: #E9F3FF; }
.location-copy { min-width: 0; margin-left: 13rpx; display: flex; flex: 1; flex-direction: column; }
.location-copy text:first-child { font-size: 24rpx; font-weight: 600; }
.location-copy text:last-child { margin-top: 7rpx; overflow: hidden; color: #919DAB; font-size: 20rpx; text-overflow: ellipsis; white-space: nowrap; }
.description-card { padding: 17rpx 18rpx; border: 2rpx solid #E8EDF3; border-radius: 22rpx; background: #FFFFFF; }
.description-card textarea { width: 100%; height: 150rpx; color: #3D4A5C; font-size: 23rpx; line-height: 1.55; }
.textarea-placeholder { color: #ABB4BF; }
.quick-descriptions { display: flex; flex-wrap: wrap; gap: 9rpx; }
.quick-descriptions text { padding: 8rpx 12rpx; border-radius: 12rpx; color: #6682A4; background: #F0F5FB; font-size: 20rpx; }
.upload-row { display: flex; gap: 13rpx; }
.upload-item, .upload-example { width: 190rpx; height: 150rpx; display: flex; align-items: center; justify-content: center; flex-direction: column; border: 2rpx dashed #C9D4E1; border-radius: 20rpx; color: #8190A2; background: #FFFFFF; font-size: 20rpx; }
.upload-item text { margin-top: 9rpx; }
.upload-example { width: 245rpx; border-style: solid; background: #F7F9FC; }
.cabinet-photo { width: 57rpx; height: 65rpx; padding: 7rpx; display: flex; flex-wrap: wrap; gap: 5rpx; border: 4rpx solid #89A4C3; border-radius: 8rpx; }
.cabinet-photo view { width: calc(50% - 3rpx); height: calc(33.333% - 4rpx); border-radius: 2rpx; background: #89A4C3; }
.upload-example text { margin-top: 8rpx; color: #8D9AAC; }
.contact-card > view { display: flex; align-items: center; gap: 8rpx; color: #617186; font-size: 23rpx; }
.contact-card input { min-width: 0; margin-left: auto; flex: 1; color: #3D4A5C; font-size: 23rpx; text-align: right; }
.urgency-card { margin-top: 20rpx; padding: 19rpx 21rpx; display: flex; align-items: center; border: 2rpx solid #F2E7D9; border-radius: 22rpx; background: #FFF9F2; }
.urgency-icon { width: 48rpx; height: 48rpx; flex: 0 0 48rpx; display: flex; align-items: center; justify-content: center; border-radius: 16rpx; background: #FFF0DF; }
.urgency-copy { min-width: 0; margin-left: 13rpx; display: flex; flex: 1; flex-direction: column; }
.urgency-copy text:first-child { color: #6B513A; font-size: 23rpx; font-weight: 600; }
.urgency-copy text:last-child { margin-top: 6rpx; color: #A2876E; font-size: 20rpx; }
.switch { width: 76rpx; height: 42rpx; flex: 0 0 76rpx; padding: 5rpx; border-radius: 21rpx; background: #D6DCE3; }
.switch view { width: 32rpx; height: 32rpx; border-radius: 50%; background: #FFFFFF; transition: transform .2s ease; }
.switch.active { background: #398BF4; }
.switch.active view { transform: translateX(34rpx); }
.service-notice { margin-top: 20rpx; padding: 17rpx 19rpx; display: flex; align-items: center; gap: 9rpx; border-radius: 18rpx; color: #6F86A2; background: #EBF4FF; font-size: 20rpx; }
.content-spacer { height: calc(150rpx + env(safe-area-inset-bottom)); }
.bottom-bar { height: calc(122rpx + env(safe-area-inset-bottom)); padding: 15rpx 28rpx env(safe-area-inset-bottom); position: absolute; z-index: 5; left: 0; right: 0; bottom: 0; background: rgba(255,255,255,.98); box-shadow: 0 -6rpx 24rpx rgba(35,61,91,.08); }
.submit-button { width: 100%; height: 86rpx; display: flex; align-items: center; justify-content: center; border-radius: 24rpx; color: #FFFFFF; background: linear-gradient(135deg, #4A9DF8, #3185F0); box-shadow: 0 10rpx 22rpx rgba(49,133,240,.22); font-size: 25rpx; font-weight: 600; }
@media screen and (max-width: 360px) {
  .repair-page { padding-right: 22rpx; padding-left: 22rpx; }
  .object-tabs { gap: 8rpx; }
  .issue-grid { gap: 9rpx; }
  .issue-grid > view { width: calc(50% - 5rpx); padding: 0 10rpx; font-size: 20rpx; }
  .upload-item { width: 165rpx; }
  .upload-example { width: 220rpx; }
  .bottom-bar { padding-right: 22rpx; padding-left: 22rpx; }
}

/* visual polish */
.repair-page{padding-right:30rpx;padding-left:30rpx;background:linear-gradient(180deg,#FAFCFF,#F5F8FC 380rpx)}
.nav-button{width:62rpx;height:62rpx;border-radius:21rpx;box-shadow:0 8rpx 22rpx rgba(39,69,104,.07)}
.hero-card{min-height:164rpx;padding:27rpx;border-radius:29rpx;box-shadow:0 16rpx 36rpx rgba(49,132,239,.19)}
.object-tabs>view{height:82rpx;border-radius:23rpx}.issue-grid>view{height:72rpx;border-radius:20rpx}
.location-card,.contact-card{padding:23rpx;border-radius:25rpx;box-shadow:0 7rpx 22rpx rgba(39,65,96,.035)}
.description-card{padding:21rpx;border-radius:25rpx}.upload-item,.upload-example{height:158rpx;border-radius:23rpx}
.urgency-card{padding:22rpx 23rpx;border-radius:24rpx}.service-notice{padding:20rpx 22rpx;border-radius:21rpx}.submit-button{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.repair-page{padding-right:22rpx;padding-left:22rpx}.hero-card{min-height:156rpx}.object-tabs>view{height:76rpx}.issue-grid>view{height:68rpx}.upload-item,.upload-example{height:150rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;background:rgba(248,251,255,.97);box-shadow:0 8rpx 22rpx rgba(37,66,99,.08);backdrop-filter:blur(18rpx)}
.repair-page{padding-top:calc(var(--status-bar-height, 0px) + 100rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.repair-page{padding-right:22rpx;padding-left:22rpx}}



/* Flat reporting form: bordered inputs define structure without visual heaviness. */
.nav-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.hero-card{box-shadow:var(--gy-shadow-brand)}
.location-card,.contact-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.submit-button{box-shadow:var(--gy-shadow-brand)}



/* Adversarial form audit: restore internal breathing room and reliable touch targets. */
.object-tabs{gap:12rpx}
.object-tabs>view{height:auto;min-height:132rpx;padding:16rpx 10rpx 15rpx;border-radius:24rpx}
.object-tabs>view>view{width:48rpx;height:48rpx;flex:0 0 48rpx;border-radius:16rpx}
.object-tabs>view>text{min-height:30rpx;margin-top:10rpx;line-height:30rpx;text-align:center;white-space:nowrap}
.issue-grid{gap:12rpx}
.issue-grid>view{height:auto;min-height:88rpx;padding:15rpx 18rpx;border-radius:21rpx;font-size:22rpx;line-height:1.35}
.radio{width:30rpx;height:30rpx;flex-basis:30rpx}
.issue-grid>view>text{margin-left:11rpx}
.description-card{padding:23rpx;border-color:var(--gy-surface-border)}
.description-card textarea{height:160rpx;font-size:24rpx}
.quick-descriptions{gap:10rpx;margin-top:4rpx}
.quick-descriptions text{min-height:58rpx;padding:0 16rpx;display:flex;align-items:center;border-radius:15rpx;font-size:21rpx}
.location-card,.contact-card{min-height:108rpx}
.urgency-card{min-height:122rpx}
.service-notice{min-height:74rpx;line-height:1.45}
@media screen and (max-width:360px){
  .object-tabs{gap:8rpx}
  .object-tabs>view{min-height:122rpx;padding:14rpx 6rpx 13rpx}
  .object-tabs>view>view{width:44rpx;height:44rpx;flex-basis:44rpx}
  .object-tabs>view>text{margin-top:8rpx;font-size:20rpx}
  .issue-grid{gap:9rpx}
  .issue-grid>view{width:calc(50% - 5rpx);min-height:84rpx;padding:14rpx 14rpx;font-size:20rpx}
  .quick-descriptions text{min-height:54rpx;padding-right:14rpx;padding-left:14rpx;font-size:20rpx}
}

</style>
