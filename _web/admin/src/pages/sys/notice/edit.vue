<template>
  <t-loading :loading="loading">
    <t-space direction="vertical" style="width: 100%">
      <t-form
        ref="form"
        class="base-form"
        :data="formData"
        :rules="FORM_RULES"
        :disabled="sourceStatus===1"
        label-align="top"
        :label-width="100"
        @keydown.enter.prevent
        @reset="onReset"
        @submit="onSubmit"
      >
        <div class="form-basic-container">
          <div class="form-basic-item">
            <div class="form-basic-container-title">{{ formData.status == 1 ? '公告详情' : '编辑通知' }}</div>
            <!-- 表单内容 -->
            <t-row class="row-gap" :gutter="[32, 24]">
              <t-col :span="6">
                <t-form-item label="标题" name="title">
                  <t-input v-model="formData.title" placeholder="请输入标题" :maxlength="255"/>
                </t-form-item>
              </t-col>
              <t-col :span="6">
                <t-form-item label="类型" name="noticeType">
                  <t-select v-model="formData.noticeType" placeholder="请选择类型">
                    <t-option :value="1" label="通知" :key="1"></t-option>
                    <t-option :value="2" label="公告" :key="2"></t-option>
                  </t-select>
                </t-form-item>
              </t-col>

              <t-col :span="6">
                <t-form-item label="生效时间" name="enableTime">
                  <t-date-picker style="width: 100%" enable-time-picker allow-input clearable
                                 v-model="formData.enableTime"/>
                </t-form-item>
              </t-col>

              <t-col :span="6">
                <t-form-item label="发布状态" name="status" v-if="sourceStatus === 0">
                  <t-select v-model="formData.status" placeholder="请选择发布状态">
                    <t-option :value="1" label="立即发布" :key="1"></t-option>
                    <t-option :value="0" label="存草稿" :key="2"></t-option>
                  </t-select>
                </t-form-item>
              </t-col>
              <template v-if="formData.id">
                <t-col :span="6">
                  <t-form-item label="创建时间" name="createTime">
                    <t-input :value="formData.createTime" placeholder="" disabled/>
                  </t-form-item>
                </t-col>

                <t-col :span="6">
                  <t-form-item label="更新时间" name="updateTime">
                    <t-input :value="formData.updateTime" placeholder="" disabled/>
                  </t-form-item>
                </t-col>
              </template>

              <t-col :span="12">
                <t-form-item label="内容" name="content">
                  <quill-editor style="max-height: 500px" v-if="sourceStatus === 0"
                                v-model="formData.content"></quill-editor>
                  <div v-else v-html="formData.content"></div>
                </t-form-item>
              </t-col>
              <t-col :span="12" v-if="sourceStatus === 0 && formData.status === 1">
                <t-form-item label="定向发布【如果都不勾选，则默认为全部】" name="">
                  <send-tab ref="sendTab"></send-tab>
                </t-form-item>
              </t-col>
            </t-row>
          </div>
        </div>

        <div class="form-submit-container" v-if="!sourceStatus">
          <div class="form-submit-sub">
            <div class="form-submit-left">
              <t-button theme="primary" class="form-submit-confirm" type="submit">
                {{ formData.status === 1 ? '立即发布' : '保存草稿' }}
              </t-button>
              <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 重置</t-button>
            </div>
          </div>
        </div>
      </t-form>

      <t-card v-if="formData.id" :bordered="false">
        <t-tabs default-value="notice-receiver">
          <t-tab-panel value="notice-receiver" label="通知用户列表">
            <notice-receiver-table v-if="formData.id"
                                   :notice-id="formData.id"
            ></notice-receiver-table>
          </t-tab-panel>
        </t-tabs>
      </t-card>
    </t-space>
  </t-loading>
</template>

<script lang="ts">

export default {
  name: 'noticeEdit',
};
</script>

<script setup lang="ts">
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, provide, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiNotice from '@/api/sys/ApiNotice';
import QuillEditor from '@/components/quill/quill-editor.vue';
import router from '@/router';
import SendTab from "./send-tab.vue";
import NoticeReceiverTable from "@/pages/sys/notice-receiver/table.vue";
//引入富文本编辑器

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
const loading = ref(true);
const sendTab = ref(null);
const form = ref(null);
const sourceStatus = ref(0);
// 表单
const formData = ref({
  id: null,
  title: '',
  content: '',
  status: 0,
  noticeType: 1,
  userId: 0,
  enableTime: '',
  receiveUserIds: [],
  receiveDepIds: [],
  receivePosIds: [],
  receiveRoleIds: [],
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  title: [{required: true, message: '标题不能为空', trigger: 'blur'}],
  content: [{required: true, message: '内容不能为空', trigger: 'change'}],
  noticeType: [{required: true, number: true, message: '类型不能为空', trigger: 'blur'}],
  status: [{required: true, number: true, message: '状态不能为空', trigger: 'blur'}],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};

// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    const res = sendTab.value.getData();
    formData.value.receiveUserIds = res.receiveUsers.map((f: any) => f.id);
    formData.value.receiveDepIds = res.receiveDeps.map((f: any) => f.id);
    formData.value.receivePosIds = res.receivePos.map((f: any) => f.id);
    formData.value.receiveRoleIds = res.receiveRoles.map((f: any) => f.id);
    ApiNotice.edit({
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
  const {id} = route.query;
  if (id) {
    ApiNotice.detail({
      id: id,
      success: (res: any) => {
        res.status = res.status ? res.status.code : 0;
        sourceStatus.value = res.status
        res.noticeType = res.noticeType ? res.noticeType.code : 1;
        formData.value = res;
        loading.value = false;
      }
    });
  } else {
    loading.value = false;
  }
});

</script>

<style lang="less" scoped>

/deep/ .send-to-tabs {
  .t-tabs__header.t-is-top {
    display: none;
  }
}

.empty {
  width: 100%;
  font-size: var(--td-font-size-title-small);
  text-align: center;
  color: var(--td-text-color-secondary);
}
</style>
