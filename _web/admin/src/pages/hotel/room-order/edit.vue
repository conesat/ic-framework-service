<template>
  <t-space direction="vertical">
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
          <div class="form-basic-container-title">编辑房间订单</div>
          <!-- 表单内容 -->
          <t-row class="row-gap" :gutter="[32, 24]">
            <t-col :span="6">
              <t-form-item label="关联用户" name="userId">
                <t-input
                  v-model="formData.userName"
                  placeholder="请选择关联用户"
                  :readonly="true"
                  :show-clear-icon-on-empty="true"
                  clearable
                  @clear="(e)=>{formData.userName=null;formData.userId=null;e.e.stopPropagation()}"
                  @click="showSelectHotelUser = true"
                />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="客户姓名" name="customerName">
                <t-input v-model="formData.customerName" placeholder="请输入客户姓名" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="客户电话" name="customerPhone">
                <t-input v-model="formData.customerPhone" placeholder="请输入客户电话" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="房间号" name="roomNo">
                <t-input v-model="formData.roomNo" placeholder="请输入房间号" :maxlength="255" readonly @click="toRoom"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="房间价格" name="roomPrice">
                <t-input v-model="formData.roomPrice" placeholder="请输入房间价格" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="实付价格" name="actualRoomPrice">
                <t-input v-model="formData.actualRoomPrice" placeholder="请输入实付价格" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="入住时间" name="inDate">
                <t-input v-model="formData.inDate" placeholder="请输入入住时间" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="离店时间" name="outDate">
                <t-input v-model="formData.outDate" placeholder="请输入离店时间" :maxlength="255"/>
              </t-form-item>
            </t-col>

            <template v-if="formData.id">
              <t-col :span="6">
                <t-form-item label="创建时间" name="createTime">
                  <t-input v-model="formData.createTime" disabled placeholder="请输入创建时间" :maxlength="255"/>
                </t-form-item>
              </t-col>

              <t-col :span="6">
                <t-form-item label="更新时间" name="updateTime">
                  <t-input v-model="formData.updateTime" disabled placeholder="请输入更新时间" :maxlength="255"/>
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


      <t-dialog v-model:visible="showSelectHotelUser" header="选择酒店用户" :footer="false" width="800"
                @cancel="showSelectHotelUser = false">
        <hotel-select
          v-if="showSelectHotelUser"
          @cancel="showSelectHotelUser = false"
          @selected="onHotelUserSelected"></hotel-select>
      </t-dialog>
    </t-form>

    <t-card v-if="formData.id" :bordered="false">
      <t-tabs default-value="consume-order">
        <t-tab-panel value="consume-order" label="消费内容">
          <room-consume-order-table v-if="formData.id"
                                    :room-order-id="formData.id"
          ></room-consume-order-table>
        </t-tab-panel>
      </t-tabs>
    </t-card>
  </t-space>
</template>

<script lang="ts">

export default {
  name: 'roomOrderEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiRoomOrder from '@/api/hotel/ApiRoomOrder';
import router from '@/router';
import HotelSelect from "@/pages/hotel/hotel-user/select.vue";
import RoomConsumeOrderTable from "@/pages/hotel/room-consume-order/table.vue";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();

const showSelectHotelUser = ref(false);
// 表单
const formData = ref({
  id: null,
  userId: '',
  userName: '',
  customerName: '',
  customerPhone: '',
  roomPrice: '',
  actualRoomPrice: '',
  inDate: '',
  outDate: '',
  roomId: '',
  roomNo: '',
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
    ApiRoomOrder.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
const onHotelUserSelected = (val: any) => {
  showSelectHotelUser.value = false;
  formData.value.userId = val.id
  formData.value.userName = val.name
};

const toRoom = () => {
  window.open(`#/hotel/room-state?id=${formData.value.roomId}`)
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiRoomOrder.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
