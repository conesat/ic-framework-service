<template>
  <div class="room-info">
    <div v-if="roomDetail && roomDetail.id">
      <div class="title">基本信息</div>
      <div style="padding: 10px 0">
        <t-descriptions layout="vertical" :label-style="{ width: '90px' }">
          <t-descriptions-item label="所属楼栋">{{ roomDetail.buildingName }}</t-descriptions-item>
          <t-descriptions-item label="所属楼层">{{ roomDetail.floorName }}</t-descriptions-item>
          <t-descriptions-item label="房间编号">{{ roomDetail.no }}</t-descriptions-item>
          <t-descriptions-item label="房间状态">{{ roomDetail.roomStatus.text }}</t-descriptions-item>
          <t-descriptions-item label="销售状态">{{ roomDetail.saleStatus.text }}</t-descriptions-item>
        </t-descriptions>
      </div>
    </div>
    <div style="text-align: center; margin: 30px 0">
      <div style="margin-top: 20px">
        <t-button theme="primary" @click="selectRoomShow = true" size="small" variant="outline">
          <template #icon>
            <RelativityIcon />
          </template>
          关联房间
        </t-button>
        <t-button theme="primary" @click="toCreateRoom" size="small" variant="outline">
          <template #icon>
            <TableAddIcon />
          </template>
          新建房间
        </t-button>
        <t-button theme="primary" @click="toRoomState" size="small" v-if="roomDetail && roomDetail.id"
          variant="outline">
          <template #icon>
            <DashboardIcon />
          </template>
          看板
        </t-button>
        <t-button theme="primary" @click="toRoom" size="small" v-if="roomDetail && roomDetail.id" variant="outline">
          <template #icon>
            <Edit2Icon />
          </template>
          编辑
        </t-button>
      </div>
    </div>
    <div v-if="roomOrder && roomOrder.id">
      <div class="title">客户信息</div>
      <div style="padding: 10px 0">
        <t-descriptions layout="vertical">
          <t-descriptions-item label="姓名">{{ roomOrder.customerName }}</t-descriptions-item>
          <t-descriptions-item label="电话">{{ roomOrder.customerPhone }}</t-descriptions-item>
          <t-descriptions-item label="vip">
            <t-tag :theme="roomOrder.vip ? 'success' : 'warning'" variant="light">
              {{ roomOrder.vip ? 'VIP' : '普通客户' }}
            </t-tag>
          </t-descriptions-item>
          <t-descriptions-item label="入驻时间">{{ roomOrder.inDate }}</t-descriptions-item>
          <t-descriptions-item label="离开时间">{{ roomOrder.outDate }}</t-descriptions-item>
        </t-descriptions>
      </div>
      <div class="title">待处理订单</div>
      <div style="padding: 10px 0; max-width: 100%; overflow: hidden">
        <swiper v-if="saleServerOrderList.length > 0" :modules="[Navigation, Mousewheel]" :slides-per-view="'auto'"
          :mousewheel="undefined" :touchStartPreventDefault="false">
          <swiper-slide v-for="item in saleServerOrderList" :key="item.id" style="width: auto">
            <order-item :order="item" @click="toOrder(item.id)" style="margin: 0 10px"></order-item>
          </swiper-slide>
        </swiper>
        <div v-else>
          <t-empty description="暂无需要处理的订单" title="正常"></t-empty>
        </div>
      </div>
    </div>
    <t-dialog v-model:visible="selectRoomShow" header="选择房间" :footer="false" width="800">
      <room-select @selected="selectRoom" @cancel="selectRoomShow = false"></room-select>
    </t-dialog>
  </div>
</template>
<script setup lang="ts">
import { RelativityIcon, Edit2Icon, DashboardIcon, TableAddIcon } from 'tdesign-icons-vue-next';
import { onMounted, watch, ref } from 'vue';
import ApiRoom from '@/api/hotel/ApiRoom';
import { Mousewheel, Navigation } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';
import OrderItem from '@/pages/hotel/index/order-item.vue';
import ApiServerOrder from '@/api/hotel/ApiServerOrder';
import RoomSelect from '@/pages/hotel/room/select.vue';
import { MessagePlugin } from 'tdesign-vue-next';
import ApiRoomOrder from '@/api/hotel/ApiRoomOrder';

const selectRoomShow = ref(false);
const saleServerOrderList = ref([]);

const props = defineProps({
  extKey: {
    type: String,
    required: true,
  },
  buildingId: {
    type: String,
    required: true,
  },
  hotelId: {
    type: String,
    required: true,
  },
  floorId: {
    type: String,
    required: true,
  },
});
const roomOrder = ref({
  id: null,
  vip: false,
  userId: '',
  userName: '',
  customerName: '',
  customerPhone: '',
  roomPrice: '',
  actualRoomPrice: '',
  inDate: '',
  outDate: '',
  roomId: '',
  roomNo: '',
  createTime: '',
  updateTime: '',
});

const roomDetail = ref({
  id: null,
  preferentialPrice: null,
  weekPrice: null,
  price: null,
  no: '',
  nos: [],
  saleStatus: {
    code: 1,
    text: '',
  },
  roomStatus: {
    code: 1,
    text: '',
  },
  floorId: '',
  floorName: '',
  roomTypeId: '',
  extKey: '',
  picFiles: [],
  roomTypeName: '',
  createTime: '',
  updateTime: '',
  buildingName: '',
  supportingIds: [],
});
watch(
  () => props.extKey,
  async () => {
    getRoomDetail();
  },
);

const getRoomDetail = () => {
  ApiRoom.detailByExtKey({
    extKey: props.extKey,
    success: (res: any) => {
      roomDetail.value = res;
      if (res) {
        getServerOrders(res.id);
        getRoomOrder(res.id);
      }
    },
  });
};
const toOrder = (id: any) => {
  window.open(`#/hotel/server-order-edit?id=${id}`);
};

const toCreateRoom = () => {
  window.open(`#/hotel/room-edit?extKey=${props.extKey}&floorId=${props.floorId}`);
};

const toRoomState = () => {
  window.open(`#/hotel/room-state?id=${roomDetail.value.id}`);
};

const toRoom = () => {
  window.open(`#/hotel/room-edit?id=${roomDetail.value.id}`);
};

const selectRoom = (e: any) => {
  selectRoomShow.value = false;
  ApiRoom.updateExtKey({
    data: {
      roomId: e.id,
      extKey: props.extKey,
    },
    success: (res: any) => {
      MessagePlugin.success('关联成功');
      getRoomDetail();
    },
  });
};

const getServerOrders = (roomId: any) => {
  ApiServerOrder.all({
    data: {
      acceptTimeIsNull: true,
      roomId: roomId,
    },
    success: (res: any) => {
      saleServerOrderList.value = res;
    },
  });
};
const getRoomOrder = (roomId: any) => {
  ApiRoomOrder.roomNowOrder(roomId, {
    success: (res: any) => {
      roomOrder.value = res;
    },
  });
};

onMounted(() => {
  getRoomDetail();
});
</script>

<style scoped lang="less">
.title::before {
  content: '';
  /* 必须设置 content 属性，即使为空 */
  position: absolute;
  left: 0;
  border-radius: 10px;
  width: 4px;
  /* 小方块的宽度 */
  height: 14px;
  /* 小方块的高度 */
  background-color: #1c6ae4;
  /* 小方块的颜色 */
}

.title {
  line-height: 24px;
  font-weight: bolder;
  position: relative;
  padding-left: 10px;
  /* 为小方块留出空间 */
  display: flex;
  align-items: center;
  /* 垂直居中 */
}

.room-info::-webkit-scrollbar {
  display: none;
}
</style>
