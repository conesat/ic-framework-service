<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">新建房间</t-button>
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input" style="display: flex">
          <t-input v-model="queryForm.searchKey" placeholder="搜索房间号" clearable @enter="getData(true)"
                   style="width: 0;flex: 1">
            <template #suffix-icon>
              <search-icon size="16px" @click.stop="getData(true)"/>
            </template>
          </t-input>
          <div style="width: 20px"></div>
          <t-button shape="circle" :theme="showFilter?'primary':'default'" @click="showFilter = !showFilter">
            <template #icon>
              <AdjustmentIcon/>
            </template>
          </t-button>
        </div>
      </t-row>
      <div class="filter" :class="showFilter?'filter-show':''" :style="{marginBottom: showFilter?'20px':'0'}">
        <div
          style="background-color: var(--td-bg-color-page);padding: 10px 20px;display: inline-flex;border-radius: var(--td-radius-large);">
          <t-button size="small" theme="default" @click="showSelectHotel=true">
            {{ queryForm.hotelName ? '酒店：' + queryForm.hotelName : '选择酒店' }}
            <template #suffix>
              <ChevronRightIcon/>
            </template>
          </t-button>
          <t-button size="small" theme="default" @click="showSelectBuilding=true" :disabled="!queryForm.hotelId">
            {{ queryForm.buildingName ? '楼栋：' + queryForm.buildingName : '选择楼栋' }}
            <template #suffix>
              <ChevronRightIcon/>
            </template>
          </t-button>
          <t-button size="small" theme="default" @click="showSelectFloor=true" :disabled="!queryForm.buildingId">
            {{ queryForm.floorName ? '楼层：' + queryForm.floorName : '选择楼层' }}
            <template #suffix>
              <ChevronRightIcon/>
            </template>
          </t-button>
        </div>
        <t-button style="margin-left: 20px" variant="outline" @click="clearFilter">
          <template #icon>
            <ClearIcon/>
          </template>
          清空
        </t-button>
      </div>
      <room-status-table ref="roomStatusTableRef" :hotel-id="queryForm.hotelId" :building-id="queryForm.buildingId"
                         :search-key="queryForm.searchKey"
                         :floor-id="queryForm.floorId"></room-status-table>
    </t-card>


    <t-dialog v-model:visible="showSelectHotel" header="选择酒店" :footer="false" width="800">
      <hotel-select v-if="showSelectHotel" @cancel="showSelectHotel = false"
                    @selected="hotelSelected"></hotel-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectBuilding" header="选择楼层" :footer="false" width="800">
      <building-select v-if="showSelectBuilding" @cancel="showSelectBuilding = false" :hotel-id="queryForm.hotelId"
                       @selected="buildingSelected"></building-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectFloor" header="选择楼层" :footer="false" width="800">
      <floor-select v-if="showSelectFloor" @cancel="showSelectFloor = false"
                    @selected="floorSelected"></floor-select>
    </t-dialog>
  </div>
</template>

<script lang="tsx">
export default {
  name: 'roomIndex',
};
</script>

<script setup lang="tsx">
import roomStatusTable from './room-status-table.vue';
import hotelSelect from '@/pages/hotel/hotel/select.vue';
import floorSelect from '@/pages/hotel/floor/select.vue';
import buildingSelect from '@/pages/hotel/building/select.vue';
import {SearchIcon, AdjustmentIcon, ChevronRightIcon, ClearIcon} from 'tdesign-icons-vue-next';
import {computed, onMounted, ref, nextTick} from 'vue';
import {useRouter} from 'vue-router';

import {useSettingStore} from "@/store";
import {queryDef, paginationDef} from "@/api/common/query";

// 定义变量 start -------------------
// 读取设置内容
const store = useSettingStore();

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
const roomStatusTableRef = ref(null);
// 路由
const router = useRouter();
// 定义变量 end -------------------

// 定义方法 start -------------------
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/hotel/room-edit?id=${id ? id : ''}`);
};
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  roomStatusTableRef.value.getData();
};
const hotelSelected = (val: any) => {
  showSelectHotel.value = false;
  queryForm.value.hotelId = val.id
  queryForm.value.hotelName = val.name
};

const buildingSelected = (val: any) => {
  showSelectBuilding.value = false;
  queryForm.value.buildingId = val.id
  queryForm.value.buildingName = val.name
};

const floorSelected = (val: any) => {
  showSelectFloor.value = false;
  queryForm.value.floorId = val.id
  queryForm.value.floorName = val.name
};

const clearFilter = () => {
  queryForm.value.hotelId = null;
  queryForm.value.hotelName = null;
  queryForm.value.buildingId = null;
  queryForm.value.buildingName = null;
  queryForm.value.floorId = null;
  queryForm.value.floorName = null;
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
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

  .status.disabled, .status.status-0 {
    background-color: var(--td-gray-color-8);
  }

  .status.status-1, .status.status-5, .status.status-7 {
    background-color: var(--td-success-color);
  }

  .status.status-2 {
    background-color: var(--td-brand-color);
  }

  .status.status-3, .status.status-4 {
    background-color: var(--td-warning-color);
  }

  .status.status-6 {
    background-color: var(--td-error-color);
  }
}
</style>
