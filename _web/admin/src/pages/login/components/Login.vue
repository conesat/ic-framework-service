<template>
  <t-form
    ref="form"
    :class="['item-container', `login-passwrod`]"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-form-item name="username" label="账号">
      <t-input v-model="formData.username" autocomplete="off" placeholder="请输入账号">
        <template #prefix-icon>
          <t-icon name="user"/>
        </template>
      </t-input>
    </t-form-item>

    <t-form-item name="passwd" label="密码">
      <t-input
        v-model="formData.passwd"
        autocomplete="new-password"
        type="password"
        clearable
        placeholder="请输入登录密码"
        @change="changePassword"
      >
        <template #prefix-icon>
          <t-icon name="lock-on"/>
        </template>
      </t-input>
    </t-form-item>

    <div class="check-container remember-pwd">
      <t-checkbox v-model="saveAccount">记住账号</t-checkbox>
      <span class="tip">忘记账号？</span>
    </div>

    <t-form-item class="btn-container">
      <t-button block size="large" type="submit" :disabled="onLogin" :loading="onLogin"> 登录</t-button>
    </t-form-item>
  </t-form>
</template>

<script setup lang="ts">
import JSEncrypt from 'jsencrypt';
import CryptoJS from 'crypto-js';
import md5 from 'js-md5';
import type {FormInstanceFunctions, FormRule, SubmitContext} from 'tdesign-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';

import ManagerService from '@/api/sys/ApiUserMine';
import {useUserStore} from '@/store';

const userStore = useUserStore();

const INITIAL_DATA = {
  phone: '',
  username: '',
  passwd: '',
  verifyCode: '',
  checked: false,
};

const FORM_RULES: Record<string, FormRule[]> = {
  phone: [{required: true, message: '手机号必填', type: 'error'}],
  username: [{required: true, message: '账号必填', type: 'error'}],
  passwd: [{required: true, message: '密码必填', type: 'error'}],
  verifyCode: [{required: true, message: '验证码必填', type: 'error'}],
};

const form = ref<FormInstanceFunctions>();
const formData = ref({...INITIAL_DATA});
const onLogin = ref(false);
const router = useRouter();
const route = useRoute();


const changePassword = (value: any, e: any) => {
  readPasswdFromLocalStorage.value = false
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    onLogin.value = true;

    ManagerService.key(formData.value.username, {
      success: async (res: any) => {
        try {
          const form = JSON.parse(JSON.stringify(formData.value));
          const rsaEncrypt = new JSEncrypt();
          rsaEncrypt.setPublicKey(res);
          let passwd
          // 如果是记住的密码，需要从记录中解密
          if (accountInfo && readPasswdFromLocalStorage.value) {
            passwd = decrypt(accountInfo.username, accountInfo.value);
          } else {
            passwd = md5(form.username + md5(form.username + form.passwd))
          }
          form.passwd = rsaEncrypt.encrypt(passwd);
          await userStore.login(form);
          // 如果记住密码需要加密保存
          if (saveAccount.value && !readPasswdFromLocalStorage.value) {
            let accountJson = JSON.stringify({
              username: formData.value.username,
              value: encrypt(formData.value.username, passwd), // 将密码加密
            });
            localStorage.setItem(
              'account-info', accountJson
            );
          } else if (!saveAccount.value) {
            localStorage.removeItem('account-info');
          }
          await MessagePlugin.success('登陆成功');
          const redirect = route.query.redirect as string;
          const redirectUrl = redirect ? decodeURIComponent(redirect) : '/dashboard';
          if (router.getRoutes().map(f => f.path).includes(redirectUrl)) {
            await router.push(redirectUrl);
          } else {
            router.push('/');
          }
        } catch (e) {
          await MessagePlugin.error(e.msg);
        } finally {
          onLogin.value = false;
        }
      }
    });
  }
};

// 读取已保存的账号密码
let saveAccount = ref(false);
// 标记是否从本地读取
let readPasswdFromLocalStorage = ref(false);
const accountStr = localStorage.getItem('account-info');
let accountInfo = accountStr ? JSON.parse(accountStr) : undefined;
if (accountInfo) {
  saveAccount.value = true;
  readPasswdFromLocalStorage.value = true;
  formData.value.username = accountInfo.username;
  formData.value.passwd = "******";
}
const key_str = 'icfire';

const fitKey = (str: string) => {
  let key = key_str + str;
  while (key.length < 16) {
    key += str;
  }
  return key.slice(0, 16);
}

// 加密函数
function encrypt(account: string, passwd: string) {
  let key = CryptoJS.enc.Utf8.parse(fitKey(account))
  const encrypted = CryptoJS.AES.encrypt(
    passwd,
    key,
    {
      iv: key,
      mode: CryptoJS.mode.CBC, // 使用CBC模式
      padding: CryptoJS.pad.Pkcs7 // 使用PKCS7填充
    }
  );
  return encrypted.toString(); // 返回Base64编码的加密字符串
}

// 解密函数
function decrypt(account: string, passwd: string) {
  let key = CryptoJS.enc.Utf8.parse(fitKey(account))
  const decrypted = CryptoJS.AES.decrypt(
    passwd,
    key,
    {
      iv: key,
      mode: CryptoJS.mode.CBC, // 使用相同的模式
      padding: CryptoJS.pad.Pkcs7 // 使用相同的填充
    }
  );
  return decrypted.toString(CryptoJS.enc.Utf8); // 转换为UTF-8字符串
}

</script>

<style lang="less" scoped>
@import url('../index.less');
</style>
