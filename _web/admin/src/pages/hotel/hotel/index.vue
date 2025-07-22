<template>
  <t-loading :loading="dataLoading">
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">新建</t-button>
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input">
          <t-input v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable @enter="getData(true)">
            <template #suffix-icon>
              <search-icon size="16px" @click="getData(true)"/>
            </template>
          </t-input>
        </div>
      </t-row>
      <div v-if="data.length == 0" style="width: 100%;text-align: center">
        <t-space :direction="'vertical'" style="margin: 20px auto">
          <component style="opacity: .3;" :is="EmptyListIcon"></component>
          <h2 style="text-align: center">暂无数据</h2>
        </t-space>
      </div>
      <t-row style="margin: -20px" :gutter="24" v-if="data.length > 0">
        <t-col :span="24" :xs="24" :sm="12" :md="6" :lg="4" :xl="3" v-for="(item, index) in data">
          <div style="padding: 20px;box-sizing: border-box">
            <div class="hotel-card">
              <div class="title" style="font-size: 16px;font-weight: bold">{{ item.name }}</div>
              <div style="color: var(--td-text-color-secondary);margin: 10px 0">{{ item.address }}</div>
              <div style="color: var(--td-text-color-secondary)">tel-{{ item.tel }} {{ item.managerUserName }}</div>
              <div style="height: 20px"></div>
              <div style="color: var(--td-text-color-secondary)">星级</div>
              <t-rate :value="item.starLevel" size="16px" disabled></t-rate>
              <div style="height: 40px"></div>
              <div>
                <t-image :src="item.picFileUrl" fit="cover"
                         style="height: 200px;border-radius: var(--td-radius-medium)"></t-image>
              </div>
              <div style="margin-top: 40px;display: flex;align-items: center">
                <div style="flex: 1">
                  <div style="color: var(--td-text-color-secondary)">入住率</div>
                  <div style="font-size: 16px;font-weight: bold">80%</div>
                </div>
                <div>
                  <t-button theme="primary" @click="handleClickEdit(item.id)">详情</t-button>
                  <t-button theme="warning" @click="handleClickDelete(item)">删除</t-button>
                </div>
              </div>
            </div>
          </div>
        </t-col>
      </t-row>
      <div style="margin: 20px 20px 0;">
        <t-pagination :total="pagination.total" :current="pagination.current" :page-size="pagination.pageSize"
                      @change="changePage"/>
      </div>
    </t-card>

    <t-dialog v-model:visible="confirmDeleteVisible" header="确认" :body="confirmDeleteBody"
              @confirm="onConfirmDelete"/>
  </t-loading>
</template>

<script lang="ts">
export default {
  name: 'hotelIndex',
};
</script>

<script setup lang="ts">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';

import ApiHotel from '@/api/hotel/ApiHotel';
import {useSettingStore} from '@/store';
import EmptyListIcon from "@/assets/svg-big/empty-list.svg";
import {queryDef, paginationDef} from "@/api/common/query";

// 定义变量 start -------------------
// 读取设置内容
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
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
const changePage = (page: any) => {
  pagination.value.current = page.current;
  pagination.value.pageSize = page.pageSize;
  getData();
};

// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiHotel.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records;
      pagination.value.total = res.total;
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
      names.join(',') + (selectedRowData.value.length > names.length ? ` ...等 ${selectedRowData.value.length} 个数据吗？` : ' 吗？')
    }，删除后无法恢复`;
  }
  return '';
};
// 确认删除事件
const onConfirmDelete = () => {
  confirmDeleteVisible.value = false;
  // 真实业务请发起请求
  ApiHotel.delete({
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
  router.push(`/hotel/hotel-edit?id=${id ? id : ''}`);
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
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  getData();
});
</script>

<style lang="less" scoped>
.hotel-card:hover {
  box-shadow: 0 0 10px var(--td-table-shadow-color);
}

.hotel-card {
  padding: 20px;
  transition: box-shadow 0.1s linear;
  width: 100%;
  background-color: var(--td-bg-color-container);
  border-radius: var(--td-radius-medium);
  //box-shadow: 4px 8px 30px var(--td-bg-color-container-show);
  box-shadow: 4px 4px 50px var(--td-bg-color-container-show);
}
</style>
