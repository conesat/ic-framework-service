<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="showSelectUser = true" :disabled="!posId" :theme="posId?'primary':'default'">添加用户岗位
          </t-button>
          <t-button variant="base" theme="default" :disabled="!posId || !selectedRowKeys.length"
                    @click="handleClickDelete()"
          >移除用户岗位
          </t-button>
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input">
          <t-input v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable @enter="getData(true)">
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
        <template #username="{ row }">
          <t-space>
            <t-link hover="color" @click="handleClickEdit(row.userId)">{{ row.username }}</t-link>
          </t-space>
        </template>
        <template #status="{ row }">
          <t-tag :theme="row.status.code === 1 ? 'success':'warning'" variant="light">{{ row.statusText }}</t-tag>
        </template>
        <template #op="{ row }" v-if="posId">
          <t-link hover="color" theme="warning" @click="handleClickDelete(row)" :disabled="!posId">移除</t-link>
        </template>
        <template #empty>
          {{ posId ? '暂无数据' : '请选择岗位' }}
        </template>
      </t-table>
    </t-card>

    <t-dialog
      v-model:visible="confirmDeleteVisible"
      header="确认"
      :body="confirmDeleteBody"
      @confirm="onConfirmDelete"
    />

    <t-dialog v-model:visible="showSelectUser" header="选择用户" :footer="false" width="800"
              @cancel="showSelectUser = false">
      <user-select :mutilate="true"
                   v-if="showSelectUser"
                   :not-in-pos-id="posId"
                   :pos-id="posId"
                   :pos-name="posName"
                   @cancel="showSelectUser = false"
                   @selected="onUserSelected"></user-select>
    </t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: 'PosUser',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {DialogPlugin, MessagePlugin, PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref, watch} from 'vue';

import ApiUserPos from '@/api/user/ApiUserPos';
import UserSelect from '@/pages/user/user/select.vue';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import router from "@/router";
import {queryDef, paginationDef} from "@/api/common/query";


// 定义变量 start -------------------
const props = defineProps({
  posId: {
    type: Number,
    default: null,
  },
  posName: {
    type: String,
    default: null,
  }
})

// 排序
const sort = ref([{
  // 按照 name 字段进行排序
  sortBy: 'username',
  // 是否按照降序进行排序
  descending: true,
}]);
// 读取设置内容
const store = useSettingStore();
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
// 是否显示确认删除弹窗
const confirmDeleteVisible = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 是否显示选择用户
const showSelectUser = ref(false);
// 表头
const columns: PrimaryTableCol[] = [
  {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
  {title: '姓名', colKey: 'userName'},
  {title: '关联时间', colKey: 'createTime', sorter: true},
  {
    align: 'left',
    fixed: 'right',
    width: 60,
    colKey: 'op',
    title: '操作',
  },
];
// 定义变量 end -------------------

// 监听props变化
watch(() => props.posId, (newValue) => {
  getData()
});

// 定义方法 start -------------------
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/user/custom-user-edit?id=${id || ''}`);
};
const sortChange = (sortInfo: any) => {
  // 对于受控属性而言，这里的赋值很重要，不可缺少
  sort.value = sortInfo;
};
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  if (!props.posId) {
    data.value = [];
    return
  }
  dataLoading.value = true;
  queryForm.value.orders = JSON.stringify(sort.value)
  ApiUserPos.page({
    data: {
      ...queryForm.value,
      posId: props.posId
    },
    pagination: pagination.value,
    success: (res: any) => {
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
  ApiUserPos.delete({
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
  if (!props.posId) {
    MessagePlugin.warning("请先选择岗位");
    return
  }
  selectedRowKeys.value = val;
  selectedRowData.value = e.selectedRowData;
};
// 表单参数变化 包括过滤、分页
const reHandleChange = (changeParams: any, triggerAndData: unknown) => {
  pagination.value = changeParams.pagination;
  getData();
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
      offsetTop: store.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    } as any),
);

const onUserSelected = (val: any) => {
  showSelectUser.value = false;
  let userIds = val.map((user: any) => user.id)
  ApiUserPos.create({
    data: {
      posId: props.posId,
      userIds: userIds
    },
    success: (res: any) => {
      MessagePlugin.success('添加成功');
      getData(true);
    }
  });
};


// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
</style>
