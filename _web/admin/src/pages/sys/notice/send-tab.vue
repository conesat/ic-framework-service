<template>
  <div>
    <t-space direction="vertical">
      <t-space>
        <t-radio-group v-model="sendTo" variant="default-filled">
          <t-radio-button value="用户">用户</t-radio-button>
          <t-radio-button value="部门">部门</t-radio-button>
          <t-radio-button value="角色">角色</t-radio-button>
          <t-radio-button value="岗位">岗位</t-radio-button>
        </t-radio-group>
        <div style="width: 20px"></div>
        <t-button @click="selectSend">添加{{ sendTo }}</t-button>
        <t-button theme="default"
                  @click="removeAll"
                  v-if="(sendTo === '用户' && receiveUsers.length > 0)
                  ||
                  (sendTo === '部门' && receiveDeps.length > 0)
                  ||
                  (sendTo === '岗位' && receivePos.length > 0)
                  ||
                  (sendTo === '角色' && receiveRoles.length > 0)">
          移除全部{{ sendTo }}
        </t-button>
      </t-space>

      <t-tabs v-model="sendTo" class="send-to-tabs">
        <t-tab-panel value="用户" label="">
          <t-list>
            <div v-if="receiveUsers.length == 0" class="empty">
              暂无数据
            </div>
            <t-list-item v-for="(item, index) in receiveUsers">
              {{ item.name }}
              <template #action>
                <t-link theme="primary" hover="color" style="margin-left: 16px" @click="receiveUsers.splice(index, 1)">
                  移除
                </t-link>
              </template>
            </t-list-item>
          </t-list>
        </t-tab-panel>
        <t-tab-panel value="部门" label="">
          <t-list>
            <div v-if="receiveDeps.length == 0" class="empty">
              暂无数据
            </div>
            <t-list-item v-for="(item, index) in receiveDeps">
              {{ item.name }}
              <template #action>
                <t-link theme="primary" hover="color" style="margin-left: 16px" @click="receiveDeps.splice(index, 1)">
                  移除
                </t-link>
              </template>
            </t-list-item>
          </t-list>
        </t-tab-panel>
        <t-tab-panel value="角色" label="">
          <t-list>
            <div v-if="receiveRoles.length == 0" class="empty">
              暂无数据
            </div>
            <t-list-item v-for="(item, index) in receiveRoles">
              {{ item.name }}
              <template #action>
                <t-link theme="primary" hover="color" style="margin-left: 16px" @click="receiveRoles.splice(index, 1)">
                  移除
                </t-link>
              </template>
            </t-list-item>
          </t-list>
        </t-tab-panel>
        <t-tab-panel value="岗位" label="">
          <t-list>
            <div v-if="receivePos.length == 0" class="empty">
              暂无数据
            </div>
            <t-list-item v-for="(item, index) in receivePos">
              {{ item.name }}
              <template #action>
                <t-link theme="primary" hover="color" style="margin-left: 16px" @click="receivePos.splice(index, 1)">
                  移除
                </t-link>
              </template>
            </t-list-item>
          </t-list>
        </t-tab-panel>
      </t-tabs>
    </t-space>

    <t-dialog v-model:visible="showSelectRole" header="选择角色" :footer="false" width="800">
      <role-select :mutilate="true"
                   v-if="showSelectRole"
                   @selected="onRoleSelected"
                   @cancel="showSelectRole = false"
                   :select-ids="receiveRoles.map((item:any) => item.id)"
                   user-type="USER"></role-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectDep" header="选择部门" :footer="false" width="800">
      <dep-select v-if="showSelectDep"
                  :mutilate="true"
                  :select-ids="receiveDeps.map((item:any) => item.id)"
                  @selected="onDepSelected"
                  @cancel="showSelectDep = false"
      ></dep-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectPos" header="选择岗位" :footer="false" width="800">
      <position-select v-if="showSelectPos"
                       :mutilate="true"
                       :select-ids="receivePos.map((item:any) => item.id)"
                       @selected="onPosSelected"
                       @cancel="showSelectPos = false"
      ></position-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectUser" header="选择用户" :footer="false" width="800">
      <user-select v-if="showSelectUser"
                       :mutilate="true"
                       :select-ids="receiveUsers.map((item:any) => item.id)"
                       @selected="onUserSelected"
                       @cancel="showSelectUser = false"
      ></user-select>
    </t-dialog>
  </div>
</template>

<script lang="ts">

export default {
  name: 'sendTab',
};
</script>

<script setup lang="ts">
import {onMounted, provide, ref} from 'vue';
import UserSelect from "@/pages/user/user/select.vue";
import DepSelect from "@/pages/org/dept/select.vue";
import RoleSelect from "@/pages/role/role/select.vue";
import PositionSelect from "@/pages/org/position/select.vue";
//引入富文本编辑器

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showSelectRole = ref(false);
const showSelectDep = ref(false);
const showSelectPos = ref(false);
const showSelectUser = ref(false);

// 路由
const sendTo = ref('用户');
const receiveUsers = ref([]);
const receiveDeps = ref([]);
const receivePos = ref([]);
const receiveRoles = ref([]);
// 定义变量 end -------------------

// 定义方法 start -------------------
const selectSend = () => {
  if (sendTo.value === '部门') {
    showSelectDep.value = true
  } else if (sendTo.value === '岗位') {
    showSelectPos.value = true
  } else if (sendTo.value === '角色') {
    showSelectRole.value = true
  } else if (sendTo.value === '用户') {
    showSelectUser.value = true
  }
}
const removeAll = () => {
  if (sendTo.value === '部门') {
    receiveDeps.value = []
  } else if (sendTo.value === '岗位') {
    receivePos.value = []
  } else if (sendTo.value === '角色') {
    receiveRoles.value = []
  } else if (sendTo.value === '用户') {
    receiveUsers.value = []
  }
}

// 部门选择回调
const onDepSelected = (res: any) => {
  showSelectDep.value = false
  receiveDeps.value = res
}

// 岗位选择回调
const onPosSelected = (res: any) => {
  showSelectPos.value = false
  receivePos.value = res
}

// 用户选择回调
const onUserSelected = (res: any) => {
  showSelectUser.value = false
  receiveUsers.value = res
}

// 角色选择回调
const onRoleSelected = (res: any) => {
  showSelectRole.value = false
  receiveRoles.value = res
}

const getData = () => {
  return {
    receiveUsers: receiveUsers.value,
    receiveDeps: receiveDeps.value,
    receivePos: receivePos.value,
    receiveRoles: receiveRoles.value
  }
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {

});

defineExpose({ getData });
</script>

<style lang="less" scoped>

/deep/ .send-to-tabs {
  .t-tabs__header.t-is-top {
    display: none;
  }
}

.empty {
  width: 100%;
  font-size: var(--td-font-size-title-small);
  text-align: center;
  color: var(--td-text-color-secondary);
}
</style>
