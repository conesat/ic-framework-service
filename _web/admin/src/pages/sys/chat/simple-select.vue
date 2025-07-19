<template>
  <t-space direction="vertical" class="container">
    <h3>聊天列表</h3>
    <t-input style="width: 100%;" v-model="queryForm.searchKey" placeholder="查找用户" clearable
             @enter="getData(true)">
      <template #suffix-icon>
        <search-icon size="16px" @click.stop="getData(true)"/>
      </template>
    </t-input>

    <div class="item" :class="chatId == item.id ? 'active':''" v-for="(item,index) in data" :key="index" @click="handleClick(item.id)">
      <div class="content">
        <div style="font-weight: bold;font-size: 14px">{{ item.summary }}</div>
        <div style="font-size: 11px;color: var(--td-text-color-placeholder)">{{ item.createTime }}</div>
      </div>
      <div>
        <t-avatar-group>
          <t-avatar v-for="(user,index) in getChatUserMap(item.id).users" :image="user.userPic" :title="user.userName" >{{ user.userName.substring(0, 1) }}
          </t-avatar>
          <t-avatar v-if="getChatUserMap(item.id).count > 2">
            +{{ getChatUserMap(item.id).count - 2}}
          </t-avatar>
        </t-avatar-group>
      </div>
    </div>
    <div style="text-align: center;cursor: pointer" v-if="hasMore">加载更多</div>
    <div style="text-align: center;" v-else>没有更多了</div>
  </t-space>
</template>

<script lang="ts">
export default {
  name: 'chatSelect',
};
</script>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';

import ApiChat from '@/api/sys/ApiChat';
import {queryDef, paginationDef} from "@/api/common/query";
import {SearchIcon} from "tdesign-icons-vue-next";
import ApiChatUser from "@/api/sys/ApiChatUser";

defineProps({
  chatId: {
    type: String,
  }
});

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel']);
// 列表数据
const data = ref([]);
const chatUserMap = ref<any>({});
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef
});
// 数据是否加载中
const dataLoading = ref(false);
const hasMore = ref(true);
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
    data.value = [];
  }
  dataLoading.value = true;
  ApiChat.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res) => {
      data.value = [...data.value, ...res.records];
      pagination.value.total = res.total;
      hasMore.value = pagination.value.current < res.pages;
      dataLoading.value = false;
      getChatUser(res.records)
      if (pagination.value.current === 1 && data.value.length > 0) {
        emit('selected', data.value[0].id);
      }
    }
  });
};

/** 获取聊天用户，拿前两个就行 */
const getChatUser = async (chats: any[]) => {
  chats.forEach((item: any) => {
    ApiChatUser.page({
      data: {
        pageSize: 2,
        chatId: item.id
      },
      success: (res) => {
        chatUserMap.value[item.id] = {
          users: res.records,
          count: res.total
        };
      }
    });
  })
};
/** 获取聊天用户，拿前两个就行 */
const getChatUserMap = (chatId: string) => {
  return chatUserMap.value[chatId] ?? {
    users: [],
    count: 0
  };
};
const handleClick = (chatId: string) => {
  emit('selected', chatId)
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
.container {
  margin-bottom: 10px;
  width: 100%;
  box-sizing: border-box;

  .item.active, .item:hover {
    background-color: var(--td-bg-color-container-hover);

  }

  .item {
    cursor: pointer;
    display: flex;
    padding: 10px 14px;
    border-radius: 10px;

    .content {
      flex: 1;
      text-align: left;
    }
  }
}
</style>
