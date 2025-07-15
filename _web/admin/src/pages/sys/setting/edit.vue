<template>
  <t-space direction="vertical" style="width: 100%">
    <t-form
      ref="form"
      class="base-form"
      :data="formData"
      :rules="FORM_RULES"
      label-align="top"
      :label-width="100"
      @keydown.enter.prevent
      @reset="onReset"
      @submit="onSubmit"
    >
      <div class="form-basic-container">
        <div class="form-basic-item">
          <div class="form-basic-container-title">
            <span style="margin-right: 10px">系统设置</span>
          </div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[32, 24]">
            <t-col :span="6">
              <t-form-item label="名称" name="name">
                <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="域名" name="domain">
                <t-input v-model="formData.domain" placeholder="请输入域名" :maxlength="255"/>
              </t-form-item>
            </t-col>
            <t-divider align="left" style="margin-bottom: 0">PC登录页广告</t-divider>
            <t-col :span="6">
              <t-form-item label="登录广告文件地址">
                <t-input v-model="formData.adFileUrl" placeholder="登录广告文件地址" :maxlength="255" clearable/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="广告点击跳转地址">
                <t-input v-model="formData.adUrl" placeholder="广告点击跳转地址" :maxlength="255" clearable/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="登录广告类型">
                <t-select v-model="formData.adType">
                  <t-option :key="1" label="视频" :value="1"/>
                  <t-option :key="2" label="直播" :value="2"/>
                  <t-option :key="3" label="图片" :value="3"/>
                  <t-option :key="4" label="网页" :value="4"/>
                </t-select>
              </t-form-item>
            </t-col>
            <t-divider align="left" style="margin-bottom: 0">APP启动页广告</t-divider>
            <t-col :span="6">
              <t-form-item label="广告文件地址">
                <t-input v-model="formData.appAdFileUrl" placeholder="广告文件地址" :maxlength="255" clearable/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="广告点击跳转地址">
                <t-input v-model="formData.appAdUrl" placeholder="广告点击跳转地址" :maxlength="255" clearable/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="广告类型">
                <t-select v-model="formData.appAdType">
                  <t-option :key="1" label="视频" :value="1"/>
                  <t-option :key="2" label="直播" :value="2" disabled/>
                  <t-option :key="3" label="图片" :value="3"/>
                  <t-option :key="4" label="网页" :value="4" disabled/>
                </t-select>
              </t-form-item>
            </t-col>
          </t-row>
        </div>
      </div>

      <div class="form-submit-container">
        <div class="form-submit-sub">
          <div class="form-submit-left">
            <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
            <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 重置</t-button>
          </div>
        </div>
      </div>
    </t-form>

    <div class="form-basic-container form-basic-container-radius">
      <t-descriptions title="激活信息" itemLayout="vertical" :column="3" :labelStyle="{'padding-bottom': '0'}">
        <t-descriptions-item label="编号：">{{ formData.activationInfoVO.code }}</t-descriptions-item>
        <t-descriptions-item label="授予：">{{ formData.activationInfoVO.grant }}</t-descriptions-item>
        <t-descriptions-item label="授权人：">{{ formData.activationInfoVO.author }}</t-descriptions-item>
        <t-descriptions-item label="有效期：">{{ formData.activeTime }}</t-descriptions-item>
        <t-descriptions-item label="code：">
          <div style="word-break: break-all">{{ formData.activationInfoVO.md5Code }}</div>
        </t-descriptions-item>
        <t-descriptions-item label="hash：">
          <div style="word-break: break-all">{{ formData.activationInfoVO.md5Hash }}</div>
        </t-descriptions-item>
        <t-descriptions-item label="">
          <t-button @click="showReActiveDialog = true">更换激活码</t-button>
        </t-descriptions-item>
      </t-descriptions>
    </div>

    <t-dialog :visible.sync="showReActiveDialog"
              header="更换激活码"
              @cancel="showReActiveDialog = false" @confirm="reActive">
      <t-textarea placeholder="请输入激活码" v-model="activeCode"/>
    </t-dialog>
  </t-space>
</template>

<script lang="ts">
export default {
  name: 'roleEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiSetting from '@/api/sys/ApiSetting';
import router from '@/router';
import {closeOrBack} from "@/utils/url-utils";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
const showReActiveDialog = ref(false);
const activeCode = ref('');
// 表单
const formData = ref({
  name: '',
  domain: '',
  adFileUrl: '',
  adUrl: '',
  adType: 3,
  appAdFileUrl: '',
  appAdUrl: '',
  appAdType: 3,
  activeTime: '',
  activationInfoVO: {
    author: "",
    code: "",
    forever: true,
    generationTime: 0,
    grant: "",
    md5Code: "",
    md5Hash: "",
    outTime: 0
  }
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiSetting.updateSetting({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
      }
    });
  }
};
const formattedDate = (time: number) => {
  const date = new Date(time);
  const year = date.getFullYear();
  const month = ("0" + (date.getMonth() + 1)).slice(-2);
  const day = ("0" + date.getDate()).slice(-2);
  const hours = ("0" + date.getHours()).slice(-2);
  const minutes = ("0" + date.getMinutes()).slice(-2);
  const seconds = ("0" + date.getSeconds()).slice(-2);
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const reActive = () => {
  if (!activeCode.value) {
    return;
  }
  ApiSetting.resetCode(activeCode.value, {
    success: () => {
      MessagePlugin.success('激活成功');
      activeCode.value = '';
      showReActiveDialog.value = false;
      getData();
    }
  })
}
const getData = () => {
  ApiSetting.detail({
    id: null,
    success: (res: any) => {
      res.activeTime = formattedDate(res.activationInfoVO.generationTime) + " 至 " + (res.activationInfoVO.forever ? '永久' : formattedDate(res.activationInfoVO.outTime))
      res.adType = res.adType ? res.adType.code : 3;
      res.appAdType = res.appAdType ? res.appAdType.code : 3;
      formData.value = res;
    }
  });
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped></style>
