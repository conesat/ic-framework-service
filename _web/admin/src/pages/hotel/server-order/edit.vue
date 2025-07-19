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
        <div class="form-basic-container-title">编辑服务订单</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="接单人员" name="serverUserId">
              <t-input
                v-model="formData.serverUserName"
                placeholder="请选择服务人员"
                :readonly="true"
                :show-clear-icon-on-empty="true"
                clearable
                @clear="
                  (e) => {
                    formData.serverUserName = null;
                    formData.serverUserId = null;
                    e.e.stopPropagation();
                  }
                "
                @click="showSelectUser = true"
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="服务状态" name="state">
              <t-select v-model="formData.state">
                <t-option v-for="item in stateEnums" :key="item.code" :label="item.text" :value="item.code" />
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="服务类型" name="target">
              <t-select v-model="formData.target">
                <t-option v-for="item in targetEnums" :key="item.code" :label="item.text" :value="item.code" />
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6" v-if="formData.targetId">
            <t-form-item :label="formData.target === 1 ? '房间' : '订单'" name="targetId">
              <t-input :value="formData.targetContent" readonly @click="toOrder()" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="接单时间" name="acceptTime">
              <t-date-picker
                style="width: 100%"
                v-model="formData.finishTime"
                clearable
                placeholder="请选择接单时间"
                enableTimePicker
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="期望完成时间" name="doTime">
              <t-date-picker
                style="width: 100%"
                v-model="formData.doTime"
                clearable
                placeholder="请选择期望完成时间"
                enableTimePicker
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="完成时间" name="finishTime">
              <t-date-picker
                style="width: 100%"
                v-model="formData.finishTime"
                clearable
                placeholder="请选择完成时间"
                enableTimePicker
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="创建时间" name="createTime">
              <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255" disabled />
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

    <t-dialog
      v-model:visible="showSelectUser"
      header="选择用户"
      :footer="false"
      width="800"
      @cancel="showSelectUser = false"
    >
      <user-select
        v-if="showSelectUser"
        @cancel="showSelectUser = false"
        @selected="onUserSelected"
      ></user-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">
export default {
  name: 'serverOrderEdit',
};
</script>

<script setup lang="ts">
import type { SubmitContext } from 'tdesign-vue-next';
import { FormRule, MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { closeOrBack } from '@/utils/url-utils';
import ApiServerOrder from '@/api/hotel/ApiServerOrder';
import router from '@/router';
import UserSelect from '@/pages/sys/user/select.vue';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showSelectUser = ref(false);
const stateEnums = ref([]);
const targetEnums = ref([]);
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  serverUserId: '',
  serverUserName: '',
  targetContent: '',
  state: '',
  target: 0,
  targetId: null,
  roomOrderId: null,
  createTime: '',
  finishTime: '',
  acceptTime: '',
  doTime: '',
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
    ApiServerOrder.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router);
      },
    });
  }
};

const onUserSelected = (val: any) => {
  showSelectUser.value = false;
  formData.value.serverUserId = val.id;
  formData.value.serverUserName = val.name;
};

const toOrder = () => {
  if (formData.value.target === 1) {
    window.open(`#/hotel/room-state?id=${formData.value.targetId}`);
  } else {
    window.open(`#/hotel/room-order-edit?id=${formData.value.roomOrderId}`);
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const { id } = route.query;
  if (id) {
    ApiServerOrder.detail({
      id: id,
      success: (res: any) => {
        res.state = res.state ? res.state.code : 0;
        res.target = res.target ? res.target.code : 0;
        formData.value = res;
      },
    });
  }
  ApiServerOrder.stateEnums({
    success: (res: any) => {
      stateEnums.value = res;
    },
  });
  ApiServerOrder.targetEnums({
    success: (res: any) => {
      targetEnums.value = res;
    },
  });
});
</script>

<style lang="less" scoped></style>
