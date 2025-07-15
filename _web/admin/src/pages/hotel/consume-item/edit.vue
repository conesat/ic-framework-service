<template>
  <t-form ref="form" class="base-form" :data="formData" :rules="FORM_RULES" label-align="top" :label-width="100"
    @reset="onReset" @submit="onSubmit">
    <div class="form-basic-container">
      <div class="form-basic-item">
        <div class="form-basic-container-title">编辑消费项目</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255" />
            </t-form-item>
            <t-form-item label="价格" name="price">
              <t-input-number v-model="formData.price" :min="0" placeholder="请输入价格" :maxlength="255" />
            </t-form-item>
            <t-form-item label="库存【-1代表无限】" name="inventory">
              <t-input-number v-model="formData.inventory" :min="-1" placeholder="请输入库存" :maxlength="255" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="图片" name="picFileId">
              <cropper style="border-radius: var(--td-radius-default)" :cover-width="400" :cover-height="400"
                :width="190" :height="190" :use-type="FileUseTypes.product"
                background-color="var(--td-bg-color-container-hover)" :url="formData.fileUrl" @uploaded="onUploaded"
                @clear="(e) => { formData.picFileUrl = null; formData.picFileId = null; e.e.stopPropagation() }"></cropper>
            </t-form-item>
          </t-col>

          <template v-if="formData.id">

            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255" disabled />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255" disabled />
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
  name: 'consumeItemEdit',
};
</script>

<script setup lang="ts">
import type { SubmitContext } from 'tdesign-vue-next';
import { FormRule, MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { closeOrBack } from "@/utils/url-utils";
import ApiConsumeItem from '@/api/hotel/ApiConsumeItem';
import router from '@/router';
import { FileUseTypes } from "@/constants/file-user-types";
import Cropper from "@/components/upload/cropper.vue";
import success from "@/pages/result/success/index.vue";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  name: '',
  price: '',
  inventory: '',
  createTime: '',
  updateTime: '',
  picFileUrl: '',
  fileUrl: '',
  fileId: '',
  picFileId: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{ required: true, message: '名称不能为空', type: 'error', trigger: 'change' }],
  price: [{ required: true, number: true, message: '价格不能为空', type: 'error', trigger: 'change' }],
  inventory: [{ required: true, number: true, message: '库存不能为空', type: 'error', trigger: 'change' }],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
const onUploaded = (res: any) => {
  formData.value.fileId = res.id
  formData.value.fileUrl = res.url
}
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiConsumeItem.edit({
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
  const { id } = route.query;
  if (id) {
    ApiConsumeItem.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
