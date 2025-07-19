<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">新建</t-button>
          <t-button variant="base" :theme="!changeMenus?'default':'primary'"
                    :disabled="!changeMenus" @click="saveRoleMenus()">
            保存更改
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
      <t-row>

      </t-row>
      <t-enhanced-table
        :tree="treeConfig"
        :tree-expand-and-fold-icon="treeExpandAndFoldIconRender"
        :data="data"
        :columns="columns"
        row-key="id"
        vertical-align="top"
        :hover="true"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
        @select-change="reHandleSelectChange"
        @filter-change="filterChange"
      >
        <template #iconShow="{ row }">
          <icon-show v-if="row.icon" :name="row.icon" :size="20" :type="row.iconType.code"></icon-show>
        </template>
        <template #status="{ row }">
          <t-tag v-if="row.status" :theme="row.status.code === 1 ? 'success':'warning'" variant="light">
            {{ row.status.text }}
          </t-tag>
        </template>
        <template #blank="{ row }">
          <t-tag :theme="row.blank ? 'success':'warning'" variant="light">{{ row.blank ? '是' : '否' }}</t-tag>
        </template>
        <template #menuType="{ row }">
          <t-tag v-if="row.menuType" variant="light">{{ row.menuType.text }}</t-tag>
        </template>
        <template #menuPlatformType="{ row }">
          <t-tag v-if="row.menuPlatformType" variant="light">{{ row.menuPlatformType.text }}</t-tag>
        </template>
      </t-enhanced-table>
    </t-card>

    <t-dialog v-model:visible="confirmSaveVisible" header="确认" :body="confirmSaveBody"
              @confirm="onConfirmSave"/>
  </div>
</template>

<script lang="tsx">
export default {
  name: 'menuIndex',
};
</script>

<script setup lang="tsx">
import {ChevronDownIcon, ChevronRightIcon, SearchIcon} from 'tdesign-icons-vue-next';
import {
  EnhancedTable as TEnhancedTable,
  EnhancedTableProps, FilterValue,
  MessagePlugin, OptionData,
  PrimaryTableCol, TableFilterChangeContext, TableRowData
} from 'tdesign-vue-next';
import {computed, onMounted, reactive, ref, watch} from 'vue';
import {useRouter} from 'vue-router';

import {queryDef, paginationDef} from "@/api/common/query";
import ApiMenu from '@/api/sys/ApiMenu';
import ApiRoleMenu from '@/api/sys/ApiRoleMenu';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import IconShow from "@/components/svg-icon-select/icon-show.vue";

// 定义变量 start -------------------
const props = defineProps({
  roleId: {
    type: String,
    default: null
  },
  selectDisabled: {
    type: Boolean,
    default: null
  },
  userType: {
    type: String,
    default: null
  }
})
const treeConfig: EnhancedTableProps['tree'] = reactive({
  treeNodeColumnIndex: 1,
  indent: 25,
  expandTreeNodeOnClick: true,
  checkStrictly: true,
});
const treeExpandAndFoldIconRender: EnhancedTableProps['treeExpandAndFoldIcon'] = (h, {type, row}) => {
  return type === 'expand' ? <ChevronRightIcon/> : <ChevronDownIcon/>;
};

// 读取设置内容
const store = useSettingStore();
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  searchKey: '',
  roleId: '',
  userType: '',
  menuPlatformType: []
});
// 数据是否加载中
const dataLoading = ref(false);
// 是否显示确认保存弹窗
const confirmSaveVisible = ref(false);
// 记录选择是否变化
const changeMenus = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
const menuPlatformTypes = ref<OptionData[]>([
  {
    label: '全部',
    checkAll: true,
  }, {
    label: '管理端',
    value: 1
  }]);
// 路由
const router = useRouter();

// 表头
const columns: PrimaryTableCol[] = [
  {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
  {title: '菜单名', colKey: 'name'},
  {title: 'icon', colKey: 'iconShow', width: 80},
  {title: '类型', colKey: 'menuType'},
  {
    title: '归属平台类型', colKey: 'menuPlatformType',
    // 多选过滤配置
    filter: {
      type: 'multiple',
      resetValue: [],
      list: menuPlatformTypes.value,
      // 是否显示重置取消按钮，一般情况不需要显示
      showConfirmAndReset: true,
    },
  },
  {title: '是与否有效', colKey: 'status'},
  {title: '是否新开标签', colKey: 'blank'},
  {title: '排序号', colKey: 'orderNo'},
];
// 定义变量 end -------------------

watch(() => props.roleId, (newValue) => {
  getRoleMenuData()
});

watch(() => props.userType, (newValue) => {
  getData()
});

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiMenu.all({
    data: {
      noParent: 1,
      userType: props.userType,
      menuPlatformType: queryForm.value.menuPlatformType
    },
    success: (res: any) => {
      data.value = res;
      if (props.roleId) {
        getRoleMenuData();
      } else {
        dataLoading.value = false;
      }
    }
  });
};
// 获取角色菜单数据
const getRoleMenuData = async () => {
  queryForm.value.roleId = props.roleId
  queryForm.value.userType = props.userType
  ApiRoleMenu.all({
    data: queryForm.value,
    success: (res: any) => {
      changeMenus.value = false
      selectedRowKeys.value = res.map((f: any) => f.menuId)
      dataLoading.value = false;
    }
  });
};
// 重置选中数据
const resetIdx = () => {
  changeMenus.value = false
  selectedRowData.value = [];
  selectedRowKeys.value = [];
};
// 构建确认删除内容
const confirmSaveBody = () => {
  if (selectedRowData.value.length > 0) {
    const names = [];
    for (let i = 0; i < selectedRowData.value.length; i++) {
      if (selectedRowData.value[i]) {
        if (selectedRowData.value[i].row) {
          names.push(selectedRowData.value[i].row.name);
        } else {
          names.push(selectedRowData.value[i].name);
        }
      }
      if (names.length > 2) {
        break
      }
    }
    return `确认为角色赋予 ${
      names.join(',') + (selectedRowData.value.length > names.length ? ` ...等 ${selectedRowData.value.length} 个菜单吗？` : ' 菜单吗？')
    }`;
  }
  return '';
};
// 确认删除事件
const onConfirmSave = () => {
  confirmSaveVisible.value = false;
  // 真实业务请发起请求
  ApiRoleMenu.editBatch({
    data: {roleId: props.roleId, menuIds: selectedRowKeys.value},
    success: (res: any) => {
      MessagePlugin.success('保存成功');
      resetIdx();
      getRoleMenuData();
    }
  });
};
// 单个删除按钮事件
const saveRoleMenus = () => {
  if (selectedRowData.value.length > 0) {
    confirmSaveVisible.value = true;
  } else {
    MessagePlugin.warning('请先选择数据');
  }
};
// 过滤数据变化
const filterChange = (filterValue: FilterValue, context: TableFilterChangeContext<TableRowData>) => {
  queryForm.value.menuPlatformType = filterValue.menuPlatformType
  getData()
};
// 表单选中数据变化
const reHandleSelectChange = (val: (string | number)[], e: any) => {
  if (props.selectDisabled) {
    MessagePlugin.warning('请先选择角色');
    return
  }
  changeMenus.value = true;
  selectedRowKeys.value = val;
  selectedRowData.value = e.selectedRowData;
};
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/sys/menu-edit?id=${id ? id : ''}`);
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
