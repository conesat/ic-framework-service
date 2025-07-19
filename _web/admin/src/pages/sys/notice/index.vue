<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">新建</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="handleClickDelete()"
          >删除
          </t-button
          >
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input">
          <t-input v-model="queryForm.searchKey" placeholder="查找标题或者所属用户" clearable @enter="getData(true)">
            <template #suffix-icon>
              <search-icon size="16px" @click.stop="getData(true)"/>
            </template>
          </t-input>
        </div>
      </t-row>
      <t-table
        :data="data"
        :columns="columns"
        row-key="id"
        vertical-align="top"
        :hover="true"
        :pagination="pagination"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
        :sort="sort"
        :multipleSort="true"
        @sort-change="sortChange"
        @change="reHandleChange"
        @select-change="reHandleSelectChange"
      >
        <template #noticeType="{ row }">
          <t-tag variant="light">{{ row.noticeType.text }}</t-tag>
        </template>
        <template #status="{ row }">
          <t-tag v-if='row.status' :theme="row.status.code === 1 ? 'success':'warning'" variant="light">{{
              row.status.code == 1 ? '已发布' : '未发布'
            }}
          </t-tag>
        </template>
        <template #op="{ row }">
          <t-space>
            <t-link hover="color" theme="primary" @click="handleClickEdit(row.id)">详情</t-link>
            <t-link hover="color" theme="warning" @click="handleClickDelete(row)">删除</t-link>
          </t-space>
        </template>
      </t-table>
    </t-card>

    <t-dialog v-model:visible="confirmDeleteVisible" header="确认" :body="confirmDeleteBody"
              @confirm="onConfirmDelete"/>
  </div>
</template>

<script lang="ts">
export default {
  name: 'noticeIndex',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin, PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';

import ApiNotice from '@/api/sys/ApiNotice';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import {queryDef, paginationDef} from "@/api/common/query";

// 定义变量 start -------------------
// 读取设置内容
const store = useSettingStore();
// 排序
const sort = ref([{
  // 按照 sort 字段进行排序
  sortBy: '',
  // 是否按照降序进行排序
  descending: false,
}]);
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef
});
// 数据是否加载中
const dataLoading = ref(false);
// 是否显示确认删除弹窗
const confirmDeleteVisible = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 路由
const router = useRouter();
// 表头
const columns: PrimaryTableCol[] = [
  {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
  {title: '标题', colKey: 'title'},
  {title: '是否发布', colKey: 'status'},
  {title: '类型', colKey: 'noticeType'},
  {title: '归属人', colKey: 'userName'},
  {title: '起始时间', colKey: 'enableTime'},
  {
    align: 'left',
    fixed: 'right',
    width: 120,
    colKey: 'op',
    title: '操作',
  },
];
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiNotice.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res) => {
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;
    }
  });
};
// 重置选中数据
const resetIdx = () => {
  selectedRowData.value = [];
  selectedRowKeys.value = [];
};
// 构建确认删除内容
const confirmDeleteBody = () => {
  if (selectedRowData.value.length > 0) {
    const names = [];
    for (let i = 0; i < selectedRowData.value.length && i < 3; i++) {
      names.push(selectedRowData.value[i].name);
    }
    return `确认删除 ${
      names.join(',') + (selectedRowData.value.length > names.length ? ` ...等 ${selectedRowData.value.length} 个数据吗？` : ' 吗？')
    }，删除后无法恢复`;
  }
  return '';
};
// 确认删除事件
const onConfirmDelete = () => {
  confirmDeleteVisible.value = false;
  // 真实业务请发起请求
  ApiNotice.delete({
    ids: selectedRowKeys.value,
    success: (res: any) => {
      MessagePlugin.success('删除成功');
      resetIdx();
      getData();
    }
  });
};
// 表单选中数据变化
const reHandleSelectChange = (val: (string | number)[], e: any) => {
  selectedRowKeys.value = val;
  selectedRowData.value = e.selectedRowData;
};
// 排序变化
const sortChange = (sortInfo: any) => {
  // 对于受控属性而言，这里的赋值很重要，不可缺少
  sort.value = sortInfo;
  queryForm.value.orders = JSON.stringify(sort.value)
};
// 表单参数变化 包括过滤、分页
const reHandleChange = (changeParams: any, triggerAndData: unknown) => {
  pagination.value = changeParams.pagination;
  getData();
};
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/sys/notice-edit?id=${id ? id : ''}`);
};
// 单个删除按钮事件
const handleClickDelete = (row?: any) => {
  if (row) {
    selectedRowKeys.value = [row.id];
    selectedRowData.value = [row];
  }
  if (selectedRowData.value.length > 0) {
    confirmDeleteVisible.value = true;
  } else {
    MessagePlugin.warning('请先选择数据');
  }
};
// 处理表单顶部距离
const headerAffixedTop = computed(
  () =>
    ({
      offsetTop: store.state.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    } as any),
);
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
</style>
