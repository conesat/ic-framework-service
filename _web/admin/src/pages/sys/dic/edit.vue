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
        <div class="form-basic-container-title">编辑字典</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="键" name="dicKey">
              <t-input v-model="formData.dicKey" placeholder="请输入键" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="值" name="dicVal">
              <t-input v-model="formData.dicVal" placeholder="请输入值" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是否对外启用" name="status">
              <t-radio-group v-model="formData.status">
                <t-radio :value="1"> 启用</t-radio>
                <t-radio :value="0"> 禁用</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>

          <template v-if="formData.id != null">
            <t-col :span="6">
              <t-form-item label="是否系统属性" name="system">
                <t-radio-group v-model="formData.system" disabled>
                  <t-radio :value="true"> 是</t-radio>
                  <t-radio :value="false"> 否</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input v-model="formData.createTime" disabled placeholder="" :maxlength="255"/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input v-model="formData.updateTime" disabled placeholder="" :maxlength="255"/>
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
  name: 'BasicDicEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiDic from '@/api/sys/ApiDic';
import router from '@/router';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  dicKey: '',
  dicVal: '',
  status: 0,
  system: false,
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  dicKey: [{required: true, message: '请输入键值', type: 'error'}],
  status: [{required: true, message: '请选择是否启用', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
// eslint-disable-next-line @typescript-eslint/no-empty-function
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiDic.edit({
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
    ApiDic.detail({
      id: id,
      success: (res: any) => {
        res.status = res.status.code
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
