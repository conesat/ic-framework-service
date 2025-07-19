<template>
  <div>
    <div style="display: flex">
      <div style="overflow: auto"
           :style="{'max-height':height+'px'}">
        <t-card class="list-card-container" :bordered="false">
          <t-row justify="space-between">
            <div class="left-operation-container">
              <t-button @click="handleClickEdit()">新建</t-button>
              <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="handleClickDelete()"
              >删除
              </t-button>
              <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
            </div>
          </t-row>
          <t-space direction="vertical">
            <t-select v-model="queryForm.userType" @change="changeUserType" clearable placeholder="选择用户类型">
              <t-option v-for="item in userTypes" :key="item.code" :label="item.name" :value="item.code"/>
            </t-select>
            <t-checkbox :checked="checkAll" :indeterminate="indeterminate" :on-change="handleSelectAll" label="全选"/>
            <t-checkbox-group v-model="selectedRowKeys">
              <t-list>
                <t-list-item
                  v-for="(item, index) in data"
                  :key="index"
                  class="role-item"
                  :class="editRole.id === item.id ? 'role-item-selected' : ''"
                  @click="editRole = item"
                >
                  <t-checkbox :key="index" :disabled="item.system" :value="item.id"></t-checkbox>
                  <div
                    :style="{'color':item.system ? 'var(--td-warning-color)' : ''}"
                    style="max-width: 140px; min-width: 100px;text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">
                    <t-popup v-if="item.system" content="系统角色，不可编辑">
                      {{ item.name }}
                    </t-popup>
                    <template v-else>
                      {{ item.name }}
                    </template>
                  </div>
                  <template #action>
                    <t-space>
                      <t-tag :theme="item.status ? 'success' : 'warning'" variant="light" size="small">
                        {{ item.status.text }}
                      </t-tag>
                      <t-link hover="color" theme="primary" @click="handleClickEdit(item.id)">详情</t-link>
                    </t-space>
                  </template>
                </t-list-item>
              </t-list>
            </t-checkbox-group>
            <t-pagination
              :total="pagination.total"
              :pageSize="pagination.pageSize"
              :current="pagination.current"
              size="small"
              :foldedMaxPageBtn="1"
              :maxPageBtn="1"
              showPageNumber
              :showPageSize="false"
              :totalContent="false"
              @change="changePage"
              showPreviousAndNextBtn/>
          </t-space>
        </t-card>
      </div>
      <div style="flex: 1; width: 0;margin-left: 16px;overflow: auto"
           :style="{'max-height':height+'px'}">
        <role-menu-table
          :role-id="editRole.id"
          :select-disabled="!editRole.id || editRole.system"
          :user-type="editRole.userType"
        ></role-menu-table>
      </div>
    </div>
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
  name: 'roleIndex',
};
</script>

<script setup lang="ts">
import {MessagePlugin, PageInfo, PrimaryTableCol} from 'tdesign-vue-next';
import {computed, nextTick, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';

import ApiRole from '@/api/sys/ApiRole';
import RoleMenuTable from '@/pages/sys/role-menu/table.vue';
import ApiSetting from "@/api/sys/ApiSetting";
import {queryDef, paginationDef} from "@/api/common/query";

// 定义变量 start -------------------
// 列表数据
const data = ref([]);
// 用户类型列表
const userTypes = ref([]);
// 组件高度
const height = ref(0);
// 正在编辑的用户
const editRole = ref({
  id: null,
  name: '',
  userType: '',
  system: false,
  status: 1,
});
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
  userType: '',
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
const checkAll = computed(() => data.value.length === selectedRowKeys.value.length);
const indeterminate = computed(
  () => !!(data.value.length > selectedRowKeys.value.length && selectedRowKeys.value.length),
);
// 定义变量 end -------------------

// 定义方法 start -------------------
// 页面变化
const changePage = async (pageInfo: PageInfo) => {
  pagination.value.current = pageInfo.current;
  await getData();
}
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiRole.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;
    }
  });
};
// 用户类型变更
const changeUserType = async () => {
  resetIdx();
  if (editRole.value.userType !== queryForm.value.userType) {
    editRole.value.id = null;
    editRole.value.userType = null;
    editRole.value.name = null;
  }
  await getData();
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
  ApiRole.delete({
    ids: selectedRowKeys.value,
    success: (res: any) => {
      MessagePlugin.success('删除成功');
      resetIdx();
      getData();
    }
  });
};
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/role/role-edit?id=${id || ''}`);
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
const handleSelectAll = (checked: boolean) => {
  selectedRowKeys.value = [];
  if (checked) {
    data.value.forEach((v) => {
      if (!v.system) {
        selectedRowKeys.value.push(v.id);
      }
    });
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  ApiSetting.userTypes({
    success: (res: any) => {
      userTypes.value = res;
    }
  });
  getData();
  nextTick(() => {
    height.value = document.body.offsetHeight - 160
  })
  window.addEventListener('resize', () => {
    height.value = document.body.offsetHeight - 160
  })
});
</script>

<style lang="less" scoped>
.role-item-selected {
  background: var(--td-scrollbar-color);
}

.role-item:hover {
  background: var(--td-scrollbar-color);
}

.role-item {
  border-radius: 4px;
  cursor: pointer;
}
</style>
