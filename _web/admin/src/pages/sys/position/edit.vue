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
        <div class="form-basic-container-title">编辑职位</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="编码" name="sign">
              <t-input v-model="formData.sign" placeholder="请输入编码" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="职级" name="level">
              <t-input-number v-model="formData.level" placeholder="请输入职级" :min="1"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是与否有效" name="status">
              <t-radio-group v-model="formData.status">
                <t-radio :value="1"> 启用</t-radio>
                <t-radio :value="0"> 禁用</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>

          <template v-if="formData.id">
            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input disabled v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input disabled v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255"/>
              </t-form-item>
            </t-col>
          </template>

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
  name: 'positionEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiPosition from '@/api/sys/ApiPosition';
import router from '@/router';
import {closeOrBack} from "@/utils/url-utils";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: '',
  name: '',
  sign: '',
  level: 1,
  status: 1,
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error'}],
  sign: [{required: true, message: '编码不能为空', type: 'error'}],
  status: [{required: true, number: true, message: '是否有效不能为空', type: 'error'}],
  level: [{required: true, message: '职级不能为空', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiPosition.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiPosition.detail({
      id: id,
      success: (res: any) => {
        res.status = res.status ? res.status.code : 1
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
