<template>
  <div class="login-wrapper">
    <div style="position: fixed;left: 0;top: 0;right: 0;bottom: 0" v-if="ad.adFileUrl" @click="toAd">
      <!--  https://corp-public.oss-cn-hangzhou.aliyuncs.com/shiji-cn%202020%20public%20course/Shiji%20Brand%20Video-CN%202020-7-27.mp4-->
      <dplayer v-if="ad.adType.code === 1"
               :video="{url:ad.adFileUrl}">
      </dplayer>
      <div v-else-if="ad.adType.code === 2"></div>
      <t-image style="height: 100%;width: 100%" v-else-if="ad.adType.code === 3" fit="cover"
               :src="ad.adFileUrl"></t-image>
      <iframe :src="ad.adFileUrl" v-else-if="ad.adType.code === 4"
              style="width: 100%;border: none;padding: 0;margin: 0;height: 100%"></iframe>
    </div>
    <login-header/>

    <div class="login-container-parent" style="pointer-events: none">
      <div class="login-container">
        <div class="title-container">
          <h1 class="title margin-no">登录 IcFire Admin</h1>
          <h1 class="title-secondary">springboot3、 mybatis 、 mysql 、 tdesign 、 flutter 、 uni。中台、小程序、app
            集成开发脚手架。</h1>
        </div>

        <login v-if="systemStatus === 'SUCCESS'" style="pointer-events: auto"/>
        <register v-else @register-success="systemStatus = 'SUCCESS'"
                  :reset-code="systemStatus === 'NOT_ACTIVE' || systemStatus === 'OUT_DATE'"
                  style="pointer-events: auto"/>
      </div>
    </div>

    <tdesign-setting/>
    <footer class="copyright">Copyright @ 2021-2022 icefire. All Rights Reserved</footer>
  </div>
</template>
<script lang="ts">
export default {
  name: 'LoginIndex',
};
</script>
<script setup lang="ts">
import dplayer from '@/components/dplayer/login-index.vue';
import {ref} from 'vue';

import ApiSetting from '@/api/sys/ApiSetting';
import TdesignSetting from '@/layouts/setting.vue';

import LoginHeader from './components/Header.vue';
import Login from './components/Login.vue';
import Register from './components/Register.vue';

const systemStatus = ref('SUCCESS');
const ad = ref({
  adFileUrl: '',
  adType: {
    code: 1,
    name: '视频广告'
  },
  adUrl: ''
});
ApiSetting.isInit({
  success: (res: any) => {
    systemStatus.value = res
  }
});
ApiSetting.ad({
  success: (res: any) => {
    ad.value = res
  }
});
const toAd = () => {
  if (ad.value.adUrl) {
    window.open(ad.value.adUrl)
  }
}
</script>

<style lang="less" scoped>
@import url('./index.less');
</style>
