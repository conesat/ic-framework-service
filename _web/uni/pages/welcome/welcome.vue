<template>
  <view class="login-page">
    <view class="hero">
      <view class="hero-orb hero-orb-one"></view>
      <view class="hero-orb hero-orb-two"></view>
      <view class="hero-grid"></view>
      <view class="brand-row">
        <view class="brand-mark">
          <view class="brand-bolt"></view>
        </view>
        <text class="brand-name">电能行</text>
      </view>

      <view class="hero-copy">
        <text class="hero-title">智能换电</text>
        <text class="hero-title">安心续航</text>
        <view class="hero-subtitle">3 步换电，30 秒满电出发</view>
      </view>

      <view class="swap-steps">
        <view class="step-item">
          <view class="step-icon station-icon"><view></view><view></view><view></view></view>
          <text>找换电站</text>
        </view>
        <view class="step-line"></view>
        <view class="step-item">
          <view class="step-icon scan-icon"><view></view><view></view><view></view><view></view></view>
          <text>扫码开柜</text>
        </view>
        <view class="step-line"></view>
        <view class="step-item">
          <view class="step-icon battery-icon"><view></view></view>
          <text>满电出发</text>
        </view>
      </view>
    </view>

    <view class="login-sheet">
      <view class="sheet-handle"></view>
      <view class="login-heading">
        <text class="login-title">欢迎登录</text>
        <text class="login-desc">体验账号已预填，可直接登录查看服务</text>
      </view>

      <view class="form-card">
        <view class="field-row">
          <uv-icon name="phone" color="#69766F" size="21"></uv-icon>
          <input v-model="phone" class="field-input" type="number" maxlength="11" placeholder="请输入手机号" placeholder-class="placeholder" />
        </view>
        <view class="field-divider"></view>
        <view class="field-row">
          <uv-icon name="lock" color="#69766F" size="21"></uv-icon>
          <input v-model="password" class="field-input" password maxlength="20" placeholder="请输入登录密码" placeholder-class="placeholder" />
        </view>
      </view>

      <view class="demo-account" @tap="fillDemoAccount">
        <view class="demo-icon"><uv-icon name="account-fill" color="#3389F7" size="19"></uv-icon></view>
        <view class="demo-copy"><text>体验账号</text><text>13800138000　密码：123456</text></view>
        <view class="demo-fill">一键填入</view>
      </view>

      <view class="agreement" @tap="agreed = !agreed">
        <view class="checkbox" :class="{ checked: agreed }">
          <uv-icon v-if="agreed" name="checkmark" color="#FFFFFF" size="12"></uv-icon>
        </view>
        <view class="agreement-text">我已阅读并同意<text class="agreement-link">《用户服务协议》</text>和<text class="agreement-link">《隐私政策》</text></view>
      </view>

      <view class="login-button" :class="{ 'login-button-disabled': !canLogin }" @tap="login">
        登录 / 注册
      </view>

      <view class="other-login">
        <view class="other-line"></view>
        <text>其他登录方式</text>
        <view class="other-line"></view>
      </view>

      <view class="quick-login-list">
        <view class="quick-login-item" @tap="wechatLogin">
          <view class="quick-icon wechat-icon"><uv-icon name="weixin-fill" color="#16B887" size="25"></uv-icon></view>
          <text>微信登录</text>
        </view>
        <view class="quick-login-item" @tap="quickPhoneLogin">
          <view class="quick-icon phone-icon"><uv-icon name="phone-fill" color="#16B887" size="22"></uv-icon></view>
          <text>手机号一键登录</text>
        </view>
      </view>
    </view>

    <view class="login-footer">换电更安全 · 出行更无忧</view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      phone: '13800138000',
      password: '123456',
      agreed: true
    }
  },
  computed: {
    canLogin() {
      return /^1\d{10}$/.test(this.phone) && this.password.length >= 6 && this.agreed
    }
  },
  methods: {
    fillDemoAccount() {
      this.phone = '13800138000'
      this.password = '123456'
      this.agreed = true
      uni.showToast({ title: '体验账号已填入', icon: 'none' })
    },
    login() {
      if (!this.canLogin) {
        uni.showToast({ title: '请填写手机号、密码并阅读协议', icon: 'none' })
        return
      }
      this.enterHome()
    },
    wechatLogin() {
      this.enterHome()
    },
    quickPhoneLogin() {
      this.enterHome()
    },
    enterHome() {
      uni.reLaunch({ url: '/pages/index/index' })
    }
  }
}
</script>

<style scoped>
.login-page{min-height:100%;overflow-x:hidden;overflow-y:auto;position:relative;color:#242D3D;background:#F8FAFD}.hero{height:628rpx;padding:calc(var(--status-bar-height,44rpx) + 26rpx) 54rpx 0;box-sizing:border-box;position:relative;overflow:hidden;background:linear-gradient(150deg,#F9FBFF 0%,#F2F7FF 100%)}.hero:before{content:'';position:absolute;right:-76rpx;top:143rpx;width:330rpx;height:330rpx;border:2rpx solid rgba(50,137,245,.12);border-radius:50%;box-shadow:inset 0 0 0 30rpx rgba(86,161,251,.035)}.hero:after{content:'';position:absolute;right:68rpx;top:205rpx;width:146rpx;height:186rpx;border:5rpx solid #398AF5;border-radius:18rpx;background:linear-gradient(180deg,#71B3FF,#3788F3);box-shadow:17rpx 17rpx 0 rgba(59,139,243,.13)}.hero-orb{position:absolute;border:2rpx solid rgba(64,145,244,.11);border-radius:50%}.hero-orb-one{width:470rpx;height:470rpx;right:-285rpx;top:73rpx}.hero-orb-two{width:250rpx;height:250rpx;left:-185rpx;top:390rpx;border-color:rgba(107,172,251,.16)}.hero-grid{position:absolute;right:88rpx;top:231rpx;z-index:1;width:104rpx;height:140rpx;padding:14rpx;box-sizing:border-box;display:grid;grid-template-columns:repeat(2,1fr);gap:9rpx;opacity:1;transform:rotate(-4deg);background:#DDECFF;border-radius:10rpx;background-image:none}.hero-grid:before,.hero-grid:after{content:'';background:#fff;border-radius:4rpx;box-shadow:57rpx 0 0 #fff,0 57rpx 0 #fff,57rpx 57rpx 0 #fff}.brand-row{position:relative;z-index:3;transform:translateY(84rpx);display:flex;align-items:center}.brand-mark{width:48rpx;height:48rpx;border-radius:15rpx;background:#378AF5;position:relative;box-shadow:0 9rpx 18rpx rgba(55,138,245,.16)}.brand-bolt{position:absolute;left:19rpx;top:10rpx;width:11rpx;height:27rpx;border-radius:3rpx;transform:skewX(-19deg);background:#fff}.brand-bolt:after{content:'';position:absolute;right:-5rpx;top:9rpx;width:13rpx;height:7rpx;background:#378AF5}.brand-name{margin-left:14rpx;color:#2D87F5;font-size:31rpx;letter-spacing:2rpx;font-weight:700}.hero-copy{position:relative;z-index:3;margin-top:134rpx}.hero-title{display:block;color:#151C28;font-size:60rpx;line-height:1.32;font-weight:700;letter-spacing:1rpx}.hero-subtitle{margin-top:18rpx;color:#8794A7;font-size:26rpx;letter-spacing:1rpx}.swap-steps{display:none}.login-sheet{position:relative;z-index:4;min-height:1000rpx;margin-top:-22rpx;padding:28rpx 48rpx 54rpx;box-sizing:border-box;border-radius:42rpx 42rpx 0 0;background:#fff;box-shadow:0 -14rpx 38rpx rgba(47,91,147,.04)}.sheet-handle{width:66rpx;height:7rpx;margin:0 auto;border-radius:9rpx;background:#D7E1F0}.login-heading{margin-top:40rpx}.login-title{display:block;color:#1E2735;font-size:42rpx;font-weight:700}.login-desc{display:block;margin-top:13rpx;color:#929DAD;font-size:25rpx}.form-card{margin-top:39rpx;padding:0 26rpx;box-sizing:border-box;border:2rpx solid #E8EDF5;border-radius:19rpx;background:#fff}.field-row{height:106rpx;display:flex;align-items:center}.field-input{min-width:0;flex:1;margin-left:20rpx;color:#222C3A;font-size:29rpx}.placeholder{color:#B0B9C6}.field-divider{height:2rpx;background:#EEF2F7}.demo-account{min-height:86rpx;margin-top:18rpx;padding:16rpx 18rpx;display:flex;align-items:center;box-sizing:border-box;border:2rpx solid #DCEBFC;border-radius:18rpx;background:#F7FBFF}.demo-icon{width:48rpx;height:48rpx;flex:0 0 48rpx;display:flex;align-items:center;justify-content:center;border-radius:15rpx;background:#E5F1FF}.demo-copy{min-width:0;flex:1;margin-left:13rpx;display:flex;flex-direction:column}.demo-copy text:first-child{color:#367ED9;font-size:22rpx;font-weight:650}.demo-copy text:last-child{margin-top:3rpx;color:#66809D;font-size:21rpx;white-space:nowrap}.demo-fill{height:46rpx;margin-left:10rpx;padding:0 14rpx;display:flex;align-items:center;justify-content:center;border-radius:14rpx;background:#EAF4FF;color:#3389F7;font-size:21rpx;font-weight:600;white-space:nowrap}.code-button{padding-left:18rpx;color:#3388F6;font-size:25rpx;white-space:nowrap}.code-button.disabled{color:#A6B1C0}.agreement{display:flex;align-items:flex-start;margin-top:22rpx}.checkbox{width:28rpx;height:28rpx;box-sizing:border-box;display:flex;align-items:center;justify-content:center;flex-shrink:0;margin:4rpx 14rpx 0 0;border:2rpx solid #C6CEDB;border-radius:50%}.checkbox.checked{border-color:#3689F5;background:#3689F5}.agreement-text{color:#8792A2;font-size:24rpx;line-height:1.65}.agreement-link{color:#3889F5}.login-button{height:96rpx;margin-top:37rpx;display:flex;align-items:center;justify-content:center;border-radius:17rpx;color:#fff;background:#378AF5;box-shadow:0 13rpx 25rpx rgba(55,138,245,.21);font-size:31rpx;font-weight:600}.login-button-disabled{background:#AFCDF2;box-shadow:none}.other-login{display:flex;align-items:center;gap:20rpx;margin-top:52rpx;color:#A1AAB8;font-size:24rpx}.other-line{height:2rpx;flex:1;background:#EEF2F7}.quick-login-list{display:flex;justify-content:center;gap:74rpx;margin-top:34rpx}.quick-login-item{display:flex;align-items:center;flex-direction:column;color:#657084;font-size:25rpx;white-space:nowrap}.quick-icon{width:68rpx;height:68rpx;box-sizing:border-box;display:flex;align-items:center;justify-content:center;margin-bottom:14rpx;border:2rpx solid #DCE8FA;border-radius:50%;background:#F3F8FF}.login-footer{position:absolute;z-index:5;right:0;bottom:calc(48rpx + env(safe-area-inset-bottom));left:0;color:#8491A4;text-align:center;font-size:24rpx;letter-spacing:1rpx}

/* visual polish */
.hero{background:radial-gradient(circle at 82% 42%,rgba(70,153,250,.1),transparent 25%),linear-gradient(150deg,#FBFDFF 0%,#F1F6FE 100%)}
.brand-mark{box-shadow:0 11rpx 24rpx rgba(55,138,245,.2)}
.login-sheet{border-radius:46rpx 46rpx 0 0;box-shadow:0 -18rpx 45rpx rgba(47,91,147,.06)}
.form-card{border-radius:23rpx;box-shadow:0 8rpx 24rpx rgba(39,65,96,.035)}
.login-button{height:100rpx;border-radius:23rpx;box-shadow:0 15rpx 30rpx rgba(55,138,245,.23)}
.quick-icon{width:72rpx;height:72rpx;box-shadow:0 7rpx 20rpx rgba(55,115,190,.055)}



/* Flat login treatment: the form is structured by borders and space, not raised sheets. */
.brand-mark{box-shadow:var(--gy-shadow-brand)}
.login-sheet{box-shadow:none;border-top:2rpx solid rgba(224,232,242,.88)}
.form-card,.quick-icon{box-shadow:none}
.form-card{border-color:var(--gy-surface-border)}
.login-button{box-shadow:var(--gy-shadow-brand)}

</style>
