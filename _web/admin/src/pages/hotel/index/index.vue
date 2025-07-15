<template>
  <div class="main" ref="main-wind" :class="maxWindow ? 'max' : ''"
    :style="{ height: maxWindow ? '100%' : height + 'px' }">
    <div style="position: absolute; left: 0; top: 0; right: 0; bottom: 0; transition: opacity 0.2s linear"
      :style="{ opacity: show3d ? 1 : 0 }">
      <iframe ref="iframeWindowRef" src="https://www.thingjs.com/pp/499231b33872e14dab67a9be"
        style="width: 100%; height: 100%; border: none; background: transparent"></iframe>
    </div>
    <div class="chart-main" style="pointer-events: none; transition: opacity 0.2s linear"
      :style="{ opacity: show3d ? 1 : 0 }">
      <div class="top" style="pointer-events: auto">
        <div @click="showSelectHotel = true" style="
            cursor: pointer;
            margin: 20px 0;
            font-weight: bold;
            font-size: 26px;
            display: flex;
            align-items: center;
          ">
          <SailingHotelIcon size="40px" />
          <div style="margin-left: 10px">
            <div style="height: 26px; line-height: 26px; word-break: normal">
              {{ hotelName }}
            </div>
            <div style="font-size: 14px; color: rgba(255, 255, 255, 0.7); margin-top: 6px">Power By Ice Framework</div>
          </div>
        </div>
        <div style="flex: 1; width: 0"></div>
        <div style="text-align: right; display: flex; align-items: center">
          <div style="margin-right: 10px; color: #cecece">
            <RainMediumIcon size="26" v-if="weather.cap.includes('雨')" />
            <SnowflakeIcon size="26" v-else-if="weather.cap.includes('雪')" />
            <SunnyIcon size="26" v-else-if="weather.cap.includes('晴')" />
            <CloudySunnyIcon size="26" v-else />
          </div>
          <div style="text-align: right">
            <div>{{ weather.cap }}</div>
            <div>{{ weather.temp }}°C</div>
          </div>
        </div>
        <div class="line-split"></div>
        <div style="text-align: right">
          <div>{{ getWeekday(new Date()) }}</div>
          <div>{{ formatDate(new Date(), 'YYYY/MM/DD') }}</div>
        </div>
        <div class="line-split"></div>
        <div style="font-family: monospace; font-size: 24px; text-align: center; color: rgba(255, 255, 255, 0.8)">
          <real-time-clock></real-time-clock>
        </div>
        <t-button style="margin-left: 20px" theme="default" variant="outline" shape="circle" @click="changeMax"
          v-if="!maxWindow">
          <Fullscreen2Icon />
        </t-button>
      </div>
      <div class="content">
        <div class="left" style="display: flex; flex-direction: column; pointer-events: auto">
          <div>
            <t-select v-model="buildingId" label="楼栋：" placeholder="请选择楼栋" auto-width>
              <t-option v-for="item in buildingList" :key="item.id" :value="item.id" :label="item.name"
                @change="getFloorList(item.id)"></t-option>
            </t-select>
          </div>
          <div style="flex: 1; display: flex; align-items: center">
            <t-space direction="vertical">
              <t-button v-for="item in floorList" class="floor-item"
                :theme="floorId === item.id ? 'primary' : 'default'" variant="outline" @click="changeFloor(item)">
                {{ item.name }}
              </t-button>
            </t-space>
          </div>
        </div>
        <div class="right" style="pointer-events: auto">
          <statistic :floorId="floorId" :buildingId="buildingId" v-if="!selectRoomExtKey"></statistic>
          <div v-if="selectRoomExtKey" style="display: flex; height: 100%; flex-direction: column">
            <div style="margin-bottom: 20px">
              <t-space align="center">
                <t-button theme="primary" variant="outline" @click="selectRoomExtKey = ''" size="small">
                  <template #icon>
                    <rollback-icon />
                  </template>
                  返回
                </t-button>
                <h3>房间信息</h3>
              </t-space>
            </div>
            <room-info style="flex: 1; height: 0; overflow: auto" :ext-key="selectRoomExtKey" :floor-id="floorId"
              :building-id="buildingId" :hotel-id="hotelId"></room-info>
          </div>
        </div>
      </div>
    </div>
    <transition name="fade">
      <div v-if="!show3d" style="
          position: absolute;
          left: 0;
          top: 0;
          right: 0;
          bottom: 0;
          text-align: center;
          display: flex;
          align-items: center;
          background: rgb(21 24 34);
          z-index: 9;
        ">
        <div style="height: 150px; width: 150px; margin: 0 auto" id="loading"></div>
      </div>
    </transition>
    <t-dialog v-model:visible="showSelectHotel" header="选择酒店" :footer="false" width="800">
      <hotel-select v-if="showSelectHotel" @cancel="showSelectHotel = false" @selected="hotelSelected"></hotel-select>
    </t-dialog>
  </div>
</template>
<script setup lang="ts">
import screenfull from 'screenfull';
import hotelSelect from '@/pages/hotel/hotel/select.vue';
import { nextTick, onMounted, ref, watch, onUnmounted } from 'vue';
import {
  SailingHotelIcon,
  Fullscreen2Icon,
  CloudySunnyIcon,
  SunnyIcon,
  SnowflakeIcon,
  RollbackIcon,
  RainMediumIcon,
} from 'tdesign-icons-vue-next';
import ApiBuilding from '@/api/hotel/ApiBuilding';
import ApiHotel from '@/api/hotel/ApiHotel';
import ApiFloor from '@/api/hotel/ApiFloor';
import ApiRoom from '@/api/hotel/ApiRoom';
import { MessagePlugin } from 'tdesign-vue-next';
import RealTimeClock from '@/pages/hotel/index/real-time-clock.vue';
import statistic from './statistic.vue';
import roomInfo from './room-info.vue';
import { formatDate, getWeekday } from '@/utils/date';
import lottie from 'lottie-web';
import animationData from '@/assets/hotel/lotties/loading-3d.json';

// 组件高度
const height = ref(0);
// 定义变量 start -------------------
const modelLoaded = ref(false); // 模型是否加载完成
const roomStateInterval = ref(null);
const showSelectHotel = ref(false);
const hotelList = ref([]);
const buildingList = ref([]);
const floorList = ref([]);
const selectRoomExtKey = ref('f_3_r_1');
const hotelId = ref('');
const hotelName = ref('');
const buildingId = ref('');
const floorId = ref('');
const extKeyFloorIdMap = ref<any>({});
const maxWindow = ref(false);
const show3d = ref(false);
const iframeWindowRef = ref(null);
const weather = ref({
  cap: '',
  temp: '',
  tempHi: '',
  tempLo: '',
});

const changeFloor = (item: any) => {
  floorId.value = item.id;
  // 发送消息给子页面
  iframeWindowRef.value.contentWindow.postMessage(
    {
      action: 'changeFloor',
      level: item.level - 1,
    },
    '*',
  );
};

const hotelSelected = (val: any) => {
  showSelectHotel.value = false;
  hotelName.value = val.name;
  hotelId.value = val.id;
};

const selectHotel = (item: any) => {
  hotelName.value = item.name;
  hotelId.value = item.id;
};

const changeMax = () => {
  maxWindow.value = true;
  if (screenfull.isEnabled) {
    screenfull.request();
  }
  MessagePlugin.success('按esc退出全屏');
};

const initLottie = () => {
  // 读取动画容器
  const lottieContainer = document.getElementById('loading');
  // 实例化
  lottie.loadAnimation({
    // UED 提供的 动画的 json 文件
    animationData: animationData,
    // 渲染方式
    renderer: 'svg',
    // 是否循环
    loop: true,
    autoplay: true, // 自动播放
    container: lottieContainer, // 用于渲染的容器
  });
};

const getFloorList = (buildingId: string) => {
  ApiFloor.all({
    data: {
      buildingId: buildingId,
    },
    success: (res: any) => {
      if (res.length > 0) {
        floorId.value = res[0].id;
      }
      res.forEach((item: any) => {
        extKeyFloorIdMap.value[item.extKey] = item.id;
      });
      floorList.value = res;
    },
  });
};
const getBuildingList = (hotelId: string) => {
  ApiBuilding.all({
    data: {
      hotelId: hotelId,
    },
    success: (res: any) => {
      buildingList.value = res;
      if (res.length > 0) {
        buildingId.value = res[0].id;
        getFloorList(res[0].id);
        getRoomState();
      }
    },
  });
};
const getHotelList = () => {
  ApiHotel.all({
    success: (res: any) => {
      hotelList.value = res;
      if (res.length > 0) {
        selectHotel(res[0]);
        getBuildingList(res[0].id);
      }
    },
  });
};

const getRoomState = () => {
  if (!buildingId.value || !modelLoaded.value || !iframeWindowRef.value) {
    return;
  }
  ApiRoom.buildRoom({
    data: {
      buildingId: buildingId.value,
    },
    success: (r: any[]) => {
      r.forEach((item: any) => {
        if (!iframeWindowRef.value || !iframeWindowRef.value.contentWindow) {
          clearInterval(roomStateInterval.value);
          return;
        }
        if (item.roomStatus.code == 2) {
          // 发送消息给子页面
          iframeWindowRef.value.contentWindow.postMessage(
            {
              action: 'highlight',
              roomId: item.extKey,
              tip: item.roomStatus.text,
              color: '#189fff',
              sparkle: false,
              tipColor: '#7fc8ff',
              tipShadow: 'transparent',
            },
            '*',
          );
        } else if (item.roomStatus.code != 1) {
          // 发送消息给子页面
          iframeWindowRef.value.contentWindow.postMessage(
            {
              action: 'highlight',
              roomId: item.extKey,
              tip: item.roomStatus.text,
              sparkle: true,
              color: '#ffa318',
              tipColor: '#ff1818',
              tipShadow: '#ffdf71',
            },
            '*',
          );
        } else {
          iframeWindowRef.value.contentWindow.postMessage(
            {
              action: 'clear',
              roomId: item.extKey,
            },
            '*',
          );
        }
      });
    },
  });
};

const getWeather = () => {
  try {
    const AK = import.meta.env.VITE_APP_BAIDU_MAP_AK;
    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = `http://api.map.baidu.com/api?v=3.0&ak=${AK}`;
    script.onload = () => {
      let geolocation = new (window as any).BMap.Geolocation();
      geolocation.getCurrentPosition(function (r: any) {
        let latitude = r.latitude; // 获取纬度
        let longitude = r.longitude; // 获取经度
        fetch(
          `https://api.msn.cn/msn/v0/pages/weather/overview?apikey=j5i4gDqHL6nGYwx5wi5kRhXjtf2c5qgFX9fzfk0TOo&ocid=msftweather&cm=zh-cn&it=edgeid&scn=APP_ANON&units=C&appId=9e21380c-ff19-4c78-b4ea-19558e93a5d3&wrapodata=false&includemapsmetadata=true&cuthour=true&lifeDays=2&lifeModes=18&includestorm=true&includeLifeActivity=true&distanceinkm=0&regionDataCount=20&orderby=distance&days=1&source=weather_csr&region=cn&market=zh-cn&locale=zh-cn&lat=${latitude}&lon=${longitude}`,
        )
          .then((response) => response.json())
          .then((res) => {
            try {
              weather.value = {
                cap: res.responses[0].weather[0].current.cap,
                temp: res.responses[0].weather[0].current.temp,
                tempHi: res.responses[0].weather[0].forecast.days[0].daily.tempHi,
                tempLo: res.responses[0].weather[0].forecast.days[0].daily.tempLo,
              };
            } catch (error) {
              console.log(error);
            }
          });
      });
    };
    document.head.appendChild(script);
  } catch (error) {
    console.log(error);
  }
};

onMounted(() => {
  show3d.value = false;
  getHotelList();
  getWeather();
  initLottie();
  document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
      maxWindow.value = false;
    }
  });
  screenfull.on('change', () => {
    if (!screenfull.isFullscreen) {
      maxWindow.value = false;
    }
  });

  nextTick(() => {
    height.value = document.body.offsetHeight - 160;
  });
  window.addEventListener('resize', () => {
    height.value = document.body.offsetHeight - 160;
  });

  // 监听来自 iframe 的消息
  window.addEventListener('message', (event) => {
    if (event.data.action == 'changeFloorEvent') {
      floorId.value = extKeyFloorIdMap.value[event.data.floorId];
    } else if (event.data.action == 'loaded') {
      floorId.value = extKeyFloorIdMap.value[event.data.floorId];
      modelLoaded.value = true;
      getRoomState();

      setTimeout(() => {
        show3d.value = true;
      }, 2000);
    } else if (event.data.action == 'roomClick') {
      selectRoomExtKey.value = event.data.roomId;
    }
  });

  roomStateInterval.value = setInterval(() => {
    getRoomState();
  }, 3000);
});
watch(
  () => floorId.value,
  () => {
    selectRoomExtKey.value = '';
  },
);
watch(
  () => buildingId.value,
  () => {
    selectRoomExtKey.value = '';
  },
);
onUnmounted(() => {
  show3d.value = false;
  clearInterval(roomStateInterval.value);
});
</script>

<style scoped lang="less">
.main.max {
  background: #000;
  position: fixed;
  z-index: 992;
  left: 0;
  right: 0;
  bottom: 0;
  top: 0;
  height: auto;
}

.main {
  transition: 0.2s;
  color: #f9f9f9;
  height: 800px;
  background: #000;
  width: 100%;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.chart-main {
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  display: flex;
  flex-direction: column;

  .top {
    padding: 0 20px;
    width: 100%;
    display: flex;
    align-items: center;

    .line-split {
      margin: 0 20px;
      width: 1px;
      height: 20px;
      background: rgba(255, 255, 255, 0.5);
    }
  }

  .content {
    flex: 1;
    position: relative;
    width: 100%;

    .left {
      padding: 20px;
      position: absolute;
      top: 0;
      left: 0;
      width: 200px;
      height: 100%;
    }

    .right::-webkit-scrollbar {
      display: none;
    }

    .right {
      padding: 0 20px;
      box-sizing: border-box;
      position: absolute;
      width: 400px;
      top: 0;
      right: 0;
      height: 100%;
      overflow: auto;
    }
  }
}

.fade-leave-active {
  transition: opacity 1s linear;
}

.fade-leave-to {
  opacity: 0;
}

.fade-leave-from {
  opacity: 1;
}
</style>
