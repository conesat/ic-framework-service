<template>
  <t-loading :loading="dataLoading">
    <div class="organizationTrend">
      <div v-if="dataList.length === 0" style="width: 100%;text-align: center;margin-top: 20px">
        <t-space :direction="'vertical'" style="margin: 20px auto">
          <component style="opacity: .3;" :is="EmptyListIcon"></component>
          <h2 style="text-align: center">暂无数据</h2>
        </t-space>
      </div>
      <div class="main" :style="{maxHeight:height+'px'}" v-if="dataList.length > 0">
        <table cellspacing="0">
          <thead>
          <tr>
            <th style="width: 100px">房间</th>
            <th v-for="(item,index) in titleList" :key="index" style="width: 100px"
                :class="{'week-line' : weekList.indexOf(index) >= 0}">
              <div class="title-date" :class="{'week':item.week}">
                <span> {{ item.date.substring(5) }} {{ item.dayOfWeek }}</span>
              </div>
            </th>
          </tr>
          </thead>
          <tbody>
          <!-- 列表数据 -->
          <tr v-for="(item,index) in dataList" :key="index">
            <td style="cursor: pointer" @click="toRoom(item.room.id)">
              <t-tooltip :content="`${item.room.hotelName}-${item.room.buildingName}-${item.room.floorName}`">
                <div class="room-name">
                  {{ item.room.floorName }}/{{ item.room.no }}
                </div>
                <div class="room-type">
                  {{ item.room.roomTypeName }}
                </div>
              </t-tooltip>
            </td>
            <template v-for="(status, index) in item.items">
              <td class="item"
                  v-if="status.show"
                  :class="{'week-line' : status.dayOfWeek === 6,
                'active' : selectedRoomId === item.room.id +'-'+ index,
                'order': status.order,
                'close': status.saleStatus.code === 0,
                'check-in':status.order && status.order.checkIn}"
                  :colspan="status.order ? status.order.remainingDay + 1:1"
              >
                <t-dropdown
                  trigger="click" placement="right" @click="clickRoomOprHandler">
                  <div style="height: 100%;width: 100%">
                    <div @click="selectRoom(item, index)" v-if="status.saleStatus.code === 1 && !status.order">
                      <div class="empty-price">￥{{ status.price }}</div>
                      <div class="empty-room">空房</div>
                    </div>
                    <div @click="selectRoom(item, index)" v-else-if="status.order">
                      <div class="empty-price">{{ status.order.customerName || status.order.customerPhone }}</div>
                    </div>
                    <div @click="selectRoom(item, index)" v-else-if="status.saleStatus.code === 0">
                      <div class="empty-room">关闭</div>
                    </div>
                  </div>

                  <t-dropdown-menu>
                    <t-dropdown-item value="edit">
                      {{ status.order ? (status.order.checkIn ? '修改订单' : '修改预约') : '预约' }}
                    </t-dropdown-item>
                    <t-dropdown-item value="cancel" v-if="status.order && !status.order.checkIn">
                      取消预约
                    </t-dropdown-item>
                    <t-dropdown-item value="cancel" v-if="status.order && status.order.checkIn">
                      取消订单
                    </t-dropdown-item>
                    <t-dropdown-item value="clear">
                      保洁
                    </t-dropdown-item>
                    <t-dropdown-item value="setting">
                      设置
                    </t-dropdown-item>
                  </t-dropdown-menu>
                </t-dropdown>
              </td>
            </template>

          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <t-drawer :visible.sync="showRoomPriceOpr"
              size="340"
              header="房价设置"
              :closeOnOverlayClick="true"
              :closeBtn="true"
              @cancel="showRoomPriceOpr = false"
              @closeBtnClick="showRoomPriceOpr = false"
              @overlayClick="showRoomPriceOpr = false"
              @confirm="updateRoomPrice">
      <t-form labelAlign="top" ref="roomSettingFormRef" v-if="showRoomPriceOpr"
              :rules="ROOM_SETTING_FORM_RULES" :data="roomSettingForm">
        <t-form-item label="所属酒店">
          <t-tag> {{ selectedRoomData.hotelName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.buildingName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.floorName }}</t-tag>
        </t-form-item>
        <t-form-item label="所属房型">
          <t-tag> {{ selectedRoomData.roomTypeName }}</t-tag>
        </t-form-item>
        <t-form-item label="批量设置" name="dates" requiredMark>
          <t-date-range-picker v-model="roomDateRange"
                               :disable-date="{before: dayjs().subtract(1, 'day').format()}"/>
        </t-form-item>
        <t-form-item label="门市价（元）" name="price">
          <t-input-number :min="0" v-model="roomSettingForm.price" placeholder="请输入价格，如：100"></t-input-number>
        </t-form-item>
      </t-form>
    </t-drawer>
    <t-drawer :visible.sync="showRoomStateOpr"
              size="340"
              header="保洁设置"
              :closeOnOverlayClick="true"
              :closeBtn="true"
              @cancel="showRoomStateOpr = false"
              @closeBtnClick="showRoomStateOpr = false"
              @overlayClick="showRoomStateOpr = false"
              @confirm="updateRoomState">
      <t-form labelAlign="top" ref="roomSettingFormRef" v-if="showRoomStateOpr"
              :rules="ROOM_SETTING_FORM_RULES" :data="roomSettingForm">
        <t-form-item label="所属酒店">
          <t-tag> {{ selectedRoomData.hotelName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.buildingName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.floorName }}</t-tag>
        </t-form-item>
        <t-form-item label="所属房型">
          <t-tag> {{ selectedRoomData.roomTypeName }}</t-tag>
        </t-form-item>
        <t-form-item label="批量设置" name="dates" requiredMark>
          <t-date-range-picker v-model="roomDateRange"
                               :disable-date="{before: dayjs().subtract(1, 'day').format()}"/>
        </t-form-item>

        <t-form-item label="是否需要保洁" name="needClear">
          <div>
            <t-checkbox v-model="roomSettingForm.needClear">
              需要保洁
            </t-checkbox>
            <br/>
            <div style="color: var(--td-text-color-secondary)">如果不勾选-将清空指定日期保洁任务</div>
          </div>
        </t-form-item>
        <t-form-item label="保洁员【如果不选择，则自动分配】" name="clearUserId" v-if="roomSettingForm.needClear">
          <t-input v-model="roomSettingForm.clearUserName" placeholder="请选择保洁员"
                   clearable
                   @clear="(e) => {roomSettingForm.clearUserId = null;roomSettingForm.clearUserName=null;e.e.stopPropagation();}"
                   @focus="() => showSelectClearUser = true"/>
        </t-form-item>
      </t-form>
    </t-drawer>
    <t-drawer :visible.sync="showRoomOrderOpr"
              size="400"
              header="房间预定"
              :closeOnOverlayClick="true"
              :closeBtn="true"
              @closeBtnClick="showRoomOrderOpr = false"
              @overlayClick="showRoomOrderOpr = false">
      <t-form labelAlign="top" ref="roomOrderFormRef" v-if="showRoomOrderOpr"
              :rules="ROOM_ORDER_FORM_RULES" :data="roomOrderForm">
        <t-form-item label="所属酒店">
          <t-tag> {{ selectedRoomData.hotelName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.buildingName }}</t-tag>
          /
          <t-tag> {{ selectedRoomData.floorName }}</t-tag>
        </t-form-item>
        <t-form-item label="所属房型">
          <t-tag> {{ selectedRoomData.roomTypeName }}</t-tag>
        </t-form-item>

        <div style="display: flex">
          <t-form-item label="手机号" name="customerPhone" style="flex: 1">
            <t-auto-complete
              v-model="roomOrderForm.customerPhone"
              :options="hotelUsers"
              @change="getHotelUser"
              :popup-props="{ overlayClassName: 't-hotel-user-autocomplete-option-list' }"
              placeholder="请输入手机号"
            >
              <template #option="{ option }">
                <div class="custom-option" style="display: flex" @click="selectHotelUser(option)">
                  <t-image style="width: 20px;height: 20px" :src="option.avatarFileUrl"/>
                  <div class="custom-option__main">
                    <div class="text">{{ option.phone }}</div>
                    <div class="description">{{ option.name }}</div>
                  </div>
                </div>
              </template>
            </t-auto-complete>
          </t-form-item>
          <div style="width: 10px"></div>
          <t-form-item label="姓名" name="customerName" style="flex: 1">
            <t-input v-model="roomOrderForm.customerName" placeholder="请输入姓名，如：张三"></t-input>
          </t-form-item>
        </div>

        <t-form-item label="入住时间" name="dates" requiredMark style="width: 100%">
          <t-date-range-picker v-model="roomDateRange" style="width: 100%"
                               @change="changeDate"
                               :disable-date="{before: dayjs().subtract(1, 'day').format()}"/>
        </t-form-item>

        <div style="display: flex;">
          <t-form-item label="是否已支付" name="payed" style="flex: 1">
            <t-checkbox v-model="roomOrderForm.payed">{{ roomOrderForm.payed ? '已支付' : '未支付' }}</t-checkbox>
          </t-form-item>
          <t-form-item label="是否已入住" name="checkIn" v-show="nowDate === selectedDate" style="flex: 1">
            <t-checkbox v-model="roomOrderForm.checkIn">{{ roomOrderForm.checkIn ? '已入住' : '未入住' }}</t-checkbox>
          </t-form-item>
        </div>

        <t-form-item label="住房总价">
          <t-tag> ￥{{ roomOrderForm.actualRoomPrice }}</t-tag>
        </t-form-item>

        <t-form-item label="用户类型">
          <t-tag :theme="roomOrderForm.vip ? 'danger':'default'"> {{ roomOrderForm.vip ? 'VIP' : '普通用户' }}</t-tag>
        </t-form-item>

        <t-form-item label="备注" name="remark">
          <t-textarea v-model="roomOrderForm.remark" placeholder="备注信息，如：客户需要3点入住"></t-textarea>
        </t-form-item>
        <t-form-item v-if="selectedRoomItemDetailData.order && selectedRoomItemDetailData.order.id">
          <div style="width: 100%;margin-top: 20px">
            <t-button size="small" @click="showRoomConsumeOrderDialog">添加其他消费订单</t-button>
            <div style="width: 100%;margin-top: 10px" v-for="(item,index) in roomConsumeOrders">
              <div style="display: flex;align-items: center">
                <div>
                  <t-image
                    :src="item.fileUrl"
                    style="width: 40px;height: 40px"
                    fit="cover"
                    shape="round"
                  >
                    <template #error>
                      <image-error-icon size="30"></image-error-icon>
                    </template>
                  </t-image>
                </div>
                <div style="flex: 1;width: 0;margin: 0 10px;text-align: left">
                  <div style="flex: 1;font-weight: bold;">{{ item.customerItemName }}</div>
                  <div style="display: flex;font-size: 11px;color: var(--td-text-color-secondary);align-items: center;">
                    <div>数量：{{ item.num }}</div>
                    <div style="width: 10px"></div>
                    <div>总价：￥{{ item.price }}</div>
                    <div style="width: 0;flex: 1"></div>
                    <div>{{ item.createTime }}</div>
                  </div>
                </div>
                <t-button size="small" theme="warning" @click="deleteConsumeOrderItem(item.id)">删除</t-button>
              </div>
            </div>
          </div>
        </t-form-item>
      </t-form>
      <template #footer>
        <t-button @click="creatOrder" theme="primary">
          {{ selectedRoomItemDetailData.order && selectedRoomItemDetailData.order.id ? '确认修改' : '确定' }}
        </t-button>
        <t-button theme="default" @click="deleteOrder(selectedRoomItemDetailData.order.id)"
                  v-if="selectedRoomItemDetailData.order && selectedRoomItemDetailData.order.id">
          {{ selectedRoomItemDetailData.order.checkIn ? '取消订单' : '取消预约' }}
        </t-button>
        <t-button theme="default" @click="showRoomOrderOpr = false"> 关闭</t-button>
      </template>
    </t-drawer>
    <div style="margin-top: 10px">
      <t-pagination :total="queryForm.total" :current="queryForm.pageIndex" :page-size="queryForm.pageSize"
                    @change="changePage"/>
    </div>


    <t-dialog v-model:visible="showRoomConsumeOrder" header="创建订单" :footer="false" width="800">
      <room-consume-order-edit v-if="showRoomConsumeOrder && selectedRoomItemDetailData.order"
                               :room-order-id="selectedRoomItemDetailData.order.id"
                               @success="getConsumeOrderItems(selectedRoomItemDetailData.order.id)"
      />
    </t-dialog>
    <t-dialog v-model:visible="showSelectClearUser" header="选择用户" :footer="false" width="800"
              @cancel="showSelectClearUser = false">
      <user-select
        v-if="showSelectClearUser"
        :pos-signs="['P6']"
        @cancel="showSelectClearUser = false"
        @selected="onUserSelected"></user-select>
    </t-dialog>
  </t-loading>
</template>

<script lang="ts">
export default {
  name: "RoomStatusTable"
}
</script>

<script lang="ts" setup>
// 引入数据
import {nextTick, onMounted, ref, watch} from "vue";
import EmptyListIcon from "@/assets/svg-big/empty-list.svg";
import dayjs from 'dayjs';
import ApiRoomOrder from '@/api/hotel/ApiRoomOrder';
import ApiHotelUser from '@/api/hotel/ApiHotelUser';
import ApiRoomConsumeOrder from '@/api/hotel/ApiRoomConsumeOrder';
import ApiRoom from '@/api/hotel/ApiRoom';
import RoomConsumeOrderEdit from '@/pages/hotel/room-consume-order/edit-dialog.vue';
import {AutoCompleteProps, CustomValidator, FormRule} from "tdesign-vue-next";
import UserSelect from "@/pages/sys/user/select.vue";
import ApiServerOrder from "@/api/hotel/ApiServerOrder";
import {ImageErrorIcon} from "tdesign-icons-vue-next";
import {queryDef, paginationDef} from "@/api/common/query";
import {useRouter} from 'vue-router';
// 路由
const router = useRouter();
const props = defineProps({
  hotelId: {
    type: String,
  },
  buildingId: {
    type: String,
  },
  floorId: {
    type: String,
  },
  searchKey: {
    type: String,
  }
});

const hotelUsers = ref<AutoCompleteProps['options']>([])
watch(() => props.hotelId, () => {
  getData();
});
watch(() => props.buildingId, () => {
  getData();
});
watch(() => props.floorId, () => {
  getData();
});
const dateRangeValidator: any = (val: any) => {
  if (!roomSettingForm.value.dateStart || !roomSettingForm.value.dateEnd) {
    return {
      result: false,
      message: '日期必填',
      type: 'error',
    };
  }
  return {
    result: true,
    type: 'success',
  };
};
// 表单校验
const ROOM_SETTING_FORM_RULES: Record<string, FormRule[]> = {
  dates: [{validator: dateRangeValidator}],
  price: [{required: true, message: '门市价不能为空', type: 'error', trigger: 'change'}],
};
const ROOM_ORDER_FORM_RULES: Record<string, FormRule[]> = {
  dates: [{validator: dateRangeValidator}],
  customerName: [{required: true, message: '姓名不能为空', type: 'error', trigger: 'change'}],
  payed: [{required: true, boolean: true, message: '支付状态不能为空', type: 'error', trigger: 'change'}],
  checkIn: [{required: true, boolean: true, message: '入住状态不能为空', type: 'error', trigger: 'change'}],
  customerPhone: [{required: true, message: '手机号不能为空', type: 'error', trigger: 'change'}],
};

const queryForm = ref({
  ...queryDef,
  total: 0,
  startDate: '',
  endDate: '',
  hotelId: '',
  hotelName: '',
  buildingId: '',
  buildingName: '',
  floorId: '',
  floorName: '',
});

const showSelectClearUser = ref(false)
const roomSettingForm = ref({
  roomId: null,
  price: null,
  dateStart: '',
  dateEnd: '',
  clearUserName: '',
  clearUserId: '',
  needClear: false
});
const roomOrderForm: any = ref({
  id: null,
  roomId: null,
  customerName: null,
  userId: null,
  payed: true,
  vip: false,
  checkIn: false,
  customerPhone: null,
  actualRoomPrice: 0,
  remark: '',
  inDate: '',
  outDate: ''
});

const roomDateRange: any = ref(['', '']);
const selectedRoomId: any = ref('');
const selectedRoomItems: any = ref({});
const selectedRoomData: any = ref({});
const selectedRoomItemDetailData: any = ref({});
const selectedIndex = ref(0);
const selectedDate = ref('');
const roomSettingFormRef = ref(null);
const roomOrderFormRef = ref(null);

const roomConsumeOrders = ref([]);
const showRoomConsumeOrder = ref(false);
const showRoomPriceOpr = ref(false);
const showRoomStateOpr = ref(false);
const showRoomOrderOpr = ref(false);
const dataLoading = ref(false);
// 组件高度
const height = ref(0);
// 头部列表
const titleList: any = ref([])
// 合计数据 头部数据
const totalData: any = ref([])
// 合计数据 头部数据
const dataList: any = ref([])
// 周末分界线
const weekList: any = ref([])
// 周末分界线
const nowDate: any = ref('')
let now = new Date();
now.setHours(0)
now.setMinutes(0)
now.setMilliseconds(0)


let end = new Date();
end.setHours(0)
end.setMinutes(0)
end.setMilliseconds(0)
end.setDate(end.getDate() + 30)


const toRoom = (id: any) => {
  router.push(`/hotel/room-state?id=${id ? id : ''}`);
}

const changeDate = (e: any) => {
  updateActualRoomPrice(e[0], e[1])
}
const updateActualRoomPrice = (start: string, end: string) => {
  // 将字符串转换为Date对象
  const d1 = new Date(start);
  const d2 = new Date(end);
  // 计算两个日期之间的毫秒差
  const diffInMs = Math.abs(d1.getTime() - d2.getTime());
  // 将毫秒差转换为天数
  const diffInDays = diffInMs / (1000 * 60 * 60 * 24);
  //
  roomOrderForm.value.actualRoomPrice = 0
  for (let i = selectedIndex.value; i < selectedIndex.value + diffInDays; i++) {
    roomOrderForm.value.actualRoomPrice += selectedRoomItems.value[i].price
  }
}


const showRoomConsumeOrderDialog = () => {
  showRoomConsumeOrder.value = true
}

const clickRoomOprHandler = (v: any) => {
  if (v.value === 'edit') {
    if (selectedRoomItemDetailData.value.order) {
      roomOrderForm.value = selectedRoomItemDetailData.value.order
      roomDateRange.value = [roomOrderForm.value.inDate, roomOrderForm.value.outDate]
      getConsumeOrderItems(selectedRoomItemDetailData.value.order.id)
    } else {
      roomOrderForm.value = {}
      roomOrderForm.value.payed = false
      roomOrderForm.value.checkIn = false
      updateActualRoomPrice(roomDateRange.value[0], roomDateRange.value[1])
    }
    roomOrderForm.value.roomId = selectedRoomData.value.id;
    showRoomOrderOpr.value = true
  } else if (v.value === 'cancel') {
    deleteOrder(selectedRoomItemDetailData.value.order.id);
  } else if (v.value === 'setting' || v.value === 'clear') {
    roomSettingForm.value.dateEnd = roomSettingForm.value.dateStart
    roomDateRange.value = [roomSettingForm.value.dateStart, roomSettingForm.value.dateEnd]
    showRoomPriceOpr.value = v.value === 'setting'
    showRoomStateOpr.value = v.value === 'clear'

    if (v.value === 'clear') {
      ApiServerOrder.page({
        data: {
          target: 1,
          targetId: selectedRoomData.value.id,
          doTimeGe: roomSettingForm.value.dateStart + ' 00:00:00',
          doTimeLe: roomSettingForm.value.dateStart + ' 23:59:59'
        },
        success: (res) => {
          if (!res || !res.records || res.records.length === 0) {
            return
          }
          roomSettingForm.value.needClear = true
          roomSettingForm.value.clearUserName = res.records[0].serverUserId
          roomSettingForm.value.clearUserName = res.records[0].serverUserName
        }
      })
    }
  }
}
const deleteOrder = (id: any) => {
  ApiRoomOrder.delete({
    ids: [id],
    success: (res: any) => {
      selectedRoomId.value = ''
      showRoomOrderOpr.value = false
      getData()
    }
  })
}

const updateRoomPrice = () => {
  roomSettingForm.value.dateStart = roomDateRange.value[0];
  roomSettingForm.value.dateEnd = roomDateRange.value[1];
  roomSettingFormRef.value.validate().then((res: any) => {
    if (res === true) {
      ApiRoom.updatePrice({
        data: roomSettingForm.value,
        success: () => {
          showRoomPriceOpr.value = false
          getData()
        }
      })
    }
  })
}

const updateRoomState = () => {
  roomSettingForm.value.dateStart = roomDateRange.value[0];
  roomSettingForm.value.dateEnd = roomDateRange.value[1];
  roomSettingFormRef.value.validate().then((res: any) => {
    if (res === true) {
      ApiRoom.updateState({
        data: roomSettingForm.value,
        success: () => {
          showRoomStateOpr.value = false
          getData()
        }
      })
    }
  })
}

const onUserSelected = (val: any) => {
  showSelectClearUser.value = false;
  roomSettingForm.value.clearUserId = val.id
  roomSettingForm.value.clearUserName = val.name
};

const creatOrder = () => {
  roomOrderForm.value.inDate = roomDateRange.value[0];
  roomOrderForm.value.outDate = roomDateRange.value[1];
  roomOrderFormRef.value.validate().then((res: any) => {
    if (res === true) {
      ApiRoomOrder.edit({
        data: roomOrderForm.value,
        success: (res: any) => {
          showRoomOrderOpr.value = false
          getData()
        }
      })
    }
  })
}

/**
 * 选中项目
 * @param item
 * @param index
 */
const selectRoom = (item: any, index: any) => {
  selectedRoomId.value = item.room.id + '-' + index;
  selectedIndex.value = index;
  selectedDate.value = titleList.value[index].date;
  selectedRoomData.value = item.room
  selectedRoomItemDetailData.value = item.items[index];
  selectedRoomItems.value = item.items;
  let endDate = new Date(selectedDate.value)
  endDate.setDate(endDate.getDate() + 1)
  let endDateFormat = formatDate(endDate)
  roomDateRange.value = [selectedDate.value, endDateFormat]
  roomSettingForm.value.roomId = item.room.id;
  roomSettingForm.value.dateStart = selectedDate.value;
  roomSettingForm.value.dateEnd = endDateFormat;
  roomOrderForm.value.inDate = selectedDate.value;
  roomOrderForm.value.outDate = endDateFormat;
  roomSettingForm.value.price = item.items[index].price;
}


/**
 * 初始化日历
 */
const init = () => {
  nowDate.value = formatDate(now)
  queryForm.value.startDate = formatDate(now)
  queryForm.value.endDate = formatDate(end)
  let i = 0;
  while (now < end) {
    titleList.value.push({
      date: formatDate(now),
      week: now.getDay() === 0 || now.getDay() === 6,
      dayOfWeek: formatNum(now.getDay())
    })
    if (now.getDay() === 6) {
      weekList.value.push(i)
    }
    now.setDate(now.getDate() + 1);
    i++;
  }
}

const setData = (data: any) => {
  totalData.value = []
  for (let i = 0; i < titleList.value.length; i++) {
    totalData.value.push({
      1: 0,
      2: 0,
      3: 0,
    })
  }
  dataList.value = data
}


const formatDate = (date: Date) => {
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${date.getFullYear()}-${month}-${day}`;
}
const formatNum = (number: number) => {
  switch (number) {
    case 0:
      return '日'
    case 1:
      return '一'
    case 2:
      return '二'
    case 3:
      return '三'
    case 4:
      return '四'
    case 5:
      return '五'
    case 6:
      return '六'
  }
}
const getConsumeOrderItems = (roomOrderId: string) => {
  showRoomConsumeOrder.value = false;
  ApiRoomConsumeOrder.all({
    data: {
      roomOrderId
    },
    success: (res: any) => {
      roomConsumeOrders.value = res
    }
  });
};
const deleteConsumeOrderItem = (id: string) => {
  ApiRoomConsumeOrder.delete({
    ids: [id],
    success: (res: any) => {
      getConsumeOrderItems(selectedRoomItemDetailData.value.order.id)
    }
  });
};
const changePage = (page: any) => {
  queryForm.value.pageIndex = page.current;
  queryForm.value.pageSize = page.pageSize;
  getData();
};
// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    queryForm.value.pageIndex = 1;
  }
  dataLoading.value = true;
  queryForm.value.hotelId = props.hotelId
  queryForm.value.buildingId = props.buildingId
  queryForm.value.floorId = props.floorId
  queryForm.value.searchKey = props.searchKey
  ApiRoom.roomStatus({
    data: queryForm.value,
    success: (res: any) => {
      setData(res.records)
      queryForm.value.total = res.total;
      dataLoading.value = false;
    }
  });
};
const selectHotelUser = (v: any) => {
  roomOrderForm.value.customerName = v.name
  roomOrderForm.value.userId = v.id
  roomOrderForm.value.vip = v.vip
}
const getHotelUser = () => {
  ApiHotelUser.page({
    data: {
      searchKey: roomOrderForm.value.customerPhone,
      current: 1,
      pageSize: 10,
    },
    success: (res: any) => {
      res.records.forEach((item: any) => {
        item.label = item.phone
        item.text = item.phone
      })
      hotelUsers.value = res.data
    }
  })
}
onMounted(() => {
  nextTick(() => {
    height.value = document.body.offsetHeight - 300
  })
  window.addEventListener('resize', () => {
    height.value = document.body.offsetHeight - 300
  })
  init()
  getData()
  getHotelUser()
})

defineExpose({getData});
</script>
<style scoped lang="less">

// 最大容器
.organizationTrend {
  border-radius: var(--td-radius-medium);
  overflow: hidden;
}

.main::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.main::-webkit-scrollbar-button {
  display: none; /* 隐藏滚动条两端的按钮 */
}

.main::-webkit-scrollbar-corner {
  display: none; /* 隐藏滚动条角落部分 */
}

.main {
  user-select: none;
  overflow: auto;
  font-size: 13px;
}

table {
  table-layout: fixed;
  width: 100%;
}

// 设置头部的所有单元格为粘性定位 以及其它属性
thead {
  tr th {
    height: 40px;
    min-width: 100px;
    text-align: center;
  }

  tr:nth-child(2) {
    font-weight: normal;
    color: var(--td-text-color-placeholder);
  }

  tr th:first-child {
    position: sticky;
    left: 0;
    z-index: 0;
    background-color: var(--td-bg-color-page);
  }

  z-index: 2;
  position: sticky;
  left: 0;
  top: 0;
  background-color: var(--td-bg-color-page);
}

// 设置头部的第一个单元格
thead tr th:first-child {
  z-index: 2;
}

// 设置内容区的第一列单元格
tbody {
  tr {
    text-align: center;
  }

  tr td:first-child {
    position: sticky;
    left: 0;
    z-index: 0;
    height: 27px;
    background-color: var(--td-bg-color-container);
  }

  tr td {
    padding: 4px;
    min-height: 50px;
  }

}


// 设置下边框线
th {
  border-bottom: 1px solid var(--td-bg-color-container);
  border-right: 1px solid var(--td-bg-color-container);
}

th.week-line, td.week-line {
  border-right: 1px solid var(--td-brand-color-3);
}

td {
  border-bottom: 1px solid var(--td-bg-color-page);
  border-right: 1px solid var(--td-bg-color-page);
}

td:first-child, th:first-child {
  border-left: 1px solid var(--td-bg-color-page);
}

.item.close {
  background-color: var(--td-bg-color-secondarycontainer-hover);
}

.item {
  cursor: pointer;
  transition: 0.2s;

  .empty-room {
    color: var(--td-text-color-placeholder);
  }


  .empty-price {
    transition: 0.1s;
    font-size: 11px;
    //color: var(--td-text-color-placeholder);
  }

}

.item.order.check-in:hover, .item.order.check-in.active {
  background-color: var(--td-brand-color-1);
}

.item.order.check-in {
  background-color: var(--td-brand-color-2);
}

.item.order:hover, .item.order.active {
  background-color: var(--td-warning-color-1);
}

.item.order {
  background-color: var(--td-warning-color-2);
}

.item:hover, .item.active {
  .empty-price {
    font-weight: bold;
    font-size: 14px;
  }

  background-color: var(--td-bg-color-component-hover);
}

.room-name {
  font-size: var(--td-font-size-title-medium);
  color: var(--td-text-color-brand);
  font-weight: bold;
}

.room-type {
  font-size: 11px;
  color: var(--td-text-color-placeholder);
}

.title-date.week {
  color: var(--td-warning-color);
}


</style>

<style lang="less">
.t-hotel-user-autocomplete-option-list {
  .t-select-option {
    height: auto !important;
  }

  .custom-option {
    width: 100%;
    display: flex;
    align-items: center;

    .description {
      color: var(--td-text-color-secondary);
    }
  }

  .custom-option__main {
    margin-left: 8px;

    .text {
      font-weight: bold;
    }
  }
}
</style>
