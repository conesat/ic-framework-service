<template>
  <t-form
    ref="form"
    :class="['item-container']"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-form-item v-if="!resetCode" name="name" label="系统名称">
      <t-input v-model="formData.name" size="large" :maxlength="11" placeholder="请输入系统名称"></t-input>
    </t-form-item>
    <t-form-item v-if="!resetCode" name="url" label="域名">
      <t-input v-model="formData.url" size="large" :maxlength="50" placeholder="请输入域名"></t-input>
    </t-form-item>
    <t-form-item v-if="!resetCode" name="username" label="超管账号">
      <t-input v-model="formData.username" size="large" :maxlength="50" placeholder="请输入超管账号"></t-input>
    </t-form-item>
    <t-form-item v-if="!resetCode" name="passwd" label="超管密码">
      <t-input v-model="formData.passwd" size="large" type="password" :maxlength="50" placeholder="请输入超管密码">
      </t-input>
    </t-form-item>
    <t-form-item v-if="!resetCode" name="repasswd" label="确认密码">
      <t-input v-model="formData.repasswd" size="large" type="password" :maxlength="50" placeholder="请确认密码">
      </t-input>
    </t-form-item>
    <t-form-item name="activationCode" label="激活码">
      <t-textarea v-model="formData.activationCode" size="large" placeholder="请输入激活码"></t-textarea>
    </t-form-item>
    <t-form-item>
      <t-button block type="submit"> 激活</t-button>
    </t-form-item>
  </t-form>
</template>

<script setup lang="ts">
import type {FormRule, SubmitContext} from 'tdesign-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import {ref} from 'vue';

import ApiUserMine from '@/api/user/ApiUserMine';
import ApiSetting from '@/api/sys/ApiSetting';
import {encryptPasswd} from "@/utils/passwd-utils";
import success from "@/pages/result/success/index.vue";

const props = defineProps({
  resetCode: {
    type: Boolean,
    default: false
  }
});
const INITIAL_DATA = {
  name: '',
  url: '',
  activationCode: '',
  username: 'admin',
  passwd: '',
  repasswd: '',
};

const form = ref();
const formData = ref({...INITIAL_DATA});

const emit = defineEmits(['registerSuccess']);

const usernameValidator: any = (val: string) => {
  const re = /^[a-zA-z]\w{3,15}$/;
  if (re.test(val)) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '账号为字母、数字、下划线组成，字母开头，4-16位', type: 'error'};
};

const passwdValidator: any = (val: string) => {
  const re = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
  if (re.test(val)) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '密码需要同时含有数字和字母，且长度要在8-16位之间', type: 'error'};
};

const repasswd: any = (val: string) => {
  return new Promise((resolve) => {
    const timer = setTimeout(() => {
      resolve(formData.value.passwd === val);
      clearTimeout(timer);
    });
  });
};

const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '系统名称必填', type: 'error'}],
  activationCode: [{required: true, message: '激活码必填', type: 'error'}],
  url: [{required: true, message: '域名必填', type: 'error'}],
  username: [
    {required: true, message: '账号不能为空', type: 'error'},
    // 自定义校验规则：不同的值可以有不同的校验结果，不同的校验类型
    {validator: usernameValidator},
  ],
  passwd: [
    {required: true, message: '密码不能为空', type: 'error'},
    // 自定义校验规则：不同的值可以有不同的校验结果，不同的校验类型
    {validator: passwdValidator},
  ],
  repasswd: [
    {required: true, message: '请确认密码', type: 'error'},
    // 自定义校验规则：自定义异步校验规则
    {validator: repasswd, message: '两次密码不一致'},
  ],
};

const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (props.resetCode) {
      ApiSetting.resetCode(formData.value.activationCode, {
        success: () => {
          MessagePlugin.success('激活成功');
          emit('registerSuccess');
        }
      });
    } else {
      const form = JSON.parse(JSON.stringify(formData.value));
      ApiUserMine.key(form.username, {
        success: (res: any) => {
          form.passwd = encryptPasswd(form.username, form.passwd, res);
          ApiSetting.init({
            data: form,
            success: () => {
              MessagePlugin.success('注册成功');
              emit('registerSuccess');
            }
          });
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
@import url('../index.less');
</style>
