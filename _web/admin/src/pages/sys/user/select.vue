<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-space>
        <t-input v-model="queryForm.searchKey" auto-width placeholder="请输入你需要搜索的内容" clearable
                 @enter="getData(true)">
          <template #suffix-icon>
            <search-icon size="16px" @click.stop="getData(true)"/>
          </template>
        </t-input>
      </t-space>

      <t-table
        :data="data"
        :columns="columns"
        row-key="id"
        vertical-align="top"
        :hover="true"
        :filter-value="filterValue"
        :pagination="pagination"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
        @change="reHandleChange"
        @select-change="reHandleSelectChange"
        @filter-change="onFilterChange"
      >
        <template #filterRow>
          <span v-for="key in Object.keys(filterValue)">
            {{ filterLabelMap[key as keyof typeof filterLabelMap] }}：[{{ filterValue[key].map((item:any) => item.name).join(',') }}]；
          </span>
          <t-button @click="clearFilter" variant="text" theme="primary">清空筛选</t-button>
        </template>

        <template #empty>
          <t-space direction="vertical" style="text-align: center">
            <span>暂无数据</span>
            <t-button theme="default" @click="handleClickEdit">点击新建</t-button>
          </t-space>
        </template>
        <template #name="{ row }">
          <t-link @click.stop="handleClickEdit(row.id)">{{ row.name }}</t-link>
        </template>
        <template #status="{ row }">
          <t-tag v-if="row.status" :theme="row.status.code === 1 ? 'success':'warning'" variant="light">
            {{ row.status.text }}
          </t-tag>
        </template>
        <template #deps="{ row }">
          <t-tag v-for="item in row.deps" style="margin-right: 4px">
            {{ item.name }}
          </t-tag>
        </template>
        <template #roles="{ row }">
          <t-tag v-for="item in row.roles" style="margin-right: 4px">
            {{ item.name }}
          </t-tag>
        </template>
        <template #pos="{ row }">
          <t-tag v-for="item in row.pos" style="margin-right: 4px">
            {{ item.name }}
          </t-tag>
        </template>
      </t-table>
      <t-row justify="end">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" :disabled="selectedRowKeys.length === 0" @click="selected">确认</t-button>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'ManagerIndex',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';

import apiUser from '@/api/sys/ApiUser';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import DepSelect from "@/pages/sys/dept/select.vue";
import PosSelect from "@/pages/sys/position/select.vue";
import RoleSelect from "@/pages/sys/role/select.vue";
import {queryDef, paginationDef} from "@/api/common/query";


const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false,
  },
  notInDepId: {
    type: Number,
    default: null,
  },
  notInPosId: {
    type: Number,
    default: null,
  },
  notInNoticeId: {
    type: String,
    default: null,
  },
  posId: {
    type: Number,
    default: null,
  },
  depId: {
    type: Number,
    default: null,
  },
  depName: {
    type: String,
    default: null,
  },
  posName: {
    type: String,
    default: null,
  },
  selectIds: {
    type: Array,
    default: null
  },
  posSigns: {
    type: Array,
    default: null
  },
});

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel']);
// 读取设置内容
const store = useSettingStore();
// 列表数据
const data = ref([]);
const filterValue:any = ref({});
const filterLabelMap = ref({
  deps: '部门',
  roles: '角色',
  pos: '岗位',
});
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
  notInDepId: 0,
  notInPosId: 0,
  notInNoticeId: '',
  posSigns: [],
  depIds: null,
  posIds: null,
  roleIds: null,
});
// 数据是否加载中
const dataLoading = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 路由
const router = useRouter();
// 表头
const columns: PrimaryTableCol[] = [
  {colKey: 'row-select', type: props.mutilate ? 'multiple' : 'single', width: 64, fixed: 'left'},
  {
    title: '姓名',
    colKey: 'name',
    fixed: 'left',
    sorter: true
  },
  {
    title: '账号',
    colKey: 'username',
    sorter: true
  },
  {
    title: '部门',
    colKey: 'deps',
    ellipsis: true,
    filter: {
      showConfirmAndReset: true,
      component: DepSelect,
      props: {
        filter: true,
        mutilate: true,
      },
    },
  },
  {
    title: '角色',
    colKey: 'roles',
    ellipsis: true,
    filter: {
      showConfirmAndReset: true,
      component: RoleSelect,
      props: {
        filter: true,
        mutilate: true,
      },
    },
  },
  {
    title: '岗位',
    colKey: 'pos',
    ellipsis: true,
    filter: props.posSigns ? null : {
      showConfirmAndReset: true,
      component: PosSelect,
      props: {
        filter: true,
        mutilate: true,
      },
    },
  },
  {
    title: '状态',
    width: 80,
    colKey: 'status',
  },
];
// 排序
const sort = ref([{
  // 按照 name 字段进行排序
  sortBy: 'username',
  // 是否按照降序进行排序
  descending: true,
}]);
// 定义变量 end -------------------

// 定义方法 start -------------------
const handleClickEdit = (id?: any) => {
  if (id) {
    window.open(`#/user/user-edit?id=${id}`);
  } else {
    window.open(`#/user/user-edit?autoClose=true&depId=${props.depId ?? ''}&depName=${props.depName ?? ''}&posId=${props.posId ?? ''}&posName=${props.posName ?? ''}`);
  }
};

// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
    data.value = [];
  }
  dataLoading.value = true;
  queryForm.value.orders = JSON.stringify(sort.value)
  queryForm.value.notInDepId = props.notInDepId;
  queryForm.value.notInPosId = props.notInPosId;
  queryForm.value.notInNoticeId = props.notInNoticeId;
  queryForm.value.posSigns = props.posSigns;
  queryForm.value.depIds = filterValue.value['deps'] ? filterValue.value['deps'].map((item: any) => item.id) : null;
  queryForm.value.posIds = filterValue.value['pos'] ? filterValue.value['pos'].map((item: any) => item.id) : null;
  queryForm.value.roleIds = filterValue.value['roles'] ? filterValue.value['roles'].map((item: any) => item.id) : null;
  apiUser.page({
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

const onFilterChange = (filter: any) => {
  filterValue.value = filter
  getData()
}

// 表单参数变化 包括过滤、分页
const reHandleChange = (changeParams: any, triggerAndData: unknown) => {
  pagination.value = changeParams.pagination;
  getData();
};

const clearFilter = () => {
  filterValue.value = {}
  getData()
}
// 取消选择
const cancel = () => {
  emit('cancel');
};
// 确认选择
const selected = () => {
  emit('selected', props.mutilate ? selectedRowData.value : selectedRowData.value[0]);
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
  selectedRowKeys.value = props.selectIds ?? [];
  getData();
});
</script>

<style lang="less" scoped></style>
