<template>
  <div>
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
          <div class="form-basic-container-title">编辑部门</div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[32, 24]">
            <t-col :span="6">
              <t-form-item label="部门名称" name="name">
                <t-input v-model="formData.name" placeholder="请输入部门名称" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="负责人" name="leaderUserId">
                <t-input v-model="formData.leaderUserName" placeholder="请输入负责人" :maxlength="255"
                         clearable
                         @clear="clearUser"
                         @focus="showSelectUser = true"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="上级部门" name="parentId">
                <t-input
                  v-model="formData.parentName"
                  placeholder="请选择上级部门"
                  :readonly="true"
                  :show-clear-icon-on-empty="true"
                  clearable
                  @clear="clearParent"
                  @click="showSelectParent = true"
                />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="联系电话" name="phone">
                <t-input v-model="formData.phone" placeholder="请输入联系电话" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="排序" name="sort">
                <t-input-number v-model="formData.sort" type="number" placeholder="请输入排序"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="是否启用" name="status">
                <t-radio-group v-model="formData.status">
                  <t-radio :value="1"> 启用</t-radio>
                  <t-radio :value="0"> 禁用</t-radio>
                </t-radio-group>
              </t-form-item>
            </t-col>

            <t-col :span="6" v-if="formData.id">
              <t-form-item label="创建时间" name="createTime">
                <t-input v-model="formData.createTime" :disabled="true"/>
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

    <t-dialog v-model:visible="showSelectParent" header="选择上级部门" :footer="false" width="600">
      <dept-select v-if="showSelectParent" @cancel="showSelectParent = false"
                   @selected="parentSelected"></dept-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectUser" header="选择用户" :footer="false" width="800">
      <user-select v-if="showSelectUser" @cancel="showSelectUser = false" @selected="userSelected"></user-select>
    </t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: 'DeptEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiDept from '@/api/org/ApiDept';
import router from '@/router';

import deptSelect from './select.vue';
import userSelect from '@/pages/user/user/select.vue';
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
  parentName: '',
  parentId: '',
  parentPath: '',
  createTime: '',
  sort: 0,
  status: 1,
  leaderUserId: '',
  leaderUserName: '',
  phone: '',
});
const showSelectParent = ref(false);
const showSelectUser = ref(false);
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '请输入部门名称', type: 'error'}],
  status: [{required: true, message: '状态必填', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
const clearUser = (e: any) => {
  formData.value.leaderUserName = '';
  formData.value.leaderUserId = '';
  formData.value.phone = '';
  e.e.stopPropagation();
}
// 重置表单
// eslint-disable-next-line @typescript-eslint/no-empty-function
const onReset = () => {
  formData.value = {
    id: formData.value.id,
    name: '',
    parentName: '',
    parentId: '',
    parentPath: '',
    createTime: '',
    sort: 0,
    status: 1,
    leaderUserId: '',
    leaderUserName: '',
    phone: '',
  }
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiDept.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};

const parentSelected = (val: any) => {
  showSelectParent.value = false;
  formData.value.parentId = val.id;
  formData.value.parentName = val.name;
};
const userSelected = (val: any) => {
  showSelectUser.value = false;
  formData.value.leaderUserId = val.id
  formData.value.leaderUserName = val.name
  formData.value.phone = val.phone
};
const clearParent = (e: any) => {
  e.e.stopPropagation();
  formData.value.parentId = '';
  formData.value.parentName = '';
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiDept.detail({
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
