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
        <div class="form-basic-container-title">编辑酒店用户</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
            </t-form-item>

            <t-form-item label="手机号" name="phone">
              <t-input v-model="formData.phone" placeholder="请输入手机号" :maxlength="255"/>
            </t-form-item>
          </t-col>


          <t-col :span="6">
            <t-form-item label="头像" name="avatarFileId">
              <cropper
                style="border-radius: 50%"
                :cover-width="400"
                :cover-height="400"
                :use-type="FileUseTypes.avatar"
                :url="formData.avatarFileUrl"
                @uploaded="onAvatarUploaded"
              ></cropper>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="是否有效" name="status">
              <t-radio-group v-model="formData.status">
                <t-radio :value="1"> 启用</t-radio>
                <t-radio :value="0"> 禁用</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>


          <t-col :span="6">
            <t-form-item label="累计消费金额" name="totalConsume">
              <t-input-number :min="0" v-model="formData.totalConsume"
                              placeholder="请输入累计消费金额"></t-input-number>
            </t-form-item>
          </t-col>


          <t-col :span="6">
            <t-form-item label="是否vip" name="vip">
              <t-checkbox v-model="formData.vip" @change="initDate"></t-checkbox>
            </t-form-item>
          </t-col>

          <template v-if="formData.vip">
            <t-col :span="12">
              <t-form-item label="vip时间" name="vipTime">
                <t-date-range-picker v-model="vipDateTime" enable-time-picker allow-input clearable/>
              </t-form-item>
            </t-col>
          </template>

          <template v-if="formData.id != null">
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
  name: 'hotelUserEdit',
};
</script>

<script setup lang="ts">
import Cropper from '@/components/upload/cropper.vue';
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiHotelUser from '@/api/hotel/ApiHotelUser';
import router from '@/router';
import {FileUseTypes} from "@/constants/file-user-types";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const vipDateTime = ref([null, null])
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  name: '',
  phone: '',
  avatarFileId: '',
  avatarFileUrl: '',
  vip: false,
  status: 1,
  vipStartTime: '',
  vipEndTime: '',
  totalConsume: 0,
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '姓名不能为空', type: 'error'}],
  phone: [{required: true, message: '手机号不能为空', type: 'error'}],
  status: [{required: true, number: true, message: '状态不能为空', type: 'error'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    formData.value.vipStartTime = vipDateTime.value[0]
    formData.value.vipEndTime = vipDateTime.value[1]
    ApiHotelUser.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
const onAvatarUploaded = (res: any) => {
  formData.value.avatarFileId = res.id
  formData.value.avatarFileUrl = res.url
}
const initDate = () => {
  if (vipDateTime.value[0] == null) {
    vipDateTime.value = [new Date(), '']
  }
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiHotelUser.detail({
      id: id,
      success: (res: any) => {
        res.status = res.status.code
        formData.value = res;
        vipDateTime.value = [formData.value.vipStartTime, formData.value.vipEndTime]
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
