<template>
  <view class="page-shell">
    <view class="nav-bar fixed-page-header">
            <view class="nav-button" @tap="goBack"><uv-icon name="arrow-left" color="#FFFFFF" size="21"></uv-icon></view>
            <text class="nav-title">会员中心</text>
            <view class="nav-button" @tap="showMemberGuide"><uv-icon name="question-circle" color="#FFFFFF" size="20"></uv-icon></view>
          </view>

    <scroll-view class="page-scroll" scroll-y :show-scrollbar="false">
      <view class="member-page">
        <view class="member-hero">
          <view class="hero-glow glow-one"></view><view class="hero-glow glow-two"></view>
          
          <view class="member-summary">
            <view class="member-emblem"><uv-icon name="star-fill" color="#FFF0B2" size="30"></uv-icon></view>
            <view class="summary-copy"><view><text>黄金会员</text><view>已开通</view></view><text>有效期至 2027-06-15</text></view>
            <view class="renew-entry" @tap="selectPlan(1)"><text>续费</text><uv-icon name="arrow-right" color="#FFF1C6" size="11"></uv-icon></view>
          </view>
          <view class="member-stats">
            <view><text>16</text><text>剩余换电次数</text></view><view class="stat-line"></view>
            <view><text>¥ 238</text><text>累计节省</text></view><view class="stat-line"></view>
            <view><text>96%</text><text>安全骑行率</text></view>
          </view>
        </view>

        <view class="member-body">
          <view class="section-heading"><text>选择套餐</text><text>会员价低至 ¥1.9/次</text></view>
          <scroll-view class="plan-scroll" scroll-x :show-scrollbar="false">
            <view class="plan-row">
              <view v-for="(plan,index) in plans" :key="plan.name" class="plan-card" :class="{active:selectedPlan===index}" @tap="selectPlan(index)">
                <view v-if="plan.tag" class="plan-tag">{{ plan.tag }}</view>
                <text class="plan-name">{{ plan.name }}</text>
                <view class="plan-price"><text>¥</text><text>{{ plan.price }}</text><text>/{{ plan.unit }}</text></view>
                <text class="plan-old">{{ plan.oldPrice }}</text>
                <view class="plan-line"></view>
                <text class="plan-desc">{{ plan.desc }}</text>
                <view class="plan-check"><uv-icon v-if="selectedPlan===index" name="checkmark" color="#FFFFFF" size="12"></uv-icon></view>
              </view>
            </view>
          </scroll-view>

          <view class="section-heading benefit-heading"><text>会员专属权益</text><text>开通即享</text></view>
          <view class="benefit-grid">
            <view v-for="item in benefits" :key="item.title" class="benefit-item">
              <view class="benefit-icon" :class="item.theme"><uv-icon :name="item.icon" :color="item.color" size="23"></uv-icon></view>
              <view><text>{{ item.title }}</text><text>{{ item.desc }}</text></view>
            </view>
          </view>

          <view class="saving-card">
            <view class="saving-top"><view class="saving-icon"><uv-icon name="integral-fill" color="#F0A13B" size="22"></uv-icon></view><view><text>预计每月可省 ¥86</text><text>按每月换电 20 次测算</text></view><text>省心又省钱</text></view>
            <view class="saving-bar"><view></view></view>
            <view class="saving-label"><text>普通用户 ¥146</text><text>会员 ¥60</text></view>
          </view>

          <view class="rules-card" @tap="showMemberRules">
            <view><uv-icon name="file-text" color="#6196D7" size="19"></uv-icon><text>会员服务说明</text></view>
            <view><text>查看使用规则与退款说明</text><uv-icon name="arrow-right" color="#A8B3C0" size="11"></uv-icon></view>
          </view>
          <view class="agreement" @tap="agreed=!agreed"><view :class="{active:agreed}"><uv-icon v-if="agreed" name="checkmark" color="#FFFFFF" size="11"></uv-icon></view><text>我已阅读并同意《会员服务协议》</text></view>
          <view class="content-spacer"></view>
        </view>
      </view>
    </scroll-view>

    <view class="bottom-bar">
      <view class="price-copy"><text>应付</text><view><text>¥</text><text>{{ currentPlan.price }}</text></view><text>{{ currentPlan.name }}</text></view>
      <view class="submit-button" :class="{disabled:!agreed}" @tap="submit"><text>立即开通</text><uv-icon name="arrow-right" color="#FFFFFF" size="14"></uv-icon></view>
    </view>
  </view>
</template>

<script>
export default {
  data(){return{selectedPlan:1,agreed:true,activated:false,plans:[
    {name:'轻享月卡',price:'39',unit:'月',oldPrice:'原价 ¥58',desc:'含 12 次换电',tag:''},
    {name:'通勤月卡',price:'69',unit:'月',oldPrice:'原价 ¥99',desc:'含 30 次换电',tag:'推荐'},
    {name:'骑手畅换卡',price:'159',unit:'月',oldPrice:'原价 ¥229',desc:'不限次畅换',tag:'最划算'}
  ],benefits:[
    {title:'换电优惠',desc:'会员专享低价',icon:'coupon-fill',color:'#378FF7',theme:'blue'},
    {title:'优先换电',desc:'高峰快速通道',icon:'clock-fill',color:'#7B6BEF',theme:'purple'},
    {title:'免费暂存',desc:'每月赠送 3 天',icon:'folder',color:'#26AA87',theme:'green'},
    {title:'安全保障',desc:'最高万元保障',icon:'lock-fill',color:'#F09A48',theme:'orange'},
    {title:'积分加速',desc:'积分双倍累积',icon:'integral-fill',color:'#E9A332',theme:'yellow'},
    {title:'专属客服',desc:'7×24 小时服务',icon:'kefu-ermai',color:'#3A95F2',theme:'cyan'}
  ]}},
  computed:{currentPlan(){return this.plans[this.selectedPlan]}},
  methods:{
    goBack(){uni.navigateBack({fail:()=>uni.reLaunch({url:'/pages/index/index'})})},
    selectPlan(index){this.selectedPlan=index},
    showMemberGuide(){uni.showModal({title:'会员说明',content:'开通会员可享换电优惠、优先换电、免费暂存及安全保障等专属权益。',showCancel:false})},
    showMemberRules(){uni.showModal({title:'会员服务说明',content:'会员权益自开通起生效；未使用的换电次数以套餐规则为准，退款请联系客服处理。',showCancel:false})},
    submit(){
      if(!this.agreed)return uni.showToast({title:'请先同意会员服务协议',icon:'none'})
      if(this.activated)return uni.showToast({title:'当前套餐已开通',icon:'none'})
      uni.showModal({title:'确认开通',content:`确认以 ¥${this.currentPlan.price} 开通${this.currentPlan.name}？`,confirmText:'确认开通',success:({confirm})=>{if(!confirm)return;this.activated=true;uni.showToast({title:'会员已开通',icon:'success'})}})
    }
  }
}
</script>

<style scoped>
.page-shell,.page-scroll{width:100%;height:100%;background:#F5F8FC}.page-shell{position:relative;overflow:hidden}.member-page{min-height:100%;color:#293445;background:#F5F8FC}.member-hero{height:440rpx;position:relative;overflow:hidden;padding:0 28rpx;color:#fff;background:linear-gradient(132deg,#25364F 0%,#31496C 52%,#3A5D8B 100%)}.hero-glow{position:absolute;border-radius:50%;background:rgba(255,227,163,.08)}.glow-one{width:330rpx;height:330rpx;right:-130rpx;top:-170rpx}.glow-two{width:250rpx;height:250rpx;left:-160rpx;bottom:-130rpx}.nav-bar{height:calc(var(--status-bar-height,0px) + 96rpx);padding-top:var(--status-bar-height,0px);position:relative;z-index:1;display:flex;align-items:center;justify-content:space-between}.nav-button{width:58rpx;height:58rpx;display:flex;align-items:center;justify-content:center;border:1rpx solid rgba(255,255,255,.16);border-radius:50%;background:rgba(255,255,255,.08)}.nav-title{font-size:29rpx;font-weight:700}.member-summary{height:134rpx;position:relative;z-index:1;display:flex;align-items:center}.member-emblem{width:86rpx;height:86rpx;flex:0 0 86rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid rgba(255,231,174,.35);border-radius:28rpx;background:linear-gradient(145deg,rgba(255,233,178,.22),rgba(255,216,129,.08));box-shadow:0 10rpx 25rpx rgba(17,29,46,.2)}.summary-copy{min-width:0;margin-left:20rpx;display:flex;flex:1;flex-direction:column}.summary-copy>view{display:flex;align-items:center;gap:10rpx}.summary-copy>view>text{font-size:32rpx;font-weight:700}.summary-copy>view>view{height:35rpx;padding:0 11rpx;display:flex;align-items:center;border:1rpx solid rgba(255,234,180,.35);border-radius:18rpx;color:#FFEAB6;background:rgba(255,224,150,.1);font-size:20rpx}.summary-copy>text{margin-top:10rpx;color:#C7D3E2;font-size:22rpx}.renew-entry{height:48rpx;padding:0 14rpx;display:flex;align-items:center;gap:4rpx;border-radius:24rpx;color:#FFF0C2;background:rgba(255,225,155,.12);font-size:21rpx}.member-stats{height:116rpx;position:relative;z-index:1;margin-top:9rpx;display:flex;align-items:center;border:1rpx solid rgba(255,255,255,.08);border-radius:22rpx;background:rgba(255,255,255,.06)}.member-stats>view:not(.stat-line){min-width:0;display:flex;flex:1;align-items:center;flex-direction:column}.member-stats text:first-child{font-size:28rpx;font-weight:700}.member-stats text:last-child{margin-top:7rpx;color:#BFCBDC;font-size:20rpx;white-space:nowrap}.stat-line{width:1rpx;height:52rpx;background:rgba(255,255,255,.12)}.member-body{margin-top:-12rpx;padding:30rpx 0 0;position:relative;z-index:2;border-radius:30rpx 30rpx 0 0;background:#F5F8FC}.section-heading{padding:0 28rpx;display:flex;align-items:center;justify-content:space-between}.section-heading text:first-child{font-size:28rpx;font-weight:700}.section-heading text:last-child{color:#8F9CAC;font-size:21rpx}.plan-scroll{width:100%;margin-top:20rpx;white-space:nowrap}.plan-row{padding:0 28rpx 12rpx;display:flex;gap:16rpx}.plan-card{width:250rpx;height:282rpx;padding:25rpx 20rpx;position:relative;flex:0 0 250rpx;overflow:hidden;border:2rpx solid #E5EAF0;border-radius:24rpx;background:#fff;box-shadow:0 8rpx 23rpx rgba(44,68,96,.04)}.plan-card.active{border-color:#63A5F7;background:linear-gradient(155deg,#F3F8FF,#FFFFFF);box-shadow:0 11rpx 28rpx rgba(52,137,242,.13)}.plan-tag{height:36rpx;padding:0 14rpx;position:absolute;right:-2rpx;top:-2rpx;display:flex;align-items:center;border-radius:0 22rpx 0 16rpx;color:#fff;background:linear-gradient(135deg,#559FF6,#3388F2);font-size:20rpx}.plan-name{font-size:25rpx;font-weight:700}.plan-price{margin-top:23rpx;display:flex;align-items:baseline;color:#3186F1}.plan-price text:first-child{font-size:23rpx;font-weight:600}.plan-price text:nth-child(2){font-size:44rpx;font-weight:700}.plan-price text:last-child{margin-left:3rpx;color:#7D8A9B;font-size:20rpx}.plan-old{margin-top:5rpx;color:#ABB4BF;font-size:20rpx;text-decoration:line-through}.plan-line{height:2rpx;margin:18rpx 0 15rpx;background:#EDF0F4}.plan-desc{color:#586679;font-size:22rpx}.plan-check{width:34rpx;height:34rpx;position:absolute;right:16rpx;bottom:16rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid #D0D8E2;border-radius:50%}.plan-card.active .plan-check{border-color:#3389F7;background:#3389F7}.benefit-heading{margin-top:29rpx}.benefit-grid{margin:20rpx 28rpx 0;padding:10rpx 20rpx;display:flex;flex-wrap:wrap;border-radius:24rpx;background:#fff;box-shadow:0 8rpx 25rpx rgba(39,65,96,.05)}.benefit-item{width:50%;min-height:112rpx;padding:16rpx 5rpx;display:flex;align-items:center}.benefit-icon{width:54rpx;height:54rpx;flex:0 0 54rpx;display:flex;align-items:center;justify-content:center;border-radius:18rpx}.benefit-icon.blue{background:#E9F3FF}.benefit-icon.purple{background:#F0EDFF}.benefit-icon.green{background:#E8F8F2}.benefit-icon.orange{background:#FFF1E5}.benefit-icon.yellow{background:#FFF5E0}.benefit-icon.cyan{background:#EAF6FF}.benefit-item>view:last-child{min-width:0;margin-left:13rpx;display:flex;flex-direction:column}.benefit-item>view:last-child text:first-child{font-size:24rpx;font-weight:600}.benefit-item>view:last-child text:last-child{margin-top:6rpx;color:#919CAB;font-size:20rpx;white-space:nowrap}.saving-card{margin:20rpx 28rpx 0;padding:20rpx 21rpx;border-radius:24rpx;background:linear-gradient(135deg,#FFF9EE,#FFF3DE)}.saving-top{display:flex;align-items:center}.saving-icon{width:50rpx;height:50rpx;flex:0 0 50rpx;display:flex;align-items:center;justify-content:center;border-radius:16rpx;background:#FFF0D1}.saving-top>view:nth-child(2){min-width:0;margin-left:13rpx;display:flex;flex:1;flex-direction:column}.saving-top>view:nth-child(2) text:first-child{color:#725234;font-size:24rpx;font-weight:600}.saving-top>view:nth-child(2) text:last-child{margin-top:5rpx;color:#A68767;font-size:20rpx}.saving-top>text{color:#D98936;font-size:20rpx}.saving-bar{height:14rpx;margin-top:19rpx;overflow:hidden;border-radius:7rpx;background:#F2D9B5}.saving-bar view{width:42%;height:100%;border-radius:7rpx;background:linear-gradient(90deg,#F2AB55,#E78B31)}.saving-label{margin-top:8rpx;display:flex;justify-content:space-between;color:#A18362;font-size:15rpx}.rules-card{min-height:92rpx;margin:20rpx 28rpx 0;padding:0 20rpx;display:flex;align-items:center;justify-content:space-between;border-radius:22rpx;background:#fff}.rules-card>view{display:flex;align-items:center;gap:9rpx}.rules-card>view:first-child{color:#4E5D70;font-size:23rpx}.rules-card>view:last-child{color:#9AA5B2;font-size:20rpx}.agreement{margin:18rpx 30rpx 0;display:flex;align-items:center;color:#7F8B9A;font-size:20rpx}.agreement>view{width:29rpx;height:29rpx;flex:0 0 29rpx;margin-right:9rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid #C4CED9;border-radius:9rpx}.agreement>view.active{border-color:#3389F7;background:#3389F7}.content-spacer{height:calc(152rpx + env(safe-area-inset-bottom))}.bottom-bar{height:calc(128rpx + env(safe-area-inset-bottom));padding:15rpx 28rpx env(safe-area-inset-bottom);position:absolute;z-index:8;left:0;right:0;bottom:0;display:flex;align-items:center;background:rgba(255,255,255,.98);box-shadow:0 -6rpx 24rpx rgba(35,61,91,.08)}.price-copy{min-width:0;display:flex;flex:1;align-items:baseline;flex-wrap:wrap}.price-copy>text:first-child{margin-right:9rpx;color:#7D8998;font-size:20rpx}.price-copy>view{display:flex;align-items:baseline;color:#3389F7}.price-copy>view text:first-child{font-size:23rpx}.price-copy>view text:last-child{font-size:36rpx;font-weight:700}.price-copy>text:last-child{flex-basis:100%;margin-top:2rpx;color:#99A4B1;font-size:15rpx}.submit-button{width:285rpx;height:86rpx;display:flex;align-items:center;justify-content:center;gap:6rpx;border-radius:24rpx;color:#fff;background:linear-gradient(135deg,#4A9DF8,#3185F0);box-shadow:0 10rpx 22rpx rgba(49,133,240,.22);font-size:25rpx;font-weight:600}.submit-button.disabled{opacity:.55}
@media screen and (max-width:360px){.member-hero{padding-right:22rpx;padding-left:22rpx}.plan-row{padding-left:22rpx;padding-right:22rpx}.section-heading{padding-left:22rpx;padding-right:22rpx}.benefit-grid,.saving-card,.rules-card{margin-left:22rpx;margin-right:22rpx}.benefit-grid{padding-left:14rpx;padding-right:14rpx}.benefit-item>view:last-child{margin-left:9rpx}.benefit-item>view:last-child text:last-child{font-size:14rpx}.bottom-bar{padding-right:22rpx;padding-left:22rpx}.submit-button{width:245rpx}.member-stats text:last-child{font-size:15rpx}}

/* visual polish */
.member-hero{height:452rpx;padding-right:30rpx;padding-left:30rpx}.nav-button{width:62rpx;height:62rpx}
.member-emblem{width:90rpx;height:90rpx;flex-basis:90rpx;border-radius:30rpx}.member-stats{height:122rpx;border-radius:25rpx}
.member-body{padding-top:34rpx;border-radius:34rpx 34rpx 0 0}.plan-row{gap:18rpx}.plan-card{height:292rpx;padding:27rpx 22rpx;border-radius:27rpx;box-shadow:var(--gy-card-shadow)}
.benefit-grid{margin-top:23rpx;padding:14rpx 22rpx;border-radius:28rpx;box-shadow:var(--gy-card-shadow)}.benefit-item{min-height:118rpx}
.saving-card{padding:23rpx;border-radius:27rpx}.rules-card{min-height:98rpx;border-radius:25rpx;box-shadow:0 8rpx 24rpx rgba(39,65,96,.045)}
.submit-button{height:90rpx;border-radius:26rpx}
@media screen and (max-width:360px){.member-hero{height:446rpx;padding-right:22rpx;padding-left:22rpx}.plan-card{height:286rpx}.benefit-grid{padding-right:16rpx;padding-left:16rpx}.benefit-item{min-height:112rpx}.submit-button{height:86rpx}}



/* Persistent page header: intentionally outside the scrolling content. */
.fixed-page-header{position:absolute;z-index:20;top:0;right:0;left:0;padding-right:28rpx;padding-left:28rpx;box-sizing:border-box;color:#FFFFFF;background:linear-gradient(180deg,rgba(37,54,79,.98),rgba(37,54,79,.82) 74%,rgba(37,54,79,0));box-shadow:none;backdrop-filter:blur(18rpx)}
.member-hero{padding-top:calc(var(--status-bar-height, 0px) + 96rpx);box-sizing:border-box}
@media screen and (max-width:360px){.fixed-page-header{padding-right:22rpx;padding-left:22rpx}.member-hero{padding-right:22rpx;padding-left:22rpx}}



/* Flat membership surfaces: selection is carried by color and border, not a high card lift. */
.member-emblem{box-shadow:none}
.plan-card,.benefit-grid,.rules-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.plan-card.active{box-shadow:0 3rpx 10rpx rgba(52,137,242,.09)}
.bottom-bar{box-shadow:none;border-top:2rpx solid var(--gy-surface-border)}
.submit-button{box-shadow:var(--gy-shadow-brand)}
.nav-button{border:2rpx solid rgba(255,255,255,.20);box-shadow:none}

</style>
