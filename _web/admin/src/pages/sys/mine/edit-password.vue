<template>
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
        <div class="form-basic-container-title">修改密码</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item name="passwdOld" label="原密码">
              <t-input v-model="formData.passwdOld" type="password" :maxlength="50"
                       placeholder="请输入原密码"></t-input>
            </t-form-item>
            <t-form-item name="passwd" label="新密码">
              <t-input v-model="formData.passwd" autocomplete="new-password" type="password" :maxlength="50"
                       placeholder="请输入新密码"></t-input>
            </t-form-item>
            <t-form-item name="repasswd" label="确认密码">
              <t-input v-model="formData.repasswd" autocomplete="new-password" type="password" :maxlength="50"
                       placeholder="请确认密码"></t-input>
            </t-form-item>
          </t-col>
        </t-row>
      </div>
    </div>

    <div class="form-submit-container">
      <div class="form-submit-sub">
        <div class="form-submit-left">
          <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
          <t-button type="reset" class="form-submit-cancel" theme="default" variant="base">重置
          </t-button>
        </div>
      </div>
    </div>
  </t-form>
</template>

<script lang="ts">
export default {
  name: 'ManagerEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';

import ApiUserMine from '@/api/sys/ApiUserMine';
import router from '@/router';
import {encryptPasswd} from "@/utils/passwd-utils";


// 自定义校验 start -------------------
const passwdValidator: any = (val: string) => {
  if (val === formData.value.passwdOld) {
    return {result: false, message: '新密码不能与旧密码一致', type: 'error'};
  }
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
// 自定义校验 end -------------------

// 定义变量 start -------------------

// 表单
const formData = ref({
  username: '',
  passwdOld: '',
  passwd: '',
  repasswd: ''
});


// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  passwdOld: [{required: true, message: '原密码不能为空', type: 'error'}],
  passwd: [
    {required: true, message: '密码不能为空', type: 'error', trigger: 'blur'},
    // 自定义校验规则：不同的值可以有不同的校验结果，不同的校验类型
    {validator: passwdValidator},
  ],
  repasswd: [
    {required: true, message: '请确认密码', type: 'error', trigger: 'blur'},
    // 自定义校验规则：自定义异步校验规则
    {validator: repasswd, message: '两次密码不一致'},
  ],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {

};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiUserMine.key(formData.value.username, {
      success: (res: any) => {
        const passwd = encryptPasswd(formData.value.username, formData.value.passwd, res);
        const passwdOld = encryptPasswd(formData.value.username, formData.value.passwdOld, res);
        ApiUserMine.updatePassword({
          data: {
            passwd: passwd,
            passwdOld: passwdOld
          },
          success: (res: any) => {
            MessagePlugin.success('已完成');
            router.back();
          }
        });
      }
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  ApiUserMine.mine({
    success: (res: any) => {
      formData.value.username = res.username;
    }
  });
});
</script>

<style lang="less" scoped></style>
