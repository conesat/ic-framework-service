<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <t-input auto-width v-model="queryForm.searchKey" placeholder="查找标题或者所属用户" clearable @enter="getData(true)">
          <template #suffix-icon>
            <search-icon size="16px" @click="getData(true)"/>
          </template>
        </t-input>
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
        <template #empty>
          <t-space direction="vertical" style="text-align: center">
            <span>暂无数据</span>
            <t-button theme="default" @click="handleClickEdit">点击新建</t-button>
          </t-space>
        </template>
      </t-table>
      <t-row justify="end">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" @click="selected" :disabled="selectedRowKeys.length === 0">确认</t-button>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'noticeSelect',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';

import ApiNotice from '@/api/sys/ApiNotice';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import {queryDef, paginationDef} from "@/api/common/query";

const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false
  }
});

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel']);
// 读取设置内容
const store = useSettingStore();
// 排序
const sort = ref([{
  // 按照 sort 字段进行排序
  sortBy: 'sort',
  // 是否按照降序进行排序
  descending: false,
}]);
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
});
// 数据是否加载中
const dataLoading = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 表头
const columns: PrimaryTableCol[] = [
  {colKey: 'row-select', type: props.mutilate ? 'multiple' : 'single', width: 64, fixed: 'left'},
  {title: '标题', colKey: 'title'},
  {title: '是与否有效', colKey: 'status'},
  {title: '类型', colKey: 'noticeType'},
  {title: '归属人', colKey: 'userName'},
  {title: '起始时间', colKey: 'enableTime'},
];
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
    data.value = [];
  }
  dataLoading.value = true;
  ApiNotice.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;
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
// 取消选择
const cancel = () => {
  emit('cancel');
}
// 确认选择
const selected = () => {
  emit('selected', props.mutilate ? selectedRowData.value : selectedRowData.value[0]);
}
// 处理表单顶部距离
const headerAffixedTop = computed(
  () =>
    ({
      offsetTop: store.state.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    } as any),
);
// 新建数据
const handleClickEdit = () => {
  window.open(`#/sys/notice-edit?autoClose=true`);
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>

</style>
