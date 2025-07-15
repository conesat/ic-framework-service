<template>
  <t-loading :loading="loading">
    <t-card :bordered="false">
      <t-space direction="vertical" style="width: 100%;">
        <div style="width: 100%;text-align: center;font-size: 16px;font-weight: bold">{{ formData.title }}</div>
        <div style="width: 100%;text-align: center;margin-bottom: 20px;font-size: 12px;color: var(--td-text-color-secondary)">{{ formData.createTime }}</div>
        <div v-html="formData.content"></div>
      </t-space>
    </t-card>
  </t-loading>
</template>

<script lang="ts">

export default {
  name: 'noticeEdit',
};
</script>

<script setup lang="ts">
import {onMounted, provide, ref} from 'vue';
import {useRoute} from 'vue-router';
import ApiNotice from '@/api/sys/ApiNotice';
import ApiMineNotice from '@/api/sys/ApiMineNotice';

//引入富文本编辑器

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
const loading = ref(true);
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
// 定义变量 end -------------------

// 定义方法 start -------------------

// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    ApiNotice.detail({
      id: id,
      success:(res: any) => {
        res.status = res.status ? res.status.code : 0;
        res.noticeType = res.noticeType ? res.noticeType.code : 1;
        formData.value = res;
        loading.value = false;
        ApiMineNotice.read(id);
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
