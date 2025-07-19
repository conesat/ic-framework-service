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
        <div class="form-basic-container-title">编辑个人信息</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="账号" name="username">
              <t-input v-model="formData.username" :disabled="formData.id !== ''" placeholder="请输入内容"/>
            </t-form-item>

            <t-form-item label="姓名" name="name">
              <t-input v-model="formData.name" placeholder="请输入内容"/>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="头像" name="avatarFileId">
              <cropper
                style="border-radius: 50%"
                :cover-width="400"
                :disabled="formData.su"
                :cover-height="400"
                :use-type="FileUseTypes.avatar"
                :url="formData.avatarFileUrl"
                @uploaded="onAvatarUploaded"
              ></cropper>
            </t-form-item>
          </t-col>

          <template v-if="!formData.id">
            <t-col :span="6">
              <t-form-item name="passwd" label="密码">
                <t-input v-model="formData.passwd" autocomplete="new-password" type="password" :maxlength="50"
                         placeholder="请输入密码"></t-input>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item name="repasswd" label="确认密码">
                <t-input v-model="formData.repasswd" autocomplete="new-password" type="password" :maxlength="50"
                         placeholder="请确认密码"></t-input>
              </t-form-item>
            </t-col>
          </template>
          <t-col :span="6">
            <t-form-item name="phone" label="手机号">
              <t-input v-model="formData.phone" :maxlength="11" placeholder="请输入11数位手机号"></t-input>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item name="email" label="邮箱">
              <t-auto-complete v-model="formData.email"
                               autocomplete="off"
                               :options="emailOptions" filterable
                               :maxlength="100"
                               placeholder="请输入邮箱号"></t-auto-complete>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="性别" name="sex">
              <t-radio-group v-model="formData.sex">
                <t-radio :value="1"> 男</t-radio>
                <t-radio :value="2"> 女</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
        </t-row>
      </div>
    </div>

    <div class="form-submit-container">
      <div class="form-submit-sub">
        <div class="form-submit-left">
          <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
          <t-button type="reset" class="form-submit-cancel" theme="default" variant="base" :disabled="formData.su">
            重置
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
import type {AutoCompleteProps, SubmitContext, UploadProps} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiUserMine from '@/api/sys/ApiUserMine';
import Cropper from '@/components/upload/cropper.vue';
import router from '@/router';
import {FileUseTypes} from "@/constants/file-user-types";


// 自定义校验 start -------------------
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
// 自定义校验 end -------------------

// 定义变量 start -------------------

// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: '',
  username: '',
  name: '',
  sex: 1,
  su: false,
  passwd: '',
  phone: '',
  email: '',
  repasswd: '',
  passwdCode: '',
  avatarFileId: '',
  avatarFileUrl: '',
});


const emailSuffix = ['@qq.com', '@163.com', '@gmail.com'];
const emailOptions = computed<AutoCompleteProps['options']>(() => {
  const emailPrefix = formData.value.email ? formData.value.email.split('@')[0] : "";
  if (!emailPrefix) return [];
  return emailSuffix.map((suffix) => emailPrefix + suffix);
});

// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '管理员姓名不能为空', type: 'error'}],
  status: [{required: true, message: '状态不能为空', type: 'error'}],
  roleIds: [{required: true, message: '角色不能为空', type: 'error'}],
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
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiUserMine.editMine({
      data: formData.value, success: (res: any) => {
        MessagePlugin.success('已完成');
        router.back();
      }
    });
  }
};
const onAvatarUploaded = (res: any) => {
  formData.value.avatarFileId = res.id
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiUserMine.mine({
      success: (res: any) => {
        res.sex = res.sex ? res.sex.code : 1;
        delete res.status;
        delete res.deps;
        delete res.pos;
        delete res.roles;
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
