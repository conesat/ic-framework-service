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
        <div class="form-basic-container-title">编辑文件</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="文件大小" name="size">
              <t-input v-model="formData.size" placeholder="请输入文件大小" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="ossObjectName" name="ossObjectName">
              <t-input v-model="formData.ossObjectName" placeholder="请输入ossObjectName" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="文件名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入文件名称" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="bucketUrl" name="bucketUrl">
              <t-input v-model="formData.bucketUrl" placeholder="请输入bucketUrl" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="引用次数" name="refCount">
              <t-input v-model="formData.refCount" placeholder="请输入引用次数"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="文件系统类型" name="type">
              <t-select v-model="formData.type">
                <t-option key="1" label="阿里云" :value="1"/>
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
</template>

<script lang="ts">

export default {
  name: 'sysFileEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiSysFile from '@/api/sys/ApiSysFile';
import router from "@/router";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  size: 0,
  ossObjectName: '',
  name: '',
  bucketUrl: '',
  refCount: 0,
  type: 1,
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '请输入文件名', type: 'error'}],
  type: [{required: true, message: '请选择文件系统类型', type: 'error'}],
  refCount: [{required: true, message: '请选输入引用次数', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiSysFile.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        router.back();
      }
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiSysFile.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
