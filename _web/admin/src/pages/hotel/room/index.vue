<template>
  <t-loading :loading="dataLoading">
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">新建</t-button>
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input" style="display: flex">
          <t-input
            v-model="queryForm.searchKey"
            placeholder="请输入你需要搜索的内容"
            clearable
            @enter="getData(true)"
            style="width: 0; flex: 1"
          >
            <template #suffix-icon>
              <search-icon size="16px" @click.stop="getData(true)" />
            </template>
          </t-input>
          <div style="width: 20px"></div>
          <t-button shape="circle" :theme="showFilter ? 'primary' : 'default'" @click="showFilter = !showFilter">
            <template #icon>
              <AdjustmentIcon />
            </template>
          </t-button>
        </div>
      </t-row>
      <div class="filter" :class="showFilter ? 'filter-show' : ''" :style="{ marginBottom: showFilter ? '20px' : '0' }">
        <div
          style="
            background-color: var(--td-bg-color-page);
            padding: 10px 20px;
            display: inline-flex;
            border-radius: var(--td-radius-large);
          "
        >
          <t-button size="small" theme="default" @click="showSelectHotel = true">
            {{ queryForm.hotelName ? '酒店：' + queryForm.hotelName : '选择酒店' }}
            <template #suffix>
              <ChevronRightIcon />
            </template>
          </t-button>
          <t-button size="small" theme="default" @click="showSelectBuilding = true" :disabled="!queryForm.hotelId">
            {{ queryForm.buildingName ? '楼栋：' + queryForm.buildingName : '选择楼栋' }}
            <template #suffix>
              <ChevronRightIcon />
            </template>
          </t-button>
          <t-button size="small" theme="default" @click="showSelectFloor = true" :disabled="!queryForm.buildingId">
            {{ queryForm.floorName ? '楼层：' + queryForm.floorName : '选择楼层' }}
            <template #suffix>
              <ChevronRightIcon />
            </template>
          </t-button>
        </div>
        <t-button style="margin-left: 20px" variant="outline" @click="clearFilter">
          <template #icon>
            <ClearIcon />
          </template>
          清空
        </t-button>
      </div>
      <div v-if="data.length === 0" style="width: 100%; text-align: center; margin-top: 20px">
        <t-space :direction="'vertical'" style="margin: 20px auto">
          <component style="opacity: 0.3" :is="EmptyListIcon"></component>
          <h2 style="text-align: center">暂无数据</h2>
        </t-space>
      </div>
      <t-row style="margin: -20px" :gutter="24" v-if="data.length > 0">
        <t-col :span="24" :xs="24" :sm="12" :md="6" :lg="4" :xl="3" v-for="(item, index) in data">
          <div style="padding: 20px; box-sizing: border-box">
            <div class="hotel-card">
              <div class="title" style="font-size: 16px; font-weight: bold">{{ item.no }}</div>
              <div style="color: var(--td-text-color-secondary)">{{ item.address }}</div>
              <div style="color: var(--td-text-color-secondary)">
                {{ item.hotelName }} - {{ item.buildingName }} -
                {{ item.floorName }}
              </div>
              <div style="height: 20px"></div>
              <div style="color: var(--td-text-color-secondary)">房型</div>
              <div style="color: var(--td-text-color-secondary)">{{ item.roomTypeName }}</div>
              <div style="height: 20px"></div>
              <div class="img-parent">
                <t-image
                  class="img"
                  v-if="item.mainPic && item.mainPic.fileUrl"
                  :src="item.mainPic.fileUrl"
                  fit="cover"
                ></t-image>
                <t-image class="img" v-else :src="WsJpg" fit="cover"></t-image>
                <div
                  class="status"
                  :class="`status-${item.roomStatus.code}`"
                  v-if="item.saleStatus && item.saleStatus.code === 1 && item.roomStatus"
                >
                  {{ item.roomStatus.text }}
                </div>
                <div class="status disabled" v-if="item.saleStatus && item.saleStatus.code === 0">
                  {{ item.saleStatus.text }}
                </div>
              </div>
              <div style="margin-top: 20px; display: flex; align-items: center">
                <div style="flex: 1">
                  <div style="color: var(--td-text-color-secondary)">门市价</div>
                  <div style="font-size: 16px; font-weight: bold">￥{{ item.price }}</div>
                </div>
                <div>
                  <t-button shape="circle" theme="primary" @click="handleClickState(item.id)">
                    <template #icon>
                      <DashboardIcon />
                    </template>
                  </t-button>
                  <t-button shape="circle" theme="primary" @click="handleClickEdit(item.id)">
                    <template #icon>
                      <Edit2Icon />
                    </template>
                  </t-button>
                  <t-button shape="circle" theme="warning" @click="handleClickDelete(item.id)">
                    <template #icon>
                      <Delete1Icon />
                    </template>
                  </t-button>
                </div>
              </div>
            </div>
          </div>
        </t-col>
      </t-row>
      <div style="margin: 20px 20px 0">
        <t-pagination
          :total="pagination.total"
          :current="pagination.current"
          :page-size="pagination.pageSize"
          @change="changePage"
        />
      </div>
    </t-card>

    <t-dialog
      v-model:visible="confirmDeleteVisible"
      header="确认"
      :body="confirmDeleteBody"
      @confirm="onConfirmDelete"
    />

    <t-dialog v-model:visible="showSelectHotel" header="选择酒店" :footer="false" width="800">
      <hotel-select
        v-if="showSelectHotel"
        @cancel="showSelectHotel = false"
        @selected="hotelSelected"
      ></hotel-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectBuilding" header="选择楼层" :footer="false" width="800">
      <building-select
        v-if="showSelectBuilding"
        @cancel="showSelectBuilding = false"
        :hotel-id="queryForm.hotelId"
        @selected="buildingSelected"
      ></building-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectFloor" header="选择楼层" :footer="false" width="800">
      <floor-select
        v-if="showSelectFloor"
        @cancel="showSelectFloor = false"
        :building-id="queryForm.buildingId"
        @selected="floorSelected"
      ></floor-select>
    </t-dialog>
  </t-loading>
</template>

<script lang="ts">
export default {
  name: 'roomIndex',
};
</script>

<script setup lang="ts">
import EmptyListIcon from '@/assets/svg-big/empty-list.svg';
import hotelSelect from '@/pages/hotel/hotel/select.vue';
import floorSelect from '@/pages/hotel/floor/select.vue';
import buildingSelect from '@/pages/hotel/building/select.vue';
import {
  SearchIcon,
  AdjustmentIcon,
  ChevronRightIcon,
  ClearIcon,
  Delete1Icon,
  Edit2Icon,
  DashboardIcon,
} from 'tdesign-icons-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import ApiRoom from '@/api/hotel/ApiRoom';

import WsJpg from '@/assets/hotel/img/ws.jpg';
import { queryDef, paginationDef } from '@/api/common/query';

// 定义变量 start -------------------
// 读取设置内容
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
  hotelId: '',
  hotelName: '',
  buildingId: '',
  buildingName: '',
  floorId: '',
  floorName: '',
});
// 数据是否加载中
const dataLoading = ref(false);
const showFilter = ref(false);
const showSelectHotel = ref(false);
const showSelectBuilding = ref(false);
const showSelectFloor = ref(false);
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
const saveQueryForm = () => {
  localStorage.setItem(
    'roomQueryForm',
    JSON.stringify({
      form: queryForm.value,
      showFilter: showFilter.value,
    }),
  );
};
// 获取列表数据
const getData = async (reload?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiRoom.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;

      saveQueryForm();
    },
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
  ApiRoom.delete({
    ids: selectedRowKeys.value,
    success: (res: any) => {
      MessagePlugin.success('删除成功');
      resetIdx();
      getData();
    },
  });
};
// 控制台
const handleClickState = (id?: any) => {
  router.push(`/hotel/room-state?id=${id ? id : ''}`);
};

// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/hotel/room-edit?id=${id ? id : ''}`);
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
const hotelSelected = (val: any) => {
  showSelectHotel.value = false;
  queryForm.value.hotelId = val.id;
  queryForm.value.hotelName = val.name;
  getData();
};

const buildingSelected = (val: any) => {
  showSelectBuilding.value = false;
  queryForm.value.buildingId = val.id;
  queryForm.value.buildingName = val.name;
  getData();
};

const floorSelected = (val: any) => {
  showSelectFloor.value = false;
  queryForm.value.floorId = val.id;
  queryForm.value.floorName = val.name;
  getData();
};

const clearFilter = () => {
  queryForm.value.hotelId = null;
  queryForm.value.hotelName = null;
  queryForm.value.buildingId = null;
  queryForm.value.buildingName = null;
  queryForm.value.floorId = null;
  queryForm.value.floorName = null;
  getData();
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  let queryFormStr = localStorage.getItem('roomQueryForm');
  console.log(queryFormStr);
  if (queryFormStr) {
    let form = JSON.parse(queryFormStr);
    queryForm.value = form.form;
    showFilter.value = form.showFilter;
  }

  getData();
});
</script>

<style lang="less" scoped>
.filter.filter-show {
  height: 45px;
  opacity: 1;
}

.filter {
  text-align: center;
  height: 0;
  opacity: 0;
  transition: 0.2s;
}

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

.img-parent {
  position: relative;

  .img {
    max-width: 100%;
    border-radius: var(--td-radius-medium);
  }

  .status {
    z-index: 9;
    position: absolute;
    right: 0;
    top: 0;
    padding: 2px 10px;
    opacity: 0.9;
    border-bottom-left-radius: var(--td-radius-medium);
    border-top-right-radius: var(--td-radius-medium);
    background-color: var(--td-text-color-brand);
    color: var(--td-font-white-1);
  }

  .status.disabled,
  .status.status-0 {
    background-color: var(--td-gray-color-8);
  }

  .status.status-1,
  .status.status-5,
  .status.status-7 {
    background-color: var(--td-success-color);
  }

  .status.status-2 {
    background-color: var(--td-brand-color);
  }

  .status.status-3,
  .status.status-4 {
    background-color: var(--td-warning-color);
  }

  .status.status-6 {
    background-color: var(--td-error-color);
  }
}
</style>
