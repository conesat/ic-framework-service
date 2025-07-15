<template>
  <t-form
    style="margin: 10px 20px"
    ref="form"
    class="base-form"
    :data="formData"
    :rules="FORM_RULES"
    label-align="top"
    :label-width="100"
    @reset="onReset"
    @submit="onSubmit"
  >
    <!-- 表单内容 -->
    <t-row class="row-gap" :gutter="[32, 24]" style="width: 100%">

      <t-col :span="6">
        <t-form-item label="消费项目" name="customerItemId">
          <t-input v-model="formData.customerItemName" placeholder="请选择消费项目" clearable
                   @clear="(e) => {formData.customerItemId = null;formData.customerItemName=null;e.e.stopPropagation();}"
                   @focus="() => showSelectConsumeItemType = true"/>
        </t-form-item>
      </t-col>

      <t-col :span="6">
        <t-form-item label="数量" name="num">
          <t-input-number :min="1" v-model="formData.num" placeholder="请输入数量" @change="changeNum"/>
        </t-form-item>
      </t-col>

      <t-col :span="6">
        <t-form-item label="总额（元）" name="price">
          <t-input v-model="formData.price" placeholder="总额" :maxlength="255" readonly/>
        </t-form-item>
      </t-col>

      <t-col :span="6">
        <t-form-item label="状态" name="status">
          <t-radio-group v-model="formData.status">
            <t-radio :value="1"> 已支付</t-radio>
            <t-radio :value="0"> 未支付</t-radio>
          </t-radio-group>
        </t-form-item>
      </t-col>

    </t-row>

    <div style="margin-top: 40px">
      <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
      <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 重置</t-button>
    </div>

    <t-dialog v-model:visible="showSelectConsumeItemType" header="选择项目" :footer="false" width="800">
      <consume-select v-if="showSelectConsumeItemType" @cancel="showSelectConsumeItemType = false"
                      @selected="consumeItemSelected"/>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">

export default {
  name: 'roomConsumeOrderEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import ApiRoomConsumeOrder from '@/api/hotel/ApiRoomConsumeOrder';
import ConsumeSelect from '@/pages/hotel/consume-item/select.vue';

const emit = defineEmits(['success']);

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const props = defineProps({
  roomOrderId: {
    type: String,
    required: true
  },
  id: {
    type: String
  }
});

// 路由
const route = useRoute();
const showSelectConsumeItemType = ref(false)
// 表单
const formData = ref({
  id: null,
  roomOrderId: '',
  customerItemId: '',
  customerItemName: '',
  customerItemPrice: 0,
  price: 0,
  num: 1,
  status: 0,
  payTime: '',
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  customerItemId: [{required: true, message: '项目不能为空', type: 'error', trigger: 'change'}],
  num: [{required: true, number: true, message: '数量不能为空', type: 'error', trigger: 'change'}],
  status: [{required: true, number: true, message: '状态不能为空', type: 'error', trigger: 'change'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
const consumeItemSelected = (val: any) => {
  showSelectConsumeItemType.value = false
  formData.value.customerItemId = val.id
  formData.value.customerItemName = val.name
  formData.value.customerItemPrice = val.price
  changeNum()
};

const changeNum = () => {
  formData.value.price = formData.value.customerItemPrice * formData.value.num
}

// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    formData.value.roomOrderId = props.roomOrderId
    ApiRoomConsumeOrder.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        emit('success')
      }
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  if (props.id) {
    ApiRoomConsumeOrder.detail({
      id: props.id,
      success: (res: any) => {
        res.status = res.status ? res.status.code : 0
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
