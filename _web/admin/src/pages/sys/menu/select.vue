<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <t-input auto-width v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable @enter="getData(true)">
          <template #suffix-icon>
            <search-icon size="16px" @click="getData(true)" />
          </template>
        </t-input>
      </t-row>

      <t-enhanced-table :tree="treeConfig" :tree-expand-and-fold-icon="treeExpandAndFoldIconRender" :data="data"
        :columns="columns" row-key="id" vertical-align="top" :hover="true" :selected-row-keys="selectedRowKeys"
        :loading="dataLoading" :header-affixed-top="headerAffixedTop" @change="reHandleChange"
        @select-change="reHandleSelectChange">
        <template #system="{ row }">
          <t-tag :theme="row.system ? 'success' : 'warning'" variant="light">{{ row.system ? '是' : '否' }}</t-tag>
        </template>
        <template #menuType="{ row }">
          <t-tag v-if="row.menuType" variant="light">{{ row.menuType.text }}</t-tag>
        </template>
        <template #menuPlatformType="{ row }">
          <t-tag v-if="row.menuPlatformType" variant="light">{{ row.menuPlatformType.text }}</t-tag>
        </template>
      </t-enhanced-table>
      <t-row justify="end">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" @click="selected" :disabled="selectedRowKeys.length === 0">确认</t-button>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="tsx">
export default {
  name: 'menuSelect',
};
</script>

<script setup lang="tsx">
import { ChevronDownIcon, ChevronRightIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { EnhancedTableProps, PrimaryTableCol } from 'tdesign-vue-next';
import { computed, onMounted, reactive, ref } from 'vue';

import { queryDef, paginationDef } from "@/api/common/query";
import ApiMenu from '@/api/sys/ApiMenu';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';

const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false
  },
});

// 定义变量 start -------------------
const treeConfig: EnhancedTableProps['tree'] = reactive({
  treeNodeColumnIndex: 1,
  indent: 25,
  expandTreeNodeOnClick: true,
});
const treeExpandAndFoldIconRender: EnhancedTableProps['treeExpandAndFoldIcon'] = (h, { type, row }) => {
  return type === 'expand' ? <ChevronRightIcon /> : <ChevronDownIcon />;
};
const emit = defineEmits(['selected', 'cancel']);
// 读取设置内容
const store = useSettingStore();
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ne: null,
  noParent: 1,
  searchKey: ''
});
// 数据是否加载中
const dataLoading = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 表头
const columns: PrimaryTableCol[] = [
  {
    colKey: 'row-select',
    type: props.mutilate ? 'multiple' : 'single',
    width: 64,
    fixed: 'left',
  },
  { title: '菜单名', colKey: 'name' },
  { title: 'icon', colKey: 'icon' },
  { title: '类型', colKey: 'menuType' },
  { title: '归属平台类型', colKey: 'menuPlatformType' },
  { title: '系统内置', colKey: 'system' },
];
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiMenu.all({
    data: queryForm.value,
    success: (res: any) => {
      data.value = res;
      dataLoading.value = false;
    }
  });
};
// 表单选中数据变化
const reHandleSelectChange = (val: (string | number)[], e: any) => {
  selectedRowKeys.value = val;
  // t-enhanced-table 有所不同需要获取row
  selectedRowData.value = e.selectedRowData.map((f: any) => f.row);
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
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped></style>
