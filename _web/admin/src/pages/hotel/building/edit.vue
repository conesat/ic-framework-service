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
        <div class="form-basic-container-title">编辑楼栋</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="所属酒店" name="hotelId">
              <t-input v-model="formData.hotelName" placeholder="请输入所属酒店" :maxlength="255"
                       clearable
                       @clear="(e) => {formData.hotelId = null;formData.hotelName=null;e.e.stopPropagation();}"
                       @focus="() => showSelectHotel = true"/>
            </t-form-item>
          </t-col>

          <template v-if="formData.id">

            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255" disabled/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255" disabled/>
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

    <t-dialog v-model:visible="showSelectHotel" header="选择酒店" :footer="false" width="800">
      <hotel-select v-if="showSelectHotel" @cancel="showSelectHotel = false"
                    @selected="hotelSelected"></hotel-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">

export default {
  name: 'buildingEdit',
};
</script>

<script setup lang="ts">
import hotelSelect from '@/pages/hotel/hotel/select.vue';
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiBuilding from '@/api/hotel/ApiBuilding';
import ApiHotel from '@/api/hotel/ApiHotel';
import router from '@/router';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showSelectHotel = ref(false);
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  name: '',
  hotelId: '',
  hotelName: '',
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error', trigger: 'change'}],
  hotelId: [{required: true, message: '所属酒店不能为空', type: 'error', trigger: 'change'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiBuilding.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
const hotelSelected = (val: any) => {
  showSelectHotel.value = false;
  formData.value.hotelId = val.id
  formData.value.hotelName = val.name
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id, hotelId} = route.query;
  if (id) {
    ApiBuilding.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  } else if (hotelId) {
    ApiHotel.detail({
      id: hotelId,
      success: (res: any) => {
        formData.value.hotelId = res.id;
        formData.value.hotelName = res.name;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
