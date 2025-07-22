<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <t-input
          auto-width
          v-model="queryForm.searchKey"
          placeholder="请输入你需要搜索的内容"
          clearable
          @enter="getData(true)"
        >
          <template #suffix-icon>
            <search-icon size="16px" @click="getData(true)" />
          </template>
        </t-input>

        <t-space align="center">
          <t-breadcrumb>
            <t-breadcrumb-item key="hotel" @click="showSelectHotel = true">
              {{ queryForm.hotelName ? '酒店：' + queryForm.hotelName : '请选择酒店' }}
            </t-breadcrumb-item>
            <t-breadcrumb-item key="building" v-if="queryForm.hotelId" @click="showSelectBuilding = true">
              {{ queryForm.buildingName ? '楼栋：' + queryForm.buildingName : '请选择楼栋' }}
            </t-breadcrumb-item>
            <t-breadcrumb-item key="floor" v-if="queryForm.buildingId" @click="showSelectFloor = true">
              {{ queryForm.floorName ? '楼层：' + queryForm.floorName : '请选择楼层' }}
            </t-breadcrumb-item>
          </t-breadcrumb>
          <t-button style="margin-left: 20px" variant="outline" @click="clearFilter">
            <template #icon>
              <ClearIcon />
            </template>
            清空
          </t-button>
        </t-space>
      </t-row>

      <t-table
        :data="data"
        :columns="columns"
        row-key="id"
        vertical-align="top"
        :hover="true"
        :pagination="pagination"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
        :sort="sort"
        :multipleSort="true"
        @change="reHandleChange"
        @select-change="reHandleSelectChange"
      >
        <template #saleStatus="{ row }">
          <t-tag v-if="row.saleStatus" :theme="row.saleStatus.code === 1 ? 'success':'warning'" variant="light">{{ row.saleStatus.text }}</t-tag>
        </template>
        <template #roomStatus="{ row }">
          <t-tag v-if="row.roomStatus" :theme="row.roomStatus.code === 1 ? 'success':'primary'" variant="light">{{ row.roomStatus.text }}</t-tag>
        </template>
        <template #empty>
          <t-space direction="vertical" style="text-align: center">
            <span>暂无数据</span>
            <t-button theme="default" @click="handleClickEdit">点击新建</t-button>
          </t-space>
        </template>
      </t-table>
      <t-row justify="end">
        <t-button theme="default" variant="base" @click="cancel">取消</t-button>
        <t-button theme="primary" @click="selected" :disabled="selectedRowKeys.length === 0">确认</t-button>
      </t-row>
    </t-card>

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
  </div>
</template>

<script lang="ts">
export default {
  name: 'roomSelect',
};
</script>

<script setup lang="ts">
import { ChevronRightIcon, ClearIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { PrimaryTableCol } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';

import hotelSelect from '@/pages/hotel/hotel/select.vue';
import floorSelect from '@/pages/hotel/floor/select.vue';
import buildingSelect from '@/pages/hotel/building/select.vue';

import ApiRoom from '@/api/hotel/ApiRoom';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';
import { queryDef, paginationDef } from '@/api/common/query';

const props = defineProps({
  mutilate: {
    type: Boolean,
    default: false,
  },
});

const showSelectHotel = ref(false);
const showSelectBuilding = ref(false);
const showSelectFloor = ref(false);

// 定义变量 start -------------------
const emit = defineEmits(['selected', 'cancel']);
// 读取设置内容
const store = useSettingStore();
// 排序
const sort = ref([
  {
    // 按照 sort 字段进行排序
    sortBy: '',
    // 是否按照降序进行排序
    descending: false,
  },
]);
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
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
// 表头
const columns: PrimaryTableCol[] = [
  { colKey: 'row-select', type: props.mutilate ? 'multiple' : 'single', width: 64, fixed: 'left' },
  { title: '所属楼层', colKey: 'floorName' },
  { title: '房间号', colKey: 'no' },
  { title: '所属房型', colKey: 'roomTypeName' },
  { title: '价格', colKey: 'price' },
  { title: '是否在售', colKey: 'saleStatus' },
  { title: '房间状态', colKey: 'roomStatus' },
];
// 定义变量 end -------------------

// 定义方法 start -------------------
// 获取列表数据
const getData = async (reload?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
    data.value = [];
  }
  dataLoading.value = true;
  ApiRoom.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;
    },
  });
};
// 表单选中数据变化
const reHandleSelectChange = (val: (string | number)[], e: any) => {
  selectedRowKeys.value = val;
  selectedRowData.value = e.selectedRowData;
};
// 排序变化
const sortChange = (sortInfo: any) => {
  // 对于受控属性而言，这里的赋值很重要，不可缺少
  sort.value = sortInfo;
  queryForm.value.orders = JSON.stringify(sort.value);
};
// 表单参数变化 包括过滤、分页
const reHandleChange = (changeParams: any, triggerAndData: unknown) => {
  pagination.value = changeParams.pagination;
  getData();
};
// 取消选择
const cancel = () => {
  emit('cancel');
};
// 确认选择
const selected = () => {
  emit('selected', props.mutilate ? selectedRowData.value : selectedRowData.value[0]);
};
// 处理表单顶部距离
const headerAffixedTop = computed(
  () =>
    ({
      offsetTop: store.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    }) as any,
);
// 新建数据
const handleClickEdit = () => {
  window.open(`#/hotel/room-edit?autoClose=true`);
};

const hotelSelected = (val: any) => {
  showSelectHotel.value = false;
  queryForm.value.hotelId = val.id;
  queryForm.value.hotelName = val.name;
  queryForm.value.buildingId = null;
  queryForm.value.buildingName = null;
  queryForm.value.floorId = null;
  queryForm.value.floorName = null;
  getData();
};

const buildingSelected = (val: any) => {
  showSelectBuilding.value = false;
  queryForm.value.buildingId = val.id;
  queryForm.value.buildingName = val.name;
  queryForm.value.floorId = null;
  queryForm.value.floorName = null;
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
  getData();
});
</script>

<style lang="less" scoped></style>
