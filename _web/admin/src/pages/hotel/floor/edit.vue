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
        <div class="form-basic-container-title">编辑楼层</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
            </t-form-item>
          </t-col>


          <t-col :span="6">
            <t-form-item label="所属楼栋" name="buildingId">
              <t-input v-model="formData.buildingName" placeholder="所属楼栋" :maxlength="255"
                       clearable
                       @clear="(e) => {formData.buildingId = null;formData.buildingName=null;e.e.stopPropagation();}"
                       @focus="() => showSelectBuilding = true"/>
            </t-form-item>
          </t-col>


          <t-col :span="6">
            <t-form-item label="楼层" name="level">
              <t-input-number v-model="formData.level" placeholder="请输入楼层" :maxlength="10"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="外部key" name="extKey">
              <t-input v-model="formData.extKey" placeholder="请输入外部key" :maxlength="255"/>
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

    <t-dialog v-model:visible="showSelectBuilding" header="选择楼层" :footer="false" width="800">
      <building-select v-if="showSelectBuilding" @cancel="showSelectBuilding = false"
                       @selected="buildingSelected"></building-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">

export default {
  name: 'floorEdit',
};
</script>

<script setup lang="ts">
import buildingSelect from '@/pages/hotel/building/select.vue';
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiFloor from '@/api/hotel/ApiFloor';
import ApiBuilding from '@/api/hotel/ApiBuilding';
import router from '@/router';
import ApiHotel from "@/api/hotel/ApiHotel";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showSelectBuilding = ref(false);
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  name: '',
  level: 1,
  buildingId: '',
  buildingName: '',
  extKey: '',
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error', trigger: 'change'}],
  buildingId: [{required: true, message: '所属楼栋不能为空', type: 'error', trigger: 'change'}],
  level: [{required: true, number: true, message: '楼层不能为空', type: 'error', trigger: 'change'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiFloor.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};

const buildingSelected = (val: any) => {
  showSelectBuilding.value = false;
  formData.value.buildingId = val.id
  formData.value.buildingName = val.name
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id, buildingId} = route.query;
  if (id) {
    ApiFloor.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  } else if (buildingId) {
    ApiBuilding.detail({
      id: buildingId,
      success: (res: any) => {
        formData.value.buildingId = res.id;
        formData.value.buildingName = res.name;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
