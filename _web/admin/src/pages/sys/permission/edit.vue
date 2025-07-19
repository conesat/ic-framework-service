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
        <div class="form-basic-container-title">权限</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="标识" name="sign">
              <t-input v-model="formData.sign" placeholder="暂无" readonly />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="暂无" :maxlength="255" readonly />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="url" name="url">
              <t-input v-model="formData.url" placeholder="暂无" :maxlength="255" readonly />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="userType" name="userType">
              <t-input v-model="formData.userType" placeholder="暂无" :maxlength="255" readonly />
            </t-form-item>
          </t-col>
        </t-row>
      </div>
    </div>
  </t-form>
</template>

<script lang="ts">
export default {
  name: 'permissionEdit',
};
</script>

<script setup lang="ts">
import type { SubmitContext } from 'tdesign-vue-next';
import { FormRule, MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import ApiPermission from '@/api/sys/ApiPermission';
import router from '@/router';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: '',
  sign: '',
  name: '',
  url: '',
  userType: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiPermission.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        router.back();
      },
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const { id } = route.query;
  if (id) {
    ApiPermission.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      },
    });
  }
});
</script>

<style lang="less" scoped></style>
