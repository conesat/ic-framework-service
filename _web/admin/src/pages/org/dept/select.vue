<template>
  <div>
    <t-card class="list-card-container" :bordered="false" :class="filter?'filter':''">
      <t-row justify="space-between">
        <t-input
          v-model="queryForm.searchKey"
          style="min-width: 200px"
          auto-width
          placeholder="请输入你需要搜索的内容"
          clearable
          @enter="getData(true)"
        >
          <template #suffix-icon>
            <search-icon size="16px" @click.stop="getData(true)"/>
          </template>
        </t-input>
      </t-row>

      <t-tree :data="data"
              v-model="selectedRowData"
              :checkable="true"
              :check-strictly="true"
              :value-mode="valueMode"
              @change="change"
              hover
              expand-all
              :style="{margin:filter ? '20px 0 0 0':'20px 0'}"
              style="width: 100%;">
        <template #icon="{ node }">
          <ChevronRightIcon v-if="node.getChildren() && !node.expanded"/>
          <ChevronDownIcon v-else-if="node.getChildren() && node.expanded"/>
        </template>
        <template #label="{node}">
          <div
            class="dept-item">
            <h4 class="title">{{ node.data.name }}</h4>
            <div class="sub" v-if="node.data.leaderUserName">负责人：{{ node.data.leaderUserName }}</div>
          </div>
        </template>
        <template #operations="{node}">
          <t-link hover="color" theme="primary" @click="handleClickEdit(node.data.id)">详情</t-link>
        </template>
      </t-tree>


      <t-row justify="end" v-if="!filter">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" :disabled="selectedRowData.length === 0" @click="selected">确认</t-button>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'DeptSelect',
};
</script>

<script setup lang="ts">
import {ChevronDownIcon, ChevronRightIcon, SearchIcon} from 'tdesign-icons-vue-next';
import {TreeProps} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';

import ApiDept from '@/api/org/ApiDept';
import {useSettingStore} from '@/store';
import {queryDef, paginationDef} from "@/api/common/query";

const valueMode = ref<TreeProps['valueMode']>('onlyLeaf');
const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false,
  },
  selectIds: {
    type: Array,
    default: []
  },
  filter: {
    type: Boolean,
    default: false
  }
});

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel', 'change']);
// 读取设置内容
const store = useSettingStore();
// 排序
const sort = ref([{
  // 按照 name 字段进行排序
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
  showPageSize: false,
});
// 数据是否加载中
const dataLoading = ref(false);
// 被选中的数据
const selectedRowData = ref([]);
// 定义变量 end -------------------

// 定义方法 start -------------------

// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  window.open(`#/org/dept-edit?id=${id || ''}`);
};
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
    item.value = JSON.stringify({
      id: item.id,
      name: item.name
    })
    item.label = item.name
    item.checked = props.selectIds.includes(item.id)
    map.set(item.id, item);
  });
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
// 取消选择
const cancel = () => {
  emit('cancel');
};
// 确认选择
const selected = () => {
  let res: any = []
  selectedRowData.value.forEach(item => {
    res.push(JSON.parse(item));
  })
  emit('selected', props.mutilate ? res : res[0]);
};

const change = (value: any) => {
  let r: any = []
  value.forEach((item: any) => {
    r.push(JSON.parse(item))
  })
  emit('change', r);
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
.filter {
  width: 400px;
  padding: 0;
}

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
