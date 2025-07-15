<template>
  <t-space direction="vertical" style="width: 100%">
    <t-form
      ref="form"
      class="base-form"
      :data="formData"
      :rules="FORM_RULES"
      label-align="top"
      :label-width="100"
      :disabled="formData.system"
      @keydown.enter.prevent
      @reset="onReset"
      @submit="onSubmit"
    >
      <div class="form-basic-container">
        <div class="form-basic-item">
          <div class="form-basic-container-title">
            <span style="margin-right: 10px">编辑角色</span>
            <t-tag theme="warning" variant="light" v-if="formData.system">系统角色不可编辑</t-tag>
          </div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[32, 24]">
            <t-col :span="6">
              <t-form-item label="名称" name="name">
                <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="英文标识" name="sign">
                <t-input v-model="formData.sign" placeholder="请输入英文标识，如：manager" :maxlength="255" />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="可授予用户类型" name="userType">
                <t-select v-model="formData.userType">
                  <t-option v-for="item in userTypes" :key="item.code" :label="item.name" :value="item.code" />
                </t-select>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="状态" name="status">
                <t-radio-group v-model="formData.status">
                  <t-radio :value="1">有效</t-radio>
                  <t-radio :value="0">无效</t-radio>
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
            <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 重置</t-button>
          </div>
        </div>
      </div>
    </t-form>

    <t-card v-if="formData.id" :bordered="false">
      <t-tabs default-value="role-promission">
        <t-tab-panel value="role-promission" label="权限">
          <role-permission-table
            v-if="formData.id"
            :role-id="formData.id"
            :role-name="formData.name"
            :user-type="formData.userType"
            :disable="formData.system || !formData.status"
            :select-disabled="!formData.id || formData.system"
          ></role-permission-table>
        </t-tab-panel>
        <t-tab-panel value="role-menu" label="菜单">
          <role-menu-table
            :role-id="formData.id"
            :select-disabled="!formData.id || formData.system"
            :user-type="formData.userType"
          ></role-menu-table>
        </t-tab-panel>
      </t-tabs>
    </t-card>
  </t-space>
</template>

<script lang="ts">
export default {
  name: 'roleEdit',
};
</script>

<script setup lang="ts">
import type { SubmitContext } from 'tdesign-vue-next';
import { FormRule, MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import ApiRole from '@/api/role/ApiRole';
import ApiSetting from '@/api/sys/ApiSetting';
import RolePermissionTable from '@/pages/role/rp/table.vue';
import RoleMenuTable from '@/pages/role/role-menu/table.vue';
import router from '@/router';
import { closeOrBack } from '@/utils/url-utils';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 用户类型列表
const userTypes = ref([]);
// 表单
const formData = ref({
  id: undefined,
  name: '',
  status: 1,
  system: false,
  createTime: '',
  sign: '',
  userType: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{ required: true, message: '名称不能为空', type: 'error' }],
  sign: [{ required: true, message: '英文标识不能为空', type: 'error' }],
  userType: [{ required: true, message: '用户类型不能为空', type: 'error' }],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiRole.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router);
      },
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  ApiSetting.userTypes({
    success: (res: any) => {
      userTypes.value = res;
    },
  });
  const { id } = route.query;
  if (id) {
    ApiRole.detail({
      id: id,
      success: (res: any) => {
        res.status = res.status ? res.status.code : 1;
        formData.value = res;
      },
    });
  }
});
</script>

<style lang="less" scoped></style>
