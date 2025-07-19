<template>
  <div class="base-form">
    <t-space style="display: inline-flex;height: 100%;">
      <t-card :bordered="false" style="height: 100%;width: 300px">
        <simple-select :chat-id="queryForm.chatId" @selected="changeChat"></simple-select>
      </t-card>
      <t-card :bordered="false" style="height: 100%">
        <h3 style="margin-bottom: 10px">聊天内容{{ chatUserCount > 2 ? ` [${chatUserCount}人]` : '' }}</h3>
        <div
          style="width: 500px;margin: 0 auto;height: 100%;display: flex;flex-direction: column">
          <t-chat
            :clearHistory="false"
            variant="base"
            :data="messages">
            <template #avatar="{ item, index }">
              <t-avatar size="medium" shape="circle" :image="getUserPic(item.userId)">
                {{ getUserName(item.userId).substring(0, 1) }}
              </t-avatar>
            </template>
            <template #name="{ item, index }">
              <span>{{ getUserName(item.userId) }}</span>
            </template>
            <template #content="{ item, index }">
              <t-chat-content
                style="background-color: var(--td-bg-color-component);border-radius: 10px;padding: 6px 12px"
                v-if="item.message.length > 0" :content="item.message"/>
            </template>
          </t-chat>
          <t-chat-sender
            v-if="false"
            style="border-radius: 10px"
            v-model="messageInput"
            :stop-disabled="loading"
            :textarea-props="{placeholder: '请输入消息...'}"
            @send="sendMessage()"
          >
          </t-chat-sender>
        </div>
      </t-card>
    </t-space>
  </div>
</template>
<script setup lang="ts">
import {ref, onMounted, nextTick} from 'vue';
import SimpleSelect from './simple-select.vue';
import {useRoute} from 'vue-router';
import {queryDef, paginationDef} from "@/api/common/query";
import ApiChatMsg from "@/api/sys/ApiChatMsg";
import ApiChatUser from "@/api/sys/ApiChatUser";
// 路由
const route = useRoute();
// 消息列表
const messages = ref<any[]>([]);
// 是否正在加载
const loading = ref(false);
const userMap = ref<any>({});
// 是否还有更多消息
const hasMore = ref(true);
// 人数
const chatUserCount = ref(0);
// 查询表单,包括分页
const queryForm = ref({
  chatId: '',
  pageIndex:1,
  pageSize:50,
  ...queryDef
});
// 消息输入框
const messageInput = ref('');
// 获取历史消息
const loadHistoryMessages = async (reload ?: boolean) => {
  if (reload) {
    messages.value = [];
    queryForm.value.pageIndex = 1;
    hasMore.value = true;
  }
  if (loading.value || !hasMore.value) return;
  loading.value = true;
  // 这里替换为实际的API调用
  ApiChatMsg.page({
    data: queryForm.value,
    success: (res) => {
      res.records.forEach((item: any) => {
        item.role = item.role === '0' ? 'user' : 'assistant';
        item.datetime = item.createTime
        messages.value.push(item)
      });
      hasMore.value = queryForm.value.pageIndex < res.pages;
      queryForm.value.pageIndex++;
    },
    finally: () => {
      loading.value = false;
    }
  });
};

// 发送消息
const sendMessage = async () => {
  if (!messageInput.value.trim()) return;

};
// 发送消息
const changeChat = (chatId: any) => {
  queryForm.value.chatId = chatId;
  loadUsers();
  loadHistoryMessages(true);
};

const getUserPic = (userId: any) => {
  let user = userMap.value[userId]
  if (user) {
    return user.userPic
  }
  return ''
};

const getUserName = (userId: any) => {
  let user = userMap.value[userId]
  if (user) {
    return user.userName
  }
  return '无名'
};

// 加载聊天用户列表
const loadUsers = async () => {
  ApiChatUser.all({
    data: {
      chatId: queryForm.value.chatId
    },
    success(res) {
      chatUserCount.value = res.length;
      res.forEach((item: any) => {
        userMap.value[item.userId] = item
      })
    },
  })
};

onMounted(() => {
  const {id} = route.query;
  if (id) {
    changeChat(id)
  }
});
</script>
<style scoped lang="less">
.base-form {
  height: 100%;
  width: 100%;
  text-align: center;

  :deep(.t-chat-sender__textarea) {
    border-radius: 10px;
  }

  :deep(.t-chat__base) {
    padding-left: 0;
    margin-bottom: 4px;
  }
}
</style>
