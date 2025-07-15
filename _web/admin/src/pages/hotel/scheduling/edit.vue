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
        <div class="form-basic-container-title">批量排班</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="所属用户" name="userId" required-mark>
              <t-tag-input v-model="userList" clearable
                           placeholder="请输选择用户"
                           @clear="(e)=>{userList = [];e.e.stopPropagation();}"
                           @click="showSelectUser = true">
                <template #valueDisplay="{ value, onClose }">
                  <t-tag
                    v-for="(item, index) in value"
                    :key="item"
                    closable
                    @close="() => onClose(index)"
                  >
                    {{ item.name }}
                  </t-tag>
                </template>
              </t-tag-input>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="班次" name="timeScopeId">
              <t-input
                v-model="formData.timeScopeName"
                placeholder="请选择班次"
                :readonly="true"
                :show-clear-icon-on-empty="true"
                clearable
                @clear="(e)=>{formData.timeScopeName=null;formData.timeScopeId=null;e.e.stopPropagation()}"
                @click="showSelectTimeScope = true"
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="日期" name="date">
              <t-date-range-picker v-model="dateRange"
                                   :presets="presets"
                                   :disable-date="{before: dayjs().subtract(1, 'day').format()}"
                                   @change="changeDate"
              />
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


    <t-dialog v-model:visible="showSelectUser" header="选择用户" :footer="false" width="800"
              @cancel="showSelectUser = false">
      <user-select :mutilate="true"
                   v-if="showSelectUser"
                   :select-ids="userList.map(item => item.id)"
                   @cancel="showSelectUser = false"
                   @selected="onUserSelected"></user-select>
    </t-dialog>

    <t-dialog v-model:visible="showSelectTimeScope" header="选择班次" :footer="false" width="800"
              @cancel="showSelectTimeScope = false">
      <time-scope-select
        v-if="showSelectTimeScope"
        @cancel="showSelectTimeScope = false"
        @selected="onTimeScopeSelected"></time-scope-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">

export default {
  name: 'schedulingEdit',
};
</script>

<script setup lang="ts">
import type {DateRangePickerProps, SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiScheduling from '@/api/hotel/ApiScheduling';
import router from '@/router';
import UserSelect from "@/pages/user/user/select.vue";
import TimeScopeSelect from "@/pages/hotel/time-scope/select.vue";
import dayjs from "dayjs";

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();

const showSelectUser = ref(false);
const showSelectTimeScope = ref(false);
const userList = ref([]);

const dateRange = ref(['', '']);
const presets = ref<DateRangePickerProps['presets']>({
  "最近7天": [dayjs().toDate(), dayjs().add(6, 'day').toDate()],
  "最近3天": [dayjs().toDate(), dayjs().add(2, 'day').toDate()],
  "今天": [dayjs().toDate(), dayjs().toDate()],
});

// 表单
const formData = ref({
  id: null,
  userId: '',
  userIds: '',
  timeScopeId: '',
  timeScopeName: '',
  date: '',
  dateStart: '',
  dateEnd: '',
  sign: '',
});
const userIdValidator: any = (val: []) => {
  if (formData.value.userIds && formData.value.userIds.length > 0) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '请选择需要排班的用户', type: 'error'};
};

const dateValidator: any = (val: []) => {
  if (formData.value.dateStart && formData.value.dateEnd) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '请选择排班日期', type: 'error'};
};

// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  userId: [
    {validator: userIdValidator, trigger: 'change'},
  ],
  timeScopeId: [{required: true, message: '班次不能为空', type: 'error', trigger: 'change'}],
  date: [
    {validator: dateValidator, trigger: 'change'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiScheduling.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};

const changeDate = () => {
  formData.value.dateStart = dateRange.value[0]
  formData.value.dateEnd = dateRange.value[1]
}

const onUserSelected = (val: any) => {
  showSelectUser.value = false;
  userList.value = val
  formData.value.userIds = val.map((item: any) => item.id)
};

const onTimeScopeSelected = (val: any) => {
  showSelectTimeScope.value = false;
  formData.value.timeScopeId = val.id
  formData.value.timeScopeName = val.name
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiScheduling.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
