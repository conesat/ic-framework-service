<template>
  <div class="statistic">
    <div>
      <div class="title">待处理订单</div>
      <div style="padding: 10px 0; max-width: 100%; overflow: hidden">
        <swiper v-if="saleServerOrderList.length > 0" :modules="[Navigation, Mousewheel]" :slides-per-view="'auto'"
          :mousewheel="true" :touchStartPreventDefault="false">
          <swiper-slide v-for="item in saleServerOrderList" :key="item.id" style="width: auto">
            <order-item :order="item" @click="toOrder(item.id)" style="margin: 0 10px"></order-item>
          </swiper-slide>
        </swiper>
        <div v-else>
          <t-empty description="暂无需要处理的订单" title="正常"></t-empty>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <div class="title">待清洁房间</div>
      <div style="padding: 10px 0; max-width: 100%; overflow: hidden">
        <swiper v-if="clearServerOrderList.length > 0" :modules="[Navigation, Mousewheel]" :slides-per-view="'auto'"
          :mousewheel="true" :touchStartPreventDefault="false">
          <swiper-slide v-for="item in clearServerOrderList" :key="item.id" style="width: auto">
            <clear-server-order-item :order="item" @click="toOrder(item.id)"
              style="margin: 0 10px"></clear-server-order-item>
          </swiper-slide>
        </swiper>
        <div v-else>
          <t-empty description="暂无需要清洁的房间" title="正常"></t-empty>
        </div>
      </div>
    </div>

    <div style="margin-top: 10px">
      <div class="title">
        <div style="margin-right: 10px">房间状态</div>
        <t-radio-group v-model="statusTab" @change="changeStatusTab" size="small">
          <t-radio-button value="floor">楼层</t-radio-button>
          <t-radio-button value="building">楼栋</t-radio-button>
        </t-radio-group>
      </div>
      <div v-show="statusTab == 'floor'" ref="floorRoomStateChartRef"
        style="width: 400px; height: 300px; margin: 0 auto"></div>
      <div v-show="statusTab == 'building'" ref="buildRoomStateChartRef"
        style="width: 400px; height: 300px; margin: 0 auto"></div>
    </div>
    <div style="margin-top: 10px">
      <div class="title">同比入住人数</div>
      <div ref="liveOnChartRef" style="width: 100%; height: 300px"></div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { Mousewheel, Navigation } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';
import OrderItem from '@/pages/hotel/index/order-item.vue';
import ClearServerOrderItem from '@/pages/hotel/index/clear-server-order-item.vue';
import { onMounted, ref, watch } from 'vue';
import ApiServerOrder from '@/api/hotel/ApiServerOrder';
import ApiRoom from '@/api/hotel/ApiRoom';
import { initLineChart, initStateChart } from '@/pages/hotel/index/charts/charts';
import 'swiper/css';
import 'swiper/css/navigation';

const props = defineProps({
  floorId: {
    type: String,
    required: true,
  },
  buildingId: {
    type: String,
    required: true,
  },
});

const statusTab = ref('floor');
const floorRoomStateChartRef = ref(null);
const buildRoomStateChartRef = ref(null);
const floorRoomStateChart = ref(null);
const buildRoomStateChart = ref(null);
const saleServerOrderList = ref([]);
const clearServerOrderList = ref([]);
const liveOnChartRef = ref(null);
const liveOnChart = ref(null);

watch(
  () => props.buildingId,
  () => {
    staticsBuildRoomStatus();
  },
);

watch(
  () => props.floorId,
  () => {
    getFloorServerOrders();
    staticsFloorRoomStatus(props.floorId, null);
  },
);

/**
 * 切换楼层状态
 */
const changeStatusTab = () => {
  if (statusTab.value == 'floor') {
    staticsFloorRoomStatus(props.floorId, null);
  } else {
    staticsFloorRoomStatus(null, props.buildingId);
  }
};

/**
 * 获取房间状态
 * @param floorId
 * @param buildingId
 */
const staticsFloorRoomStatus = (floorId: string, buildingId: string) => {
  if (!floorId && !buildingId) {
    return;
  }
  ApiRoom.statics({
    data: {
      floorId: floorId,
      buildingId: buildingId,
    },
    success: (r) => {
      const data: any = [];
      r.forEach((item: any) => {
        data.push({
          name: item.status.text,
          value: item.count,
        });
      });
      data.sort((a: any, b: any) => {
        return b.value - a.value;
      });
      if (statusTab.value == 'floor') {
        floorRoomStateChart.value = initStateChart(floorRoomStateChartRef.value, data, floorRoomStateChart.value);
      } else {
        buildRoomStateChart.value = initStateChart(buildRoomStateChartRef.value, data, buildRoomStateChart.value);
      }
    },
  });
};
const getFloorServerOrders = () => {
  if (!props.floorId) {
    return;
  }
  ApiServerOrder.all({
    data: {
      acceptTimeIsNull: true,
      floorId: props.floorId,
    },
    success: (res: any) => {
      clearServerOrderList.value = [];
      saleServerOrderList.value = [];
      res.forEach((item: any) => {
        if (item.target.code === 1) {
          clearServerOrderList.value.push(item);
        } else if (item.target.code === 2) {
          saleServerOrderList.value.push(item);
        }
      });
    },
  });
};

const toOrder = (id: any) => {
  window.open(`#/hotel/server-order-edit?id=${id}`);
};

const staticsBuildRoomStatus = () => {
  if (!props.buildingId) {
    return;
  }
  ApiRoom.statics({
    data: {
      buildingId: props.buildingId,
    },
    success: (r: any[]) => {
      const data: any = [];
      r.forEach((item) => {
        data.push({
          name: item.status.text,
          value: item.count,
        });
      });
      data.sort((a: any, b: any) => {
        return b.value - a.value;
      });
      buildRoomStateChart.value = initStateChart(buildRoomStateChartRef.value, data, buildRoomStateChart.value);
    },
  });
};

const renderChart = () => {
  renderLiveOnChart();
};
const renderLiveOnChart = () => {
  liveOnChart.value = initLineChart(liveOnChartRef.value, {}, liveOnChart.value);
};

onMounted(() => {
  renderChart();
  changeStatusTab();
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
</style>
