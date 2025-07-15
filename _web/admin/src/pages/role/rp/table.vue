<template>
  <div style="height: 100%">
    <t-card class="list-card-container" :bordered="false" style="height: 100%;overflow: auto">
      <t-loading :loading="dataLoading">
        <t-row justify="space-between">
          <div class="left-operation-container">
            <t-checkbox :checked="checkedAll" :indeterminate="indeterminateAll" label="全选"
                        :disabled="!roleId || disable"
                        @change="handleSelectAll"></t-checkbox>
          </div>
        </t-row>
        <t-row style="margin: -20px -10px" v-if="data.length > 0">
          <t-col :xs="12" :sm="12" :md="6" :xl="3" :xxl="2" v-for="(item, index) in data" :key="index"
                 style="padding: 20px 10px">
            <t-checkbox :checked="item.checked" :indeterminate="item.indeterminate"
                        @change="handleGroupSelectAll(item)" :disabled="!roleId || disable">
              <div style="margin-bottom: 10px;">
                <div style="font-size: 12px;font-weight: bolder;">{{ item.name }}</div>
                <div style="color: var(--td-text-color-secondary)">{{ item.path }}</div>
              </div>
            </t-checkbox>
            <div style="margin-left: 24px;">
              <div v-for="(p,index) in item.permissionVOS" style="line-height: 20px" :key="'p_'+ index">
                <t-checkbox @change="checkPermission(item, p)" :checked="p.checked" :disabled="!roleId || disable">
                  {{ p.name }} <span style="color: var(--td-text-color-secondary)">{{ p.path }}</span>
                </t-checkbox>
              </div>
            </div>
          </t-col>
        </t-row>
        <t-row v-else>
          <t-space :direction="'vertical'" style="margin: 20px auto">
            <component style="opacity: .3;" :is="EmptyListIcon"></component>
            <h2 style="text-align: center">{{ !roleId ? '请选择角色' : '暂无数据' }}</h2>
          </t-space>
        </t-row>
      </t-loading>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'permissionIndex',
};
</script>

<script setup lang="ts">
import {computed, onMounted, reactive, ref, watch} from 'vue';

import ApiPermissionGroup from '@/api/role/ApiPermissionGroup';
import EmptyListIcon from '@/assets/svg-big/empty-list.svg';
import ApiRP from '@/api/role/ApiRP';
import {queryDef, paginationDef} from "@/api/common/query";

const props = defineProps({
  roleId: {
    type: String,
    default: null
  },
  userType: {
    type: String,
    default: null
  },
  disable: {
    type: Boolean,
    default: null
  }
});

// 监听props.roleId的变化
watch(() => props.roleId, (newValue) => {
  getRpData()
});
// 监听props.userType的变化
watch(() => props.userType, (newValue) => {
  getData()
});

// 已选中数据
const checkedAll = ref(false);
const indeterminateAll = ref(false);

// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  searchKey: '',
  userType: '',
});
// 数据是否加载中
const dataLoading = ref(false);
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  data.value = [];
  checkedAll.value = false;
  indeterminateAll.value = false;
  if (!props.userType) {
    return
  }
  dataLoading.value = true;
  queryForm.value.userType = props.userType;
  ApiPermissionGroup.all({
    data: queryForm.value,
    success: (res: any) => {
      data.value = res;
      getRpData();
    }
  });
};

/**
 * 获取角色权限
 */
const getRpData = async () => {
  if (props.roleId == null) {
    dataLoading.value = false;
    return
  }
  ApiRP.allPermission(props.roleId, {
    success: (res: any) => {
      data.value.forEach((group: any) => {
        group.permissionVOS.forEach((p: any) => {
          p.checked = res.includes(p.id);
        })
        checkGroup(group);
      })
      dataLoading.value = false;
    }
  });
};


/**
 * 编辑角色权限
 */
const editBatch = async (permissionIds: String[], enable: boolean) => {
  dataLoading.value = true;
  ApiRP.editBatch({
    data: {
      roleId: props.roleId,
      permissionIds: permissionIds,
      enable: enable
    },
    success: (res: any) => {
      dataLoading.value = false;
    }
  });
};

/**
 * 分组权限变化
 * @param group
 */
const handleGroupSelectAll = (group: any) => {
  let permissionIds: any[] = []
  let checked = !group.checked
  group.permissionVOS.forEach((p: any) => {
    p.checked = checked;
    p.indeterminate = false;
    permissionIds.push(p.id);
  })
  editBatch(permissionIds, checked)
  checkGroup(group);
}

/**
 * 权限权限变化
 * @param checked
 */
const handleSelectAll = () => {
  let permissionIds: any[] = []
  let checked = !checkedAll.value
  data.value.forEach((group: any) => {
    group.permissionVOS.forEach((p: any) => {
      p.checked = checked;
      p.indeterminate = false;
      permissionIds.push(p.id);
    })
    checkGroup(group);
  })
  editBatch(permissionIds, checked)
}

/**
 * 选择某个权限
 * @param checked
 * @param group
 * @param item
 */
const checkPermission = (group: any, item: any) => {
  item.checked = !item.checked;
  editBatch([item.id], item.checked)
  checkGroup(group);
};

/**
 * 检查分组是否全选
 * @param group
 */
const checkGroup = (group: any) => {
  let hasChecked = false;
  let hasNotChecked = false;
  group.permissionVOS.forEach((p: any) => {
    if (p.checked) {
      hasChecked = true;
    } else {
      hasNotChecked = true;
    }
  });
  group.checked = hasChecked && !hasNotChecked;
  group.indeterminate = hasChecked && hasNotChecked;
  checkTotal();
}

/**
 * 检查是否全选
 */
const checkTotal = () => {
  let checkAll = true;
  let hasChecked = false;
  for (const i in data.value) {
    let group = data.value[i];
    if (group.indeterminate) {
      checkAll = false;
      hasChecked = true;
    } else if (group.checked) {
      hasChecked = true;
    } else {
      checkAll = false;
    }
    if (hasChecked && !checkAll) {
      break
    }
  }
  checkedAll.value = checkAll;
  indeterminateAll.value = hasChecked && !checkAll;
}

// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped></style>
