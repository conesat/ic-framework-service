<template>
  <div>
    <div style="display: flex">
      <split-panel :left-width="300" :min-left="240" :max-left="500">
        <template #left>
          <t-card class="list-card-container" :bordered="false">
            <div>
              <t-row justify="space-between">
                <div class="left-operation-container">
                  <t-button @click="handleClickEdit()">新建</t-button>
                  <t-button @click="handleClickEdit()">导入</t-button>
                </div>
              </t-row>
              <t-space direction="vertical" style="width: 100%">
                <t-input
                  v-model="queryForm.searchKey"
                  style="width: 100%"
                  auto-width
                  placeholder="查找部门名称/负责人"
                  clearable
                  @enter="getData(true)"
                >
                  <template #suffix-icon>
                    <search-icon size="16px" @click.stop="getData(true)"/>
                  </template>
                </t-input>
                <t-tree :data="data"
                        :expand-all="true"
                        style="width: 100%">
                  <template #icon="{ node }">
                    <ChevronRightIcon v-if="node.getChildren() && !node.expanded"/>
                    <ChevronDownIcon v-else-if="node.getChildren() && node.expanded"/>
                  </template>
                  <template #label="{node}">
                    <div
                      @click="onEditDeptClick($event, node.data)"
                      class="dept-item"
                      :class="node.data.id == editDept.id ? 'dept-item-selected':''">
                      <h4 class="title">{{ node.data.name }}</h4>
                      <div class="sub" v-if="node.data.leaderUserName">负责人：{{ node.data.leaderUserName }}</div>
                    </div>
                  </template>
                  <template #operations="{node}">
                    <t-link hover="color" theme="primary" @click="handleClickEdit(node.data.id)">详情</t-link>
                  </template>
                </t-tree>
              </t-space>

            </div>
          </t-card>
        </template>
        <template #right>
          <dept-user-table
            v-if="!dataLoading"
            :dep-id="editDept.id"
            :dep-name="editDept.name"
            :select-disabled="!editDept.id || editDept.sys"
          ></dept-user-table>
        </template>
      </split-panel>


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
import {MessagePlugin, PrimaryTableCol} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {queryDef, paginationDef} from "@/api/common/query";
import {useRouter} from 'vue-router';
import {ChevronRightIcon, ChevronDownIcon, SearchIcon} from "tdesign-icons-vue-next"
import ApiDept from '@/api/sys/ApiDept';
import DeptUserTable from '@/pages/sys/dept-user/table.vue';
import SplitPanel from '@/components/split-panel/split-panel.vue';

// 定义变量 start -------------------
// 列表数据
const data = ref([]);
const editDept = ref({id: null, name: '', sys: false, leaderUserId: ''});
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  searchKey: '',
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
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiDept.all({
    data: queryForm.value,
    success: (res: any) => {
      handlerDataToTree(res)
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
  ApiDept.delete({
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
  router.push(`/org/dept-edit?id=${id || ''}`);
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

/**
 * 选择部门
 * @param e
 * @param item
 */
const onEditDeptClick = (e: Event, item: any) => {
  e.stopPropagation()
  editDept.value = item
}

/**
 * 转成树形结构
 * @param d
 */
const handlerDataToTree = (d: any[]) => {
  // 避免外部数据受到干扰拷贝一份处理
  const r = JSON.parse(JSON.stringify(d));
  const result: any = [];
  const map = new Map();
  r.forEach((item: any) => {
    map.set(item.id, item);
  });
  // console.log(map);
  r.forEach((item: any) => {
    // item.pid 为null时 返回underfined
    const parent = map.get(item.parentId);
    if (parent) {
      (parent.children || (parent.children = [])).push(item);
    } else {
      // 这里push的item是pid为null的数据
      result.push(item);
    }
  });
  data.value = result;
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
.dept-item {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  padding: 4px 8px;
  border-radius: 4px;
  max-width: 8rem;
  min-width: 100%;

  .sub {
    font-size: 11px;
    color: var(--td-text-color-secondary);
  }
}

/deep/ .dept-item-selected, .dept-item:hover {
  .title {
    color: var(--td-brand-color-6);
  }

  .sub {
    opacity: 0.8;
    color: var(--td-brand-color-6);
  }
}

/deep/ .t-tree__label {
  flex: 1;
}
</style>
