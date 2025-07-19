<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container"></div>
        <div class="search-input">
          <t-input v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable @enter="search">
            <template #suffix-icon>
              <search-icon size="16px"></search-icon>
            </template>
          </t-input>
        </div>
      </t-row>
      <t-table
        :data="data"
        :columns="columns"
        row-key="sign"
        vertical-align="top"
        :hover="true"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
      >
      </t-table>
    </t-card>

    <t-dialog
      v-model:visible="confirmDeleteVisible"
      header="确认"
      :body="confirmDeleteBody"
      @confirm="onConfirmDelete"
    />
  </div>
</template>

<script lang="ts">
export default {
  name: 'rpIndex',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin, PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref, watch} from 'vue';

import ApiPermission from '@/api/sys/ApiPermission';
import ApiUserRole from '@/api/sys/ApiUserRole';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import {queryDef, paginationDef} from "@/api/common/query";

const props = defineProps({
  managerId: String,
  selectDisabled: Boolean,
});
watch(
  () => props.managerId,
  (newValue, oldValue) => {
    loadRoles();
  },
);
// 定义变量 start -------------------
// 读取设置内容
const store = useSettingStore();
// 列表数据
const data = ref([]);
const sourceData = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
});
// 数据是否加载中
const dataLoading = ref(false);
// 是否显示确认删除弹窗
const confirmDeleteVisible = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 表头
const columns: PrimaryTableCol[] = [
  {
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
    fixed: 'left',
    checkProps: ({rowIndex}) => ({disabled: props.selectDisabled}),
  },
  {title: '名称', colKey: 'name', width: 200},
  {title: '标识', colKey: 'sign', width: 160},
];
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiPermission.all({
    data: queryForm.value,
    success: (res: any) => {
      sourceData.value = res;
      handlerData(res);
      dataLoading.value = false;
    }
  });
  loadRoles();
};
const loadRoles = () => {
  if (props.managerId) {
    ApiUserRole.all(
      {
        data: {
          roleId: props.managerId,
        },
        success: (res: any) => {
          selectedRowKeys.value = [];
          res.forEach((v: any) => {
            selectedRowKeys.value.push(v.permissionSign);
          });
        }
      }
    );
  }
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
      names.join(',') +
      (selectedRowData.value.length > names.length ? ` ...等 ${selectedRowData.value.length} 个数据吗？` : ' 吗？')
    }，删除后无法恢复`;
  }
  return '';
};
// 确认删除事件
const onConfirmDelete = () => {
  confirmDeleteVisible.value = false;
  // 真实业务请发起请求
  ApiPermission.delete({
    ids: selectedRowKeys.value,
    success: (res: any) => {
      MessagePlugin.success('删除成功');
      resetIdx();
      pagination.value.current = 1;
      getData();
    }
  });
};
// 处理表单顶部距离
const headerAffixedTop = computed(
  () =>
    ({
      offsetTop: store.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    } as any),
);
const handlerData = (d: any[]) => {
  // 避免外部数据受到干扰拷贝一份处理
  const r = JSON.parse(JSON.stringify(d));
  const result: any = [];
  const map = new Map();
  r.forEach((item: any) => {
    map.set(item.sign, item);
  });
  // console.log(map);
  r.forEach((item: any) => {
    // item.pid 为null时 返回underfined
    const parent = map.get(item.parentSign);
    if (parent) {
      (parent.children || (parent.children = [])).push(item);
    } else {
      // 这里push的item是pid为null的数据
      result.push(item);
    }
  });
  data.value = result;
};
const search = () => {
  dataLoading.value = true;
  let rs: any = [];
  if (queryForm.value.searchKey) {
    sourceData.value.forEach((v: any) => {
      if (v.name.indexOf(queryForm.value.searchKey) > -1 || v.sign.indexOf(queryForm.value.searchKey) > -1) {
        rs.push(v);
      }
    });
  } else {
    rs = sourceData.value;
  }
  handlerData(rs);
  dataLoading.value = false;
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped></style>
