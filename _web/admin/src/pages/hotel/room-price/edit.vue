<template>
  <t-form
    ref="form"
    class="base-form"
    :data="formData"
    :rules="FORM_RULES"
    label-align="top"
    :label-width="100"
    @reset="onReset"
    @submit="onSubmit"
  >
    <div class="form-basic-container">
      <div class="form-basic-item">
        <div class="form-basic-container-title">编辑房价</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="门市价" name="price">
              <t-input v-model="formData.price" placeholder="请输入门市价" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="日期" name="date">
              <t-input v-model="formData.date" placeholder="请输入日期" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="创建时间" name="createTime">
              <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="更新时间" name="updateTime">
              <t-input v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255"/>
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
  name: 'roomPriceEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiRoomPrice from '@/api/hotel/ApiRoomPrice';
import router from '@/router';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  price: '',
  date: '',
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiRoomPrice.edit({
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
    ApiRoomPrice.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
