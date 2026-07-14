<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
          <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#283344" size="21"></uv-icon></view>
          <text class="nav-title">我的电池</text>
          <view class="nav-button" @tap="showBatteryGuide"><uv-icon name="question-circle" color="#283344" size="20"></uv-icon></view>
        </view>

    <scroll-view class="page-scroll" scroll-y :show-scrollbar="false">
      <view class="battery-page">
        

        <view class="battery-hero">
          <view class="hero-glow glow-one"></view><view class="hero-glow glow-two"></view>
          <view class="active-label"><view></view><text>当前使用中</text></view>
          <view class="battery-visual">
            <view class="battery-cap"></view><view class="battery-screen"><view></view><text>ONLINE</text></view>
            <view class="battery-cells"><view></view><view></view><view></view><view></view></view><view class="battery-bolt"></view>
          </view>
          <view class="hero-copy"><text>72V 30Ah</text><text>智能锂电池</text><text>编号 GY72V30-10682</text></view>
          <view class="power-card"><view><text>86</text><text>%</text></view><text>剩余电量</text><view class="power-bar"><view></view></view></view>
          <view class="hero-stats"><view><text>49 km</text><text>预计续航</text></view><view></view><view><text>99%</text><text>健康度</text></view><view></view><view><text>28°C</text><text>当前温度</text></view></view>
        </view>

        <view class="safety-status">
          <view class="shield"><uv-icon name="lock-fill" color="#3389F7" size="21"></uv-icon></view>
          <view><text>电池状态良好</text><text>最后检测于今天 14:28，未发现异常</text></view>
          <view class="normal"><view></view><text>安全</text></view>
        </view>

        <view class="section-heading"><text>实时数据</text><text @tap="showDataGuide">每 5 分钟更新</text></view>
        <view class="data-grid">
          <view v-for="item in metrics" :key="item.title" class="data-item">
            <view class="data-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="20"></uv-icon></view>
            <view><text>{{ item.value }}</text><text>{{ item.title }}</text></view>
          </view>
        </view>

        <view class="section-heading"><text>电池服务</text><text>全生命周期保障</text></view>
        <view class="service-card">
          <view v-for="(item,index) in services" :key="item.title" class="service-row" @tap="handleService(item)">
            <view class="service-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="20"></uv-icon></view>
            <view class="service-copy"><text>{{ item.title }}</text><text>{{ item.desc }}</text></view>
            <view v-if="item.badge" class="service-badge">{{ item.badge }}</view>
            <uv-icon name="arrow-right" color="#A7B1BE" size="12"></uv-icon>
            <view v-if="index<services.length-1" class="service-line"></view>
          </view>
        </view>

        <view class="stored-card" @tap="openStorage">
          <view class="stored-icon"><uv-icon name="folder" color="#7569EC" size="22"></uv-icon></view>
          <view><text>另有 1 块电池暂存中</text><text>科技园换电站 · 剩余 2 天</text></view>
          <view class="stored-action"><text>查看</text><uv-icon name="arrow-right" color="#7569EC" size="11"></uv-icon></view>
        </view>
        <view class="content-spacer"></view>
      </view>
    </scroll-view>

    <view class="bottom-actions">
      <view class="secondary" @tap="openRepair"><uv-icon name="setting" color="#58677A" size="18"></uv-icon><text>故障报修</text></view>
      <view class="primary" @tap="goScan"><uv-icon name="scan" color="#FFFFFF" size="19"></uv-icon><text>去换电</text></view>
    </view>
  </view>
</template>

<script>
export default {
  data(){return{metrics:[
    {title:'电池电压',value:'73.6 V',icon:'integral',color:'#398CF4',theme:'blue'},
    {title:'循环次数',value:'186 次',icon:'reload',color:'#7569EC',theme:'purple'},
    {title:'今日里程',value:'18.6 km',icon:'car-fill',color:'#26A987',theme:'green'},
    {title:'累计减碳',value:'32.8 kg',icon:'heart-fill',color:'#EE9848',theme:'orange'}
  ],services:[
    {title:'电池轨迹',desc:'查看最近 7 天使用记录',icon:'map-fill',color:'#378FF7',theme:'blue'},
    {title:'健康报告',desc:'查看电芯与循环健康详情',icon:'file-text',color:'#7569EC',theme:'purple',badge:'优秀'},
    {title:'安全保障',desc:'设备异常主动提醒',icon:'lock-fill',color:'#27A987',theme:'green'}
  ]}},
  methods:{
    goBack(){uni.navigateBack({fail:()=>uni.reLaunch({url:'/pages/index/index'})})},
    goScan(){uni.reLaunch({url:'/pages/index/index?tab=2'})},
    openRepair(){uni.navigateTo({url:'/pages/repair/create'})},
    openStorage(){uni.navigateTo({url:'/pages/storage/index'})},
    showBatteryGuide(){uni.showModal({title:'电池说明',content:'当前电池为平台智能锂电池，支持实时安全监测。请勿私自拆卸、浸水或在高温环境下长时间放置。',showCancel:false})},
    showDataGuide(){uni.showModal({title:'实时数据说明',content:'电池状态会按约 5 分钟同步一次；网络异常时将展示最近一次有效数据。',showCancel:false})},
    handleService(item){
      const messages={
        '电池轨迹':'最近 7 天使用正常：科技园换电站、深大地铁站换电点、高新园北区站。',
        '健康报告':'电芯一致性良好，健康度 99%，循环状态正常。',
        '安全保障':'已开启过温、过流和异常震动提醒；发现风险时会优先推送通知。'
      }
      uni.showModal({title:item.title,content:messages[item.title],showCancel:false})
    }
  }
}
</script>

<style scoped>
.page-shell,.page-scroll{width:100%;height:100%;background:#F5F8FC}.page-shell{position:relative;overflow:hidden}.battery-page{min-height:100%;padding:0 28rpx;color:#293445;background:#F5F8FC;box-sizing:border-box}.nav-bar{height:calc(var(--status-bar-height,0px) + 96rpx);padding-top:var(--status-bar-height,0px);display:flex;align-items:center;justify-content:space-between}.nav-button{width:58rpx;height:58rpx;display:flex;align-items:center;justify-content:center;border:1rpx solid #E8EDF3;border-radius:50%;background:#fff}.nav-title{font-size:29rpx;font-weight:700}.battery-hero{height:410rpx;position:relative;overflow:hidden;border-radius:28rpx;color:#fff;background:linear-gradient(128deg,#2D80EE 0%,#4B9EFA 100%);box-shadow:0 15rpx 32rpx rgba(47,132,237,.19)}.hero-glow{position:absolute;border:2rpx solid rgba(255,255,255,.14);border-radius:50%}.glow-one{width:300rpx;height:300rpx;right:-160rpx;top:-130rpx}.glow-two{width:220rpx;height:220rpx;left:-130rpx;bottom:-120rpx}.active-label{height:38rpx;padding:0 12rpx;position:absolute;left:22rpx;top:20rpx;display:flex;align-items:center;gap:7rpx;border-radius:19rpx;color:#E8F5FF;background:rgba(255,255,255,.14);font-size:20rpx}.active-label view{width:8rpx;height:8rpx;border-radius:50%;background:#48E0AE;box-shadow:0 0 10rpx rgba(72,224,174,.8)}.battery-visual{width:112rpx;height:172rpx;position:absolute;left:43rpx;top:79rpx;padding:17rpx 13rpx 11rpx;border:5rpx solid rgba(255,255,255,.88);border-radius:21rpx;background:linear-gradient(145deg,#DCEEFF,#FFFFFF);box-shadow:0 16rpx 30rpx rgba(20,86,168,.25);transform:rotate(-3deg)}.battery-cap{width:43rpx;height:10rpx;position:absolute;left:29rpx;top:-13rpx;border-radius:6rpx 6rpx 0 0;background:#EAF4FF}.battery-screen{height:36rpx;display:flex;align-items:center;justify-content:center;gap:5rpx;border-radius:8rpx;color:#3D78BB;background:#CBE3FC;font-size:11rpx;font-weight:700}.battery-screen view{width:7rpx;height:7rpx;border-radius:50%;background:#26C28F}.battery-cells{margin-top:12rpx;display:flex;gap:4rpx}.battery-cells view{height:45rpx;flex:1;border-radius:4rpx;background:linear-gradient(180deg,#51A6FA,#3187F0)}.battery-bolt{width:14rpx;height:27rpx;margin:10rpx auto 0;border-radius:3rpx;background:#5AA4F5;transform:skewX(-18deg)}.hero-copy{position:absolute;left:184rpx;top:80rpx;display:flex;flex-direction:column}.hero-copy text:first-child{font-size:34rpx;font-weight:700}.hero-copy text:nth-child(2){margin-top:4rpx;color:#E0EEFF;font-size:24rpx}.hero-copy text:last-child{margin-top:12rpx;color:#CDE3FF;font-size:20rpx}.power-card{width:215rpx;height:105rpx;position:absolute;right:22rpx;top:173rpx;padding:12rpx 15rpx;border:1rpx solid rgba(255,255,255,.18);border-radius:20rpx;background:rgba(255,255,255,.1)}.power-card>view:first-child{display:flex;align-items:baseline}.power-card>view:first-child text:first-child{font-size:32rpx;font-weight:700}.power-card>view:first-child text:last-child{font-size:20rpx}.power-card>text{margin-left:8rpx;color:#DCEBFC;font-size:15rpx}.power-bar{height:9rpx;margin-top:9rpx;overflow:hidden;border-radius:5rpx;background:rgba(255,255,255,.18)}.power-bar view{width:86%;height:100%;border-radius:5rpx;background:#fff}.hero-stats{height:92rpx;position:absolute;left:20rpx;right:20rpx;bottom:18rpx;display:flex;align-items:center;border:1rpx solid rgba(255,255,255,.13);border-radius:20rpx;background:rgba(255,255,255,.09)}.hero-stats>view:nth-child(odd){display:flex;flex:1;align-items:center;flex-direction:column}.hero-stats>view:nth-child(even){width:1rpx;height:41rpx;background:rgba(255,255,255,.17)}.hero-stats text:first-child{font-size:24rpx;font-weight:700}.hero-stats text:last-child{margin-top:5rpx;color:#D6E8FC;font-size:15rpx}.safety-status{min-height:100rpx;margin-top:20rpx;padding:18rpx 19rpx;display:flex;align-items:center;border-radius:24rpx;background:#fff;box-shadow:0 8rpx 26rpx rgba(42,68,99,.05)}.shield{width:54rpx;height:54rpx;flex:0 0 54rpx;display:flex;align-items:center;justify-content:center;border-radius:18rpx;background:#EAF4FF}.safety-status>view:nth-child(2){min-width:0;margin-left:13rpx;display:flex;flex:1;flex-direction:column}.safety-status>view:nth-child(2) text:first-child{font-size:24rpx;font-weight:600}.safety-status>view:nth-child(2) text:last-child{margin-top:6rpx;overflow:hidden;color:#8C98A7;font-size:20rpx;text-overflow:ellipsis;white-space:nowrap}.normal{height:37rpx;padding:0 11rpx;display:flex;align-items:center;gap:6rpx;border-radius:18rpx;color:#278D6F;background:#EAF8F3;font-size:20rpx}.normal view{width:7rpx;height:7rpx;border-radius:50%;background:#2DBB8C}.section-heading{margin:28rpx 2rpx 0;display:flex;align-items:center;justify-content:space-between}.section-heading text:first-child{font-size:28rpx;font-weight:700}.section-heading text:last-child{color:#94A0AE;font-size:20rpx}.data-grid{margin-top:18rpx;display:flex;flex-wrap:wrap;gap:14rpx}.data-item{width:calc(50% - 7rpx);min-height:100rpx;padding:18rpx;display:flex;align-items:center;border-radius:22rpx;background:#fff;box-shadow:0 7rpx 22rpx rgba(42,68,99,.045)}.data-icon{width:48rpx;height:48rpx;flex:0 0 48rpx;display:flex;align-items:center;justify-content:center;border-radius:16rpx}.data-icon.blue{background:#EAF4FF}.data-icon.purple{background:#F0EDFF}.data-icon.green{background:#E8F8F2}.data-icon.orange{background:#FFF1E5}.data-item>view:last-child{min-width:0;margin-left:12rpx;display:flex;flex-direction:column}.data-item>view:last-child text:first-child{font-size:24rpx;font-weight:600}.data-item>view:last-child text:last-child{margin-top:5rpx;color:#929EAD;font-size:20rpx}.service-card{margin-top:18rpx;padding:0 20rpx;border-radius:24rpx;background:#fff;box-shadow:0 8rpx 25rpx rgba(39,65,96,.05)}.service-row{min-height:100rpx;position:relative;display:flex;align-items:center}.service-icon{width:50rpx;height:50rpx;flex:0 0 50rpx;display:flex;align-items:center;justify-content:center;border-radius:17rpx}.service-icon.blue{background:#EAF4FF}.service-icon.purple{background:#F0EDFF}.service-icon.green{background:#E8F8F2}.service-copy{min-width:0;margin-left:13rpx;display:flex;flex:1;flex-direction:column}.service-copy text:first-child{font-size:24rpx;font-weight:600}.service-copy text:last-child{margin-top:5rpx;color:#929DAB;font-size:20rpx}.service-badge{height:36rpx;margin-right:10rpx;padding:0 11rpx;display:flex;align-items:center;border-radius:18rpx;color:#2A8F70;background:#E9F8F2;font-size:20rpx}.service-line{height:2rpx;position:absolute;left:63rpx;right:0;bottom:0;background:#F0F2F5}.stored-card{min-height:92rpx;margin-top:20rpx;padding:18rpx 19rpx;display:flex;align-items:center;border-radius:23rpx;background:linear-gradient(135deg,#F6F3FF,#F0EDFF)}.stored-icon{width:50rpx;height:50rpx;flex:0 0 50rpx;display:flex;align-items:center;justify-content:center;border-radius:17rpx;background:#E7E2FF}.stored-card>view:nth-child(2){min-width:0;margin-left:13rpx;display:flex;flex:1;flex-direction:column}.stored-card>view:nth-child(2) text:first-child{font-size:23rpx;font-weight:600}.stored-card>view:nth-child(2) text:last-child{margin-top:5rpx;color:#8D85B7;font-size:20rpx}.stored-action{display:flex;align-items:center;color:#7569EC;font-size:20rpx}.content-spacer{height:calc(158rpx + env(safe-area-inset-bottom))}.bottom-actions{height:calc(128rpx + env(safe-area-inset-bottom));padding:16rpx 28rpx env(safe-area-inset-bottom);position:absolute;z-index:8;left:0;right:0;bottom:0;display:flex;gap:16rpx;background:rgba(255,255,255,.98);box-shadow:0 -6rpx 24rpx rgba(35,61,91,.08)}.bottom-actions>view{height:86rpx;display:flex;align-items:center;justify-content:center;gap:7rpx;border-radius:24rpx;font-size:24rpx;font-weight:600}.secondary{width:40%;border:2rpx solid #DCE4EE;color:#58677A;background:#fff}.primary{flex:1;color:#fff;background:linear-gradient(135deg,#4A9DF8,#3185F0);box-shadow:0 10rpx 22rpx rgba(49,133,240,.22)}
@media screen and (max-width:360px){.battery-page{padding-right:22rpx;padding-left:22rpx}.hero-copy{left:170rpx}.power-card{width:195rpx}.bottom-actions{padding-right:22rpx;padding-left:22rpx}.data-item{padding:15rpx}.data-item>view:last-child{margin-left:9rpx}}

/* visual polish */
.battery-page{padding-right:30rpx;padding-left:30rpx;background:linear-gradient(180deg,#FAFCFF,#F5F8FC 380rpx)}.nav-button{width:62rpx;height:62rpx;box-shadow:0 8rpx 22rpx rgba(39,69,104,.07)}
.battery-hero{height:424rpx;border-radius:31rpx;box-shadow:0 18rpx 42rpx rgba(47,132,237,.22)}
.safety-status{min-height:108rpx;padding:21rpx;border-radius:27rpx;box-shadow:var(--gy-card-shadow)}
.data-grid{gap:17rpx}.data-item{width:calc(50% - 9rpx);min-height:108rpx;padding:20rpx;border-radius:25rpx;box-shadow:var(--gy-card-shadow)}
.service-card{padding:3rpx 22rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}.service-row{min-height:108rpx}
.stored-card{min-height:100rpx;padding:21rpx;border-radius:26rpx}.bottom-actions>view{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.battery-page{padding-right:22rpx;padding-left:22rpx}.battery-hero{height:416rpx}.data-grid{gap:14rpx}.data-item{width:calc(50% - 7rpx);min-height:102rpx;padding:17rpx}.service-row{min-height:102rpx}.bottom-actions>view{height:86rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;background:rgba(248,251,255,.97);box-shadow:0 8rpx 22rpx rgba(37,66,99,.08);backdrop-filter:blur(18rpx)}
.battery-page{padding-top:calc(var(--status-bar-height, 0px) + 96rpx)}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.battery-page{padding-right:22rpx;padding-left:22rpx}}



/* Flat battery dashboard: status cards now use subtle outlines rather than stacked elevation. */
.battery-hero{box-shadow:var(--gy-shadow-brand)}
.battery-visual{box-shadow:0 6rpx 14rpx rgba(20,86,168,.13)}
.safety-status,.data-item,.service-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.bottom-actions{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.primary{box-shadow:var(--gy-shadow-brand)}
.nav-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}

</style>
