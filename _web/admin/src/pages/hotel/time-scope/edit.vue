<template>
  <div>
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
          <div class="form-basic-container-title">编辑时间表</div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[32, 24]">
            <t-col :span="6">
              <t-form-item label="班次名称" name="name">
                <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="所属部门【留空则所有部门通用】" name="deptId">
                <t-input
                  v-model="formData.deptName"
                  placeholder="请选择所属部门"
                  :readonly="true"
                  :show-clear-icon-on-empty="true"
                  clearable
                  @clear="(e)=>{formData.deptId=null;formData.deptName=null;e.e.stopPropagation()}"
                  @click="showSelectDep = true"
                />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="开始时间" name="startTime">
                <t-time-picker format="HH:mm" v-model="formData.startTime" placeholder="请选择开始时间"
                               style="width: 100%"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="结束时间" name="endTime">
                <t-time-picker format="HH:mm" v-model="formData.endTime" placeholder="请选择结束时间"
                               style="width: 100%"/>
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

    <t-dialog v-model:visible="showSelectDep" header="选择部门" :footer="false" width="600">
      <dep-select v-if="showSelectDep"
                  @selected="onDepSelected"
                  @cancel="showSelectDep = false"
      ></dep-select>
    </t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: 'timeScopeEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import router from '@/router';
import {closeOrBack} from "@/utils/url-utils";
import DepSelect from "@/pages/org/dept/select.vue";
import ApiTimeScope from "@/api/hotel/ApiTimeScope";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------

const showSelectDep = ref(false);
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  deptId: null,
  deptName: null,
  name: '',
  startTime: '',
  endTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '请输入部门名称', type: 'error'}],
  status: [{required: true, message: '状态必填', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
// eslint-disable-next-line @typescript-eslint/no-empty-function
const onReset = () => {
};

// 部门选择回调
const onDepSelected = (res: any) => {
  showSelectDep.value = false
  formData.value.deptId = res.id
  formData.value.deptName = res.name
}
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiTimeScope.edit({
      data: {
        ...formData.value,
        startTime: formData.value.startTime + ":00",
        endTime: formData.value.endTime + ":00"
      },
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
    ApiTimeScope.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
