<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-checkbox :checked="checkedAll" :indeterminate="indeterminateAll" label="全选" :disabled="roleId == null"
                      @change="handleSelectAll"></t-checkbox>
        </div>
      </t-row>
      <t-row style="margin: -20px -10px">
        <t-col :sm="6" :md="4" :xl="3" :xxl="2" v-for="(item, index) in data" :key="index" style="padding: 20px 10px">
          <t-checkbox :checked="item.checked" :indeterminate="item.indeterminate"
                      @change="handleGroupSelectAll($event, item)" :disabled="roleId == null">
            <div style="margin-bottom: 10px;">
              <div style="font-size: 12px;font-weight: bolder;">{{ item.name }}</div>
              <div style="color: var(--td-text-color-secondary)">{{ item.path }}</div>
            </div>
          </t-checkbox>
          <div style="margin-left: 24px;">
            <div v-for="p in item.permissionVOS" style="line-height: 20px">
              <t-checkbox @change="checkPermission($event, item, p)" :checked="p.checked" :disabled="roleId == null">
                {{ p.name }} <span
                style="color: var(--td-text-color-secondary)">{{ p.path }}</span></t-checkbox>
            </div>
          </div>
        </t-col>
      </t-row>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'permissionIndex',
};
</script>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue';
import {queryDef, paginationDef} from "@/api/common/query";

import ApiPermissionGroup from '@/api/role/ApiPermissionGroup';
import ApiRP from '@/api/role/ApiRP';

const props = withDefaults(defineProps<{
  roleId?: number;
}>(), {roleId: null});

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
  dataLoading.value = true;
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
  ApiRP.all({
    data: {
      "roleId": props.roleId
    },
    success: (res: any) => {
      dataLoading.value = false;
    }
  });
};

/**
 * 分组权限变化
 * @param checked
 */
const handleGroupSelectAll = (checked: boolean, group: any) => {
  group.permissionVOS.forEach((p: any) => {
    p.checked = checked;
    p.indeterminate = false;
  })
  checkGroup(group);
}

/**
 * 权限权限变化
 * @param checked
 */
const handleSelectAll = (checked: boolean) => {
  data.value.forEach((group: any) => {
    group.permissionVOS.forEach((p: any) => {
      p.checked = checked;
      p.indeterminate = false;
    })
    checkGroup(group);
  })
}

/**
 * 选择某个权限
 * @param checked
 * @param group
 * @param item
 */
const checkPermission = (checked: boolean, group: any, item: any) => {
  item.checked = checked;
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
