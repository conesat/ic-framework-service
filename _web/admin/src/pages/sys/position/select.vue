<template>
  <div ref="mainContainer">
    <t-card class="list-card-container" :bordered="false" :class="filter?'filter':''">
      <t-row justify="space-between">
        <t-input auto-width v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable
                 @enter="getData(true)">
          <template #suffix-icon>
            <search-icon size="16px" @click.stop="getData(true)"/>
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
        <template #empty>
          <t-space direction="vertical" style="text-align: center">
            <span>暂无数据</span>
            <t-button theme="default" @click="handleClickEdit">点击新建</t-button>
          </t-space>
        </template>
      </t-table>
      <t-row justify="end" v-if="!filter">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" @click="selected" :disabled="selectedRowKeys.length === 0">确认</t-button>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'positionSelect',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';

import ApiPosition from '@/api/sys/ApiPosition';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import {queryDef, paginationDef} from "@/api/common/query";

const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false
  },
  selectIds: {
    type: Array,
    default: []
  },
  value: {
    type: Array,
    default: []
  },
  filter: {
    type: Boolean,
    default: false
  }
});

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel', "change"]);
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
const mainContainer = ref(null);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
  status: 1,
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
  {title: '名称', colKey: 'name'},
  {title: '编码', colKey: 'sign'},
  {title: '职级', colKey: 'level'},
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
  ApiPosition.page({
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
  emit('change', e.selectedRowData);
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
const handleClickEdit = () => {
  window.open(`#/org/position-edit?autoClose=true`);
};

// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  if (props.selectIds && props.selectIds.length > 0) {
    selectedRowKeys.value = props.selectIds ?? [];
  } else if (props.value && props.value.length > 0) {
    selectedRowKeys.value = props.value.map((item: any) => item.id) ?? [];
  }
  getData();
});
</script>

<style lang="less" scoped>

.filter {
  width: 400px;
  padding: 0;

  /deep/ .t-table__content {
    overflow: hidden;
  }
}
</style>
