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
        <div class="form-basic-container-title">编辑配套设施</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="配套设施类型" name="supportingType">
              <t-select v-model="formData.supportingType" placeholder="请选择类型">
                <t-option :value="item.code" :label="item.text" :key="item.code" v-for="item in types"></t-option>
              </t-select>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="图标" name="iconFileId">
              <cropper
                style="border-radius: var(--td-radius-default)"
                :cover-width="128"
                :cover-height="128"
                :use-type="FileUseTypes.supportingTypeIcon"
                background-color="var(--td-bg-color-container-hover)"
                :url="formData.iconFileUrl"
                @uploaded="onUploaded"
                @clear="(e)=>{formData.iconFileUrl=null;formData.iconFileId=null;e.e.stopPropagation()}"
              ></cropper>
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
  name: 'supportingEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiSupporting from '@/api/hotel/ApiSupporting';
import router from '@/router';
import {FileUseTypes} from "@/constants/file-user-types";
import Cropper from "@/components/upload/cropper.vue";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 类型
const types = ref([])
// 表单
const formData = ref({
  id: null,
  name: '',
  iconFileId: '',
  iconFileUrl: '',
  supportingType: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error', trigger: 'change'}],
  supportingType: [{required: true, number: true, message: '类型不能为空', type: 'error', trigger: 'change'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiSupporting.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
const onUploaded = (res: any) => {
  formData.value.iconFileId = res.id
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiSupporting.detail({
      id: id,
      success: (res: any) => {
        res.supportingType = res.supportingType ? res.supportingType.code : null
        formData.value = res;
      }
    });
  }
  ApiSupporting.types({
    success: (res: any) => {
      types.value = res
    }
  })
});
</script>

<style lang="less" scoped></style>
