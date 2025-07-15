<template>
  <t-row :gutter="[24, 24]">
    <t-col :flex="3">
      <t-space direction="vertical">
        <div class="user-left-greeting">
          <div>
            Hi，{{ mineInfo.name }}
            <span class="regular"> 下午好，欢迎使用IC-Framework</span>
          </div>
          <img src="@/assets/assets-logo.png" class="logo" alt="" />
        </div>

        <t-card title="个人信息" class="card-container" :bordered="false">
          <template #actions>
            <t-button theme="default" shape="square" variant="text" @click="toEditUserInfo">
              <t-icon name="ellipsis" />
            </t-button>
          </template>
          <t-descriptions item-layout="vertical" :column="4" :label-style="{ 'padding-bottom': '0' }">
            <t-descriptions-item label="姓名：">{{ mineInfo.name }}</t-descriptions-item>
            <t-descriptions-item label="性别：">{{ mineInfo.sex ? mineInfo.sex.text : '' }}</t-descriptions-item>
            <t-descriptions-item label="手机号：">{{ mineInfo.phone ? mineInfo.phone : '无' }}</t-descriptions-item>
            <t-descriptions-item label="邮箱：">{{ mineInfo.email ? mineInfo.email : '无' }}</t-descriptions-item>
            <t-descriptions-item label="角色：">
              {{ mineInfo.roles ? mineInfo.roles.map((item: any) => item.name).join('/') : '无' }}
            </t-descriptions-item>
            <t-descriptions-item label="部门：">
              {{ mineInfo.deps ? mineInfo.deps.map((item: any) => item.name).join('/') : '无' }}
            </t-descriptions-item>
            <t-descriptions-item label="职务：">
              {{ mineInfo.pos ? mineInfo.pos.map((item: any) => item.name).join('/') : '无' }}
            </t-descriptions-item>
          </t-descriptions>
        </t-card>

        <t-card class="card-container" title="当前账号在线列表" :bordered="false">
          <online-user></online-user>
        </t-card>
      </t-space>
    </t-col>

    <t-col :flex="1" style="max-width: 340px; min-width: 300px">
      <t-space direction="vertical" style="width: 100%">
        <t-card class="card-container" :bordered="false">
          <div style="display: flex">
            <cropper
              style="border-radius: 50%"
              :width="80"
              :height="80"
              :cover-width="400"
              :cover-height="400"
              :use-type="FileUseTypes.avatar"
              :url="mineInfo.avatarFileUrl"
              :show-delete="false"
              @uploaded="onAvatarUploaded"
            ></cropper>
            <div style="flex: 1; display: flex; align-items: center">
              <div style="width: 100%; text-align: center">
                <div class="name">
                  {{ mineInfo.name }}
                </div>
                <div class="position">
                  {{ mineInfo.deps ? mineInfo.deps.map((item: any) => item.name).join(',') : '无部门' }}
                  <span style="margin: 0 6px">/</span>
                  {{ mineInfo.pos ? mineInfo.pos.map((item: any) => item.name).join(',') : '无职位' }}
                </div>
              </div>
            </div>
          </div>
        </t-card>

        <t-card title="部门成员" class="card-container" :bordered="false">
          <t-empty v-if="!depUserDataLoading && depUserList.length == 0"> 暂无数据</t-empty>
          <t-loading :loading="depUserDataLoading">
            <t-list :split="false">
              <t-list-item v-for="(item, index) in depUserList" :key="index">
                <t-list-item-meta :title="item.name" :description="item.posNames">
                  <template #image>
                    <t-avatar size="56px" :image="item.avatarFileUrl">{{ item.name.substring(0, 1) }}</t-avatar>
                  </template>
                </t-list-item-meta>
              </t-list-item>
              <t-pagination
                :total="pagination.total"
                :page-size="depUserParams.pageSize"
                :current="pagination.current"
                size="small"
                :folded-max-page-btn="1"
                :max-page-btn="1"
                show-page-number
                :show-page-size="false"
                :total-content="false"
                show-previous-and-next-btn
                @change="changeDepUserPage"
              />
            </t-list>
          </t-loading>
        </t-card>
      </t-space>
    </t-col>
  </t-row>
</template>
<script lang="ts">
export default {
  name: 'UserIndex',
};
</script>
<script setup lang="ts">
import type { PageInfo } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';

import ApiDepUser from '@/api/org/ApiDepUser';
import ApiUserMine from '@/api/user/ApiUserMine';
import Cropper from '@/components/upload/cropper.vue';
import { FileUseTypes } from '@/constants/file-user-types';
import OnlineUser from '@/pages/sys/online-user/mine.vue';
import router from '@/router';
import { queryDef, paginationDef } from '@/api/common/query';

const depUserDataLoading = ref(false);
const depUserList = ref([]);

// 排序
const sort = ref([
  {
    sortBy: 'posLevel',
    descending: true,
  },
]);

const depUserParams = ref({
  ...queryDef,
  depIds: [],
  userIdNe: '',
  orders: JSON.stringify(sort.value),
});
// 分页
const pagination = ref(paginationDef);

const mineInfo = ref({
  id: '',
  name: '',
  username: '',
  avatarFileUrl: '',
  su: false,
  sex: { code: 1, text: '男' },
  phone: '',
  email: '',
  roles: [],
  deps: [],
  pos: [],
});

onMounted(() => {
  ApiUserMine.mine({
    success: (res: any) => {
      mineInfo.value = res;
      if (res.deps) {
        depUserParams.value.depIds = res.deps.map((item: any) => item.id);
        getDepUsers();
      }
    },
  });
});

const onAvatarUploaded = (res: any) => {
  ApiUserMine.editAvatar({
    data: { avatarFileId: res.id },
  });
};

// 页面变化
const changeDepUserPage = (pageInfo: PageInfo) => {
  pagination.value.current = pageInfo.current;
  getDepUsers();
};
const getDepUsers = () => {
  depUserParams.value.userIdNe = mineInfo.value.id;
  depUserDataLoading.value = true;
  ApiDepUser.pageDetail({
    data: depUserParams.value,
    pagination: pagination.value,
    success: (res: any) => {
      depUserList.value = res.data;
      pagination.value.total = res.total;
      depUserDataLoading.value = false;
    },
  });
};
const toEditUserInfo = () => {
  router.push(`/mine/edit?id=${mineInfo.value.id || ''}`);
};
</script>

<style lang="less" scoped>
:deep(.t-card__title) {
  font: var(--td-font-title-large);
  font-weight: 400;
}

.user-left-greeting {
  padding: var(--td-comp-paddingTB-xxl) var(--td-comp-paddingLR-xxl);
  font: var(--td-font-title-large);
  background: var(--td-bg-color-container);
  color: var(--td-text-color-primary);
  text-align: left;
  border-radius: var(--td-radius-medium);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .regular {
    margin-left: var(--td-comp-margin-xl);
    font: var(--td-font-body-medium);
  }

  .logo {
    width: 168px;
  }
}

.card-container {
  padding: var(--td-comp-paddingTB-xxl) var(--td-comp-paddingLR-xxl);

  :deep(.t-card__header) {
    padding: 0;
    margin-bottom: var(--td-comp-margin-m);
  }

  :deep(.t-card__body) {
    padding: 0;
  }
}

.name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}
</style>
