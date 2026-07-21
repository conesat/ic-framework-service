<template>
  <view class="settings-page">
    <view class="fixed-page-header page-header">
      <view class="header-back" @tap="goBack" aria-label="返回"><uv-icon name="arrow-left" color="#2B3748" size="22"></uv-icon></view>
      <view class="header-copy"><text class="header-title">设置</text><text class="header-subtitle">管理通知、定位和账号使用偏好</text></view>
    </view>
    <scroll-view class="settings-scroll" scroll-y :show-scrollbar="false">
      <view class="settings-content">
        <view class="account-brief"><view class="account-avatar"><uv-icon name="account-fill" color="#3389F7" size="28"></uv-icon></view><view class="account-copy"><text class="account-phone">138 **** 5678</text><text class="account-state">账号已实名认证，安全保护已开启</text></view><uv-icon name="checkmark-circle-fill" color="#39B78B" size="20"></uv-icon></view>
        <view class="section-title">服务偏好</view>
        <view class="settings-card">
          <view v-for="(item,index) in preferences" :key="item.key" class="setting-row">
            <view class="setting-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="20"></uv-icon></view>
            <view class="setting-copy"><text class="setting-label">{{ item.label }}</text><text class="setting-desc">{{ item.desc }}</text></view>
            <view class="setting-toggle" :class="{ on: item.enabled }" @tap.stop="togglePreference(item)"><view class="toggle-knob"></view></view>
            <view v-if="index < preferences.length - 1" class="row-divider"></view>
          </view>
        </view>
        <view class="section-title">通用</view>
        <view class="settings-card">
          <view v-for="(item,index) in generalItems" :key="item.label" class="setting-row nav-row" @tap="handleGeneral(item)">
            <view class="setting-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="20"></uv-icon></view>
            <view class="setting-copy"><text class="setting-label">{{ item.label }}</text><text class="setting-desc">{{ item.detail }}</text></view>
            <uv-icon name="arrow-right" color="#A9B4C2" size="14"></uv-icon>
            <view v-if="index < generalItems.length - 1" class="row-divider"></view>
          </view>
        </view>
        <view class="privacy-tip"><uv-icon name="lock-fill" color="#5C9CED" size="18"></uv-icon><text>我们仅在提供换电服务所需范围内使用定位和通知权限。</text></view>
        <view class="bottom-spacer"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
const SETTINGS_KEY = 'energy_settings'
export default {
  data() { return { preferences: [{ key: 'order', label: '订单进度通知', desc: '换电、暂存、维修状态及时提醒', enabled: true, icon: 'bell-fill', color: '#3389F7', theme: 'blue' }, { key: 'station', label: '附近站点提醒', desc: '靠近常去站点时推荐可用电池', enabled: true, icon: 'map-fill', color: '#7269EB', theme: 'purple' }, { key: 'location', label: '定位服务', desc: '用于展示距离最近的换电站', enabled: true, icon: 'map', color: '#39AE8A', theme: 'green' }], generalItems: [{ label: '清理缓存', detail: '页面图片及临时数据', icon: 'trash-fill', color: '#E79650', theme: 'orange', action: 'clear' }, { label: '帮助与反馈', detail: '常见问题、意见反馈', icon: 'question-circle-fill', color: '#3389F7', theme: 'blue', action: 'help' }, { label: '隐私政策', detail: '个人信息处理说明', icon: 'file-text-fill', color: '#667B96', theme: 'gray', action: 'privacy' }, { label: '关于电能行', detail: '当前版本 1.2.0', icon: 'info-circle-fill', color: '#667B96', theme: 'gray', action: 'about' }] } },
  onLoad() { const saved = uni.getStorageSync(SETTINGS_KEY); if (saved && typeof saved === 'object') this.preferences = this.preferences.map(item => ({ ...item, enabled: saved[item.key] !== undefined ? saved[item.key] : item.enabled })) },
  methods: {
    goBack() { uni.navigateBack({ delta: 1, fail: () => uni.reLaunch({ url: '/pages/index/index?tab=4' }) }) },
    togglePreference(item) { item.enabled = !item.enabled; uni.setStorageSync(SETTINGS_KEY, this.preferences.reduce((state, current) => ({ ...state, [current.key]: current.enabled }), {})); uni.showToast({ title: item.enabled ? '已开启' : '已关闭', icon: 'none' }) },
    handleGeneral(item) { if (item.action === 'help') return uni.navigateTo({ url: '/pages/help/index' }); if (item.action === 'clear') return uni.showModal({ title: '清理缓存', content: '将清除本机的页面图片与临时数据，不影响订单和账户信息。', confirmText: '立即清理', success: ({ confirm }) => { if (confirm) uni.showToast({ title: '缓存已清理', icon: 'success' }) } }); if (item.action === 'privacy') return uni.showModal({ title: '隐私政策', content: '我们会在提供换电服务、订单履约与安全保障所需范围内处理你的信息。', showCancel: false }); uni.showModal({ title: '电能行', content: '电能行智能换电\n版本 1.2.0\n让每一次出发都有满格能量。', showCancel: false }) }
  }
}
</script>

<style scoped>
.settings-page { width: 100%; height: 100%; overflow: hidden; background: #F5F8FC; color: var(--gy-text-primary); }
.page-header { height: calc(var(--status-bar-height, 0px) + 128rpx); padding: calc(var(--status-bar-height, 0px) + 24rpx) 28rpx 18rpx; position: fixed; z-index: 20; top: 0; right: 0; left: 0; display: flex; align-items: center; box-sizing: border-box; background: rgba(255, 255, 255, .97); backdrop-filter: blur(18rpx); }
.header-back { width: 64rpx; height: 64rpx; flex: 0 0 64rpx; display: flex; align-items: center; justify-content: center; border: 1rpx solid var(--gy-surface-border); border-radius: 20rpx; background: #FFFFFF; }
.header-copy { min-width: 0; flex: 1; margin-left: 18rpx; display: flex; flex-direction: column; }
.header-title { color: #273448; font-size: 32rpx; line-height: 40rpx; font-weight: 700; }
.header-subtitle { margin-top: 2rpx; overflow: hidden; color: var(--gy-text-muted); font-size: 21rpx; line-height: 30rpx; text-overflow: ellipsis; white-space: nowrap; }
.settings-scroll { width: 100%; height: 100%; padding-top: calc(var(--status-bar-height, 0px) + 128rpx); box-sizing: border-box; }
.settings-content { padding: 24rpx 28rpx 0; box-sizing: border-box; }
.account-brief { min-height: 112rpx; padding: 18rpx 20rpx; display: flex; align-items: center; box-sizing: border-box; border: 1rpx solid #DCEBFD; border-radius: 25rpx; background: linear-gradient(110deg, #F8FBFF, #F0F7FF); }
.account-avatar { width: 64rpx; height: 64rpx; flex: 0 0 64rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #E1F0FF; }
.account-copy { min-width: 0; flex: 1; margin-left: 16rpx; display: flex; flex-direction: column; }
.account-phone { display: block; color: #293649; font-size: 27rpx; line-height: 38rpx; font-weight: 650; }
.account-state { display: block; margin-top: 6rpx; overflow: hidden; color: #7B899B; font-size: 21rpx; line-height: 30rpx; text-overflow: ellipsis; white-space: nowrap; }
.section-title { margin: 30rpx 4rpx 14rpx; color: #536174; font-size: 25rpx; line-height: 35rpx; font-weight: 650; }
.settings-card { overflow: hidden; border: 1rpx solid var(--gy-surface-border); border-radius: 25rpx; background: #FFFFFF; }
.setting-row { min-height: 112rpx; padding: 18rpx 20rpx; position: relative; display: flex; align-items: center; box-sizing: border-box; }
.setting-icon { width: 58rpx; height: 58rpx; flex: 0 0 58rpx; display: flex; align-items: center; justify-content: center; border-radius: 19rpx; }
.setting-icon.blue { background: #E9F3FF; }.setting-icon.purple { background: #F0EEFF; }.setting-icon.green { background: #E8F8F2; }.setting-icon.orange { background: #FFF2E6; }.setting-icon.gray { background: #F0F3F7; }
.setting-copy { min-width: 0; flex: 1; margin-left: 16rpx; margin-right: 14rpx; display: flex; flex-direction: column; }
.setting-label { display: block; color: #2C394C; font-size: 26rpx; line-height: 38rpx; font-weight: 600; }
.setting-desc { display: block; margin-top: 6rpx; overflow: hidden; color: #8491A2; font-size: 21rpx; line-height: 30rpx; text-overflow: ellipsis; white-space: nowrap; }
.setting-toggle { width: 76rpx; height: 44rpx; flex: 0 0 76rpx; padding: 4rpx; display: flex; align-items: center; border-radius: 24rpx; box-sizing: border-box; background: #CFD8E3; transition: background .2s ease; }
.setting-toggle.on { justify-content: flex-end; background: #3389F7; }
.toggle-knob { width: 36rpx; height: 36rpx; border-radius: 50%; background: #FFFFFF; box-shadow: 0 1rpx 3rpx rgba(24, 52, 83, .18); }
.row-divider { height: 1rpx; position: absolute; right: 20rpx; bottom: 0; left: 94rpx; background: #EEF2F6; }
.nav-row { min-height: 114rpx; }.nav-row:active { background: #F8FBFF; }
.privacy-tip { margin-top: 22rpx; padding: 0 8rpx; display: flex; align-items: flex-start; gap: 9rpx; color: #8B98A8; font-size: 21rpx; line-height: 33rpx; }.privacy-tip text { flex: 1; }
.bottom-spacer { height: calc(54rpx + env(safe-area-inset-bottom)); }
@media screen and (max-width: 360px) { .page-header { padding-right: 22rpx; padding-left: 22rpx; }.settings-content { padding-right: 22rpx; padding-left: 22rpx; }.header-copy { margin-left: 14rpx; }.header-subtitle { font-size: 20rpx; }.setting-row { padding-right: 17rpx; padding-left: 17rpx; }.setting-copy { margin-left: 14rpx; margin-right: 10rpx; }.setting-desc { font-size: 20rpx; }.row-divider { right: 17rpx; left: 89rpx; } }


/* Adversarial UI audit: small helper copy remains readable without changing the primary hierarchy. */
.header-back{width:80rpx;height:80rpx;flex-basis:80rpx;border-color:var(--gy-surface-border);box-shadow:none}
.header-subtitle,.account-state,.setting-desc,.privacy-tip{font-size:22rpx;line-height:32rpx}
.setting-row{min-height:116rpx}
.setting-toggle{min-width:88rpx;min-height:52rpx;height:52rpx;border-radius:28rpx}
.toggle-knob{width:44rpx;height:44rpx}



.header-back{width:92rpx;height:92rpx;flex-basis:92rpx}

</style>
