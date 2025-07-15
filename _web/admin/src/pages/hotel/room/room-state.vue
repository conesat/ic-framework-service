<template>
  <div
    class="state-main"
    :style="{
      height: height + 'px',
      'background-image': roomDetail.mainPic ? `url('${roomDetail.mainPic}')` : `url('${WsJpg}')`,
    }"
  >
    <div style="text-align: center; display: inline-flex; margin: 0 auto; align-items: center; height: 100%">
      <div class="opr">
        <div class="opr-item" :class="{ active: activeOpr === 0 }" @click="activeOpr = 0">
          <HomeIcon />
        </div>
        <div class="opr-item" :class="{ active: activeOpr === 1 }" @click="activeOpr = 1">
          <ModeLightIcon />
        </div>
        <div class="opr-item" :class="{ active: activeOpr === 2 }" @click="activeOpr = 2">
          <AssignmentUserIcon />
        </div>
      </div>

      <div class="opr-fiter" :style="{ padding: activeOpr == 1 ? '0' : '30px' }">
        <div v-show="activeOpr === 0">
          <div class="opr-top" style="display: flex; align-items: center; margin-bottom: 20px">
            <div style="display: flex; align-items: center">
              <div
                style="
                  display: flex;
                  align-items: center;
                  padding: 4px 4px;
                  background-color: var(--td-bg-color-container-show);
                  border-radius: 40px;
                "
              >
                <div
                  style="width: 6px; height: 6px; border-radius: 100%; margin-left: 6px"
                  :style="{
                    'background-color':
                      roomOrder && roomOrder.id
                        ? roomOrder.checkIn
                          ? 'var(--td-warning-color-6)'
                          : 'var(--td-success-color-6)'
                        : 'var(--td-gray-color-1)',
                  }"
                ></div>
                <div style="font-weight: bold; margin: 0 6px">{{ roomDetail.no }}</div>
                <div>{{ roomDetail.roomTypeName }}</div>
                <div style="width: 10px"></div>
                <t-tag shape="round" v-if="roomOrder.checkIn" theme="success" size="small">已入住</t-tag>
                <t-tag shape="round" v-else theme="primary" size="small">未入住</t-tag>
              </div>
              <div style="margin-left: 10px">
                {{ roomDetail.hotelName }} / {{ roomDetail.buildingName }} / {{ roomDetail.floorName }}
              </div>
            </div>
            <div style="flex: 1"></div>
            <t-avatar v-if="roomOrder" :image="roomOrder.avatarFileUrl ? roomOrder.avatarFileUrl : ''">
              <UserIcon />
            </t-avatar>

            <div style="width: 10px"></div>
            <div style="font-size: 14px; font-weight: bold">
              {{ roomOrder && roomOrder.customerName ? roomOrder.customerName : '空闲' }}
            </div>
            <div style="width: 10px"></div>
            <template v-if="roomOrder && roomOrder.id">
              <t-tag shape="round" v-if="roomOrder && roomOrder.vip" theme="danger">VIP</t-tag>
              <t-tag shape="round" v-else theme="primary">普通住客</t-tag>
              <div style="width: 10px"></div>
            </template>
          </div>
          <div class="oprs">
            <t-row style="margin: -10px">
              <t-col>
                <t-row style="width: 100%">
                  <t-col style="padding: 10px">
                    <div class="card-item">
                      <div style="display: flex; align-items: center">
                        <div style="font-weight: bold">房门监控</div>
                        <div style="flex: 1; width: 0"></div>
                        <div style="margin-right: 10px">来访提醒：{{ stateForm.lftx ? 'ON' : 'OFF' }}</div>
                        <t-switch size="small" v-model="stateForm.lftx" />
                      </div>
                      <div style="margin-top: 20px; position: relative; width: 300px; height: 156px">
                        <t-image
                          :src="JK"
                          style="border-radius: var(--td-radius-medium); height: 100%; width: 100%"
                          fit="cover"
                        ></t-image>
                        <div
                          style="
                            position: absolute;
                            left: 10px;
                            top: 10px;
                            background-color: rgba(21, 28, 30, 0.4);
                            z-index: 2;
                            display: flex;
                            align-items: center;
                            padding: 2px 10px;
                            border-radius: 40px;
                          "
                        >
                          <div
                            style="
                              width: 6px;
                              height: 6px;
                              border-radius: 6px;
                              background-color: var(--td-error-color);
                              margin-right: 4px;
                            "
                          ></div>
                          <div style="font-size: 10px; color: var(--td-font-white-1)">实时画面</div>
                        </div>
                        <div
                          style="
                            position: absolute;
                            right: 10px;
                            top: 10px;
                            background-color: rgba(21, 28, 30, 0.4);
                            z-index: 2;
                            display: flex;
                            align-items: center;
                            padding: 2px 10px;
                            border-radius: 40px;
                          "
                        >
                          <div style="font-size: 10px; color: var(--td-font-white-1)">1080P 高清</div>
                        </div>
                      </div>
                    </div>
                  </t-col>
                  <t-col style="padding: 10px">
                    <div class="card-item">
                      <div style="display: flex; height: 80px">
                        <div style="width: 120px; height: 80px; overflow: hidden">
                          <div id="weather" style="height: 140px; width: 140px; margin: -30px -20px -10px -10px"></div>
                        </div>
                        <div style="flex: 1; width: 0"></div>
                        <div style="text-align: right; height: 100%; display: flex; flex-direction: column">
                          <div style="white-space: nowrap; display: flex; align-items: center">
                            <LocationIcon />
                            <span style="margin-left: 4px">北海市</span>
                          </div>
                          <div style="height: 0; flex: 1; display: flex; align-items: center; margin-top: 6px">
                            <div style="text-align: right; width: 100%">
                              <span style="font-size: 30px; margin-right: 4px">28</span>
                              <span>°C</span>
                            </div>
                          </div>
                          <div style="color: var(--td-text-color-secondary)">夜间多云</div>
                        </div>
                      </div>
                    </div>
                    <div style="height: 20px"></div>
                    <div style="display: flex">
                      <div class="card-item" style="padding: 10px">
                        <div style="display: flex">
                          <div style="flex: 1; text-align: left">{{ stateForm.mdr ? 'ON' : 'OFF' }}</div>
                          <t-switch v-model="stateForm.mdr" size="small"></t-switch>
                        </div>
                        <UserTalkOff1Icon
                          size="24"
                          style="margin: 6px"
                          :style="{
                            color: stateForm.mdr ? 'var(--td-text-color-primary)' : 'var(--td-text-color-secondary)',
                          }"
                        />
                        <div>免打扰</div>
                      </div>
                      <div style="min-width: 20px"></div>
                      <div class="card-item" style="padding: 10px">
                        <div style="display: flex">
                          <div style="flex: 1; text-align: left">{{ stateForm.jy ? 'ON' : 'OFF' }}</div>
                          <t-switch v-model="stateForm.jy" size="small"></t-switch>
                        </div>
                        <SoundMute1Icon
                          size="24"
                          style="margin: 6px"
                          :style="{
                            color: stateForm.jy ? 'var(--td-text-color-primary)' : 'var(--td-text-color-secondary)',
                          }"
                        />
                        <div>全屋静音</div>
                      </div>
                    </div>
                  </t-col>
                </t-row>
                <t-row>
                  <t-col style="padding: 10px">
                    <div class="card-item" style="padding: 6px">
                      <div style="display: flex">
                        <div>
                          <t-image
                            :src="JHQ"
                            fit="contain"
                            style="width: 86px; height: 106px; background: transparent"
                          ></t-image>
                        </div>
                        <div>
                          <t-space direction="vertical" :size="4">
                            <t-switch size="small" v-model="stateForm.jhq"></t-switch>
                            <WifiIcon style="color: var(--td-success-color)" size="16" />
                            <BatteryChargingIcon style="color: var(--td-success-color)" size="14" />
                          </t-space>
                        </div>
                      </div>
                      <div>净化器：{{ stateForm.jhq ? '开启' : '关闭' }}</div>
                    </div>
                  </t-col>
                  <t-col style="padding: 10px">
                    <div class="card-item" style="padding: 6px">
                      <div style="display: flex">
                        <div>
                          <t-image
                            :src="JSQ"
                            fit="contain"
                            style="width: 86px; height: 106px; background: transparent"
                          ></t-image>
                        </div>
                        <div>
                          <t-space direction="vertical" :size="4">
                            <t-switch size="small" v-model="stateForm.jsq"></t-switch>
                            <WifiIcon style="color: var(--td-success-color)" size="16" />
                            <BatteryChargingIcon style="color: var(--td-success-color)" size="14" />
                          </t-space>
                        </div>
                      </div>
                      <div>加湿器：{{ stateForm.jsq ? '开启' : '关闭' }}</div>
                    </div>
                  </t-col>
                  <t-col style="padding: 10px">
                    <div class="card-item" style="padding: 6px">
                      <div style="display: flex">
                        <div>
                          <t-image
                            :src="CSJ"
                            fit="contain"
                            style="width: 86px; height: 106px; background: transparent"
                          ></t-image>
                        </div>
                        <div>
                          <t-space direction="vertical" :size="4">
                            <t-switch size="small" v-model="stateForm.csj"></t-switch>
                            <WifiIcon style="color: var(--td-success-color)" size="16" />
                            <BatteryChargingIcon style="color: var(--td-success-color)" size="14" />
                          </t-space>
                        </div>
                      </div>
                      <div>除湿机：{{ stateForm.csj ? '开启' : '关闭' }}</div>
                    </div>
                  </t-col>
                  <t-col style="padding: 10px">
                    <div class="card-item" style="padding: 6px">
                      <div style="display: flex">
                        <div>
                          <t-image
                            :src="TYY"
                            fit="contain"
                            style="width: 86px; height: 106px; background: transparent"
                          ></t-image>
                        </div>
                        <div>
                          <t-space direction="vertical" :size="4">
                            <t-switch size="small" v-model="stateForm.tyy"></t-switch>
                            <WifiIcon style="color: var(--td-success-color)" size="16" />
                            <BatteryChargingIcon style="color: var(--td-success-color)" size="14" />
                          </t-space>
                        </div>
                      </div>
                      <div>投影仪：{{ stateForm.tyy ? '开启' : '关闭' }}</div>
                    </div>
                  </t-col>
                </t-row>
              </t-col>
              <t-col style="padding: 10px">
                <div class="card-item">
                  <div style="width: 100%">
                    <div style="display: flex; align-items: center">
                      <div style="flex: 1; text-align: left">
                        <div style="font-weight: bold">房间空调</div>
                        <div style="color: var(--td-text-color-secondary); font-size: 10px">
                          空调{{ stateForm.airConditioner ? '已开启' : '已关闭' }}
                        </div>
                      </div>
                      <div>
                        <t-switch v-model="stateForm.airConditioner"></t-switch>
                      </div>
                    </div>
                    <div style="width: 200px; height: 200px; position: relative; margin: 22px 20px">
                      <v-chart
                        :option="charOption"
                        :theme="settingStore.mode"
                        style="height: 100%; width: 100%"
                      ></v-chart>
                      <t-button
                        style="position: absolute; bottom: 30px; left: 60px"
                        shape="circle"
                        theme="default"
                        :disabled="!stateForm.airConditioner || charOption.series[0].data[0].value <= 14"
                        @click="changeAirData(-0.5)"
                      >
                        <template #icon>
                          <MinusIcon />
                        </template>
                      </t-button>
                      <t-button
                        style="position: absolute; bottom: 30px; left: 100px"
                        shape="circle"
                        theme="default"
                        :disabled="!stateForm.airConditioner || charOption.series[0].data[0].value >= 32"
                        @click="changeAirData(0.5)"
                      >
                        <template #icon>
                          <PlusIcon />
                        </template>
                      </t-button>
                    </div>
                    <div style="text-align: center; padding: 0 10px; margin-top: -10px">
                      <t-space>
                        <div style="text-align: center">
                          <t-button theme="primary" shape="round" variant="base" :disabled="!stateForm.airConditioner">
                            <template #icon>
                              <SnowflakeIcon />
                            </template>
                          </t-button>
                          <div style="font-weight: bold; margin-top: 10px">模式</div>
                          <div style="color: var(--td-text-color-secondary); font-size: 10px">当前模式：制冷</div>
                        </div>
                        <div style="text-align: center">
                          <t-button theme="default" shape="round" variant="base" :disabled="!stateForm.airConditioner">
                            <template #icon>
                              <WindyRainIcon />
                            </template>
                          </t-button>
                          <div style="font-weight: bold; margin-top: 10px">通风</div>
                          <div style="color: var(--td-text-color-secondary); font-size: 10px">通风已关闭</div>
                        </div>
                        <div style="text-align: center">
                          <t-button theme="default" shape="round" variant="base" :disabled="!stateForm.airConditioner">
                            <template #icon>
                              <TimeIcon />
                            </template>
                          </t-button>
                          <div style="font-weight: bold; margin-top: 10px">定时</div>
                          <div style="color: var(--td-text-color-secondary); font-size: 10px">定时已关闭</div>
                        </div>
                      </t-space>
                    </div>
                  </div>
                </div>
              </t-col>
            </t-row>
          </div>
        </div>

        <div v-show="activeOpr == 1" class="oprs">
          <div class="card-item" style="padding: 0; position: relative; max-width: 848px; width: 100%">
            <img src="@/assets/hotel/img/room_d.jpg" style="width: 100%; height: auto" />
            <img
              src="@/assets/hotel/img/room_1.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.l_room_0 ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/room_2.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.l_room_1 ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/room_3.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.l_room_2 ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/kt.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.l_kt_0 ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/cs.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.cs ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/cf.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.cf ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/yt.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.yt ? 1 : 0 }"
            />
            <img
              src="@/assets/hotel/img/xst.png"
              style="position: absolute; left: 0; top: 0; width: 100%; height: auto; transition: 0.2s"
              :style="{ opacity: stateForm.xst ? 1 : 0 }"
            />

            <div
              style="
                position: absolute;
                left: 25.4%;
                top: 20%;
                width: 4%;
                height: 12%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.l_kt_0 = !stateForm.l_kt_0"
            ></div>
            <div
              style="
                position: absolute;
                left: 70.4%;
                top: 20%;
                width: 12%;
                height: 6%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.l_room_1 = !stateForm.l_room_1"
            ></div>
            <div
              style="
                position: absolute;
                left: 54%;
                top: 12%;
                width: 4%;
                height: 6%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.l_room_2 = !stateForm.l_room_2"
            ></div>
            <div
              style="
                position: absolute;
                left: 54%;
                top: 24%;
                width: 4%;
                height: 16%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.l_room_0 = !stateForm.l_room_0"
            ></div>
            <div
              style="
                position: absolute;
                left: 72%;
                top: 52%;
                width: 20%;
                height: 40%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.cs = !stateForm.cs"
            ></div>
            <div
              style="
                position: absolute;
                left: 46%;
                top: 54%;
                width: 8%;
                height: 26%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.cf = !stateForm.cf"
            ></div>
            <div
              style="
                position: absolute;
                left: 6%;
                top: 84%;
                width: 32%;
                height: 6%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.yt = !stateForm.yt"
            ></div>
            <div
              style="
                position: absolute;
                left: 41%;
                top: 90%;
                width: 6%;
                height: 6%;
                background-color: transparent;
                cursor: pointer;
              "
              @click="stateForm.xst = !stateForm.xst"
            ></div>
          </div>
        </div>
        <div v-show="activeOpr == 2" class="oprs">
          <t-row style="margin: -10px">
            <t-col style="padding: 10px">
              <div class="card-item">
                <div style="min-width: 264px; max-height: 500px; overflow: auto">
                  <t-space direction="vertical">
                    <t-avatar :image="roomOrder && roomOrder.avatarFileUrl ? roomOrder.avatarFileUrl : ''" size="60px">
                      <UserIcon />
                    </t-avatar>
                    <t-space>
                      <div style="font-weight: bold; font-size: 16px" v-if="roomOrder.userName">
                        {{ roomOrder ? roomOrder.userName : '临时住客' }}
                      </div>
                      <t-tag shape="round" v-if="roomOrder && roomOrder.vip" theme="danger">VIP</t-tag>
                      <t-tag shape="round" v-if="roomOrder && roomOrder.id" theme="primary">普通住客</t-tag>
                      <t-tag shape="round" v-else theme="success">空闲</t-tag>
                    </t-space>
                    <div>
                      <t-divider>订单信息</t-divider>
                      <div>
                        <div style="display: flex; align-items: center">
                          <div style="color: var(--td-text-color-secondary)">房间号</div>
                          <div style="flex: 1; text-align: right; padding-left: 10px">{{ roomDetail.no }}</div>
                        </div>
                        <div style="display: flex; align-items: center; margin-top: 10px">
                          <div style="color: var(--td-text-color-secondary)">房型</div>
                          <div style="flex: 1; text-align: right; padding-left: 10px">
                            {{ roomDetail.roomTypeName }}
                          </div>
                        </div>
                        <div style="display: flex; align-items: center; margin-top: 10px">
                          <div style="color: var(--td-text-color-secondary)">楼层</div>
                          <div style="flex: 1; text-align: right; padding-left: 10px">
                            {{ roomDetail.hotelName }} / {{ roomDetail.buildingName }} / {{ roomDetail.floorName }}
                          </div>
                        </div>
                        <div style="display: flex; align-items: center; margin-top: 10px">
                          <div style="color: var(--td-text-color-secondary)">时间</div>
                          <div style="flex: 1; text-align: right; padding-left: 10px">
                            {{ roomOrder.inDate }} 至
                            {{ roomOrder.outDate }}
                          </div>
                        </div>
                        <div style="display: flex; align-items: center; margin-top: 10px">
                          <div style="color: var(--td-text-color-secondary)">总消费</div>
                          <div style="flex: 1; text-align: right; padding-left: 10px">
                            ￥{{ roomOrder.actualRoomPrice }}
                          </div>
                        </div>
                      </div>
                    </div>

                    <div>
                      <t-divider>消费内容</t-divider>
                      <div
                        style="width: 100%; margin-top: 10px; text-align: left"
                        v-for="(item, index) in roomConsumeOrders"
                      >
                        <div style="display: flex; align-items: end; margin-bottom: 4px">
                          <div style="flex: 1; font-weight: bold">{{ item.customerItemName }}</div>
                          <div style="font-size: 11px; color: var(--td-text-color-secondary)">
                            {{ item.createTime.substring(5, 16) }}
                          </div>
                        </div>
                        <div style="display: flex; align-items: center">
                          <div>
                            <t-image :src="item.fileUrl" style="width: 40px; height: 40px" fit="cover" shape="round">
                              <template #error>
                                <image-error-icon></image-error-icon>
                              </template>
                            </t-image>
                          </div>
                          <div style="flex: 1; width: 0; margin-left: 10px; text-align: left">
                            <div style="font-size: 11px; color: var(--td-text-color-secondary); align-items: center">
                              <div style="display: flex; align-items: center">
                                <div style="flex: 1">数量：</div>
                                <div>{{ item.num }}</div>
                              </div>
                              <div style="width: 10px"></div>
                              <div style="display: flex; align-items: center">
                                <div style="flex: 1">总价：</div>
                                <div>￥{{ item.price }}</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </t-space>
                </div>
              </div>
            </t-col>
            <t-col style="padding: 10px">
              <div class="card-item" style="display: flex; flex-direction: column; max-height: 500px; width: 500px">
                <div style="height: 0; flex: 1; overflow: auto; margin: -10px">
                  <t-row>
                    <t-col v-for="item in consumeItems" style="padding: 10px">
                      <div class="card-item" style="width: 120px; text-align: center">
                        <t-image
                          style="height: auto; width: 100%; border-radius: 10px; min-height: 30px"
                          :src="item.fileUrl"
                        >
                          <template #error>
                            <image-error-icon size="30"></image-error-icon>
                          </template>
                        </t-image>
                        <div style="margin: 4px 0">{{ item.name }}</div>
                        <div style="margin: 4px 0">￥{{ item.price }}</div>
                        <t-input-number
                          style="width: 100%"
                          size="small"
                          :min="0"
                          :max="item.inventory >= 0 ? item.inventory : 99999999"
                          v-model="item.num"
                          @change="onConsumeItemChange"
                        ></t-input-number>
                      </div>
                    </t-col>
                  </t-row>
                </div>

                <div style="display: flex; align-items: center; margin-top: 20px">
                  <div>合计：￥{{ totalPrice }}</div>
                  <div style="flex: 1; min-width: 20px"></div>
                  <div style="display: flex; align-items: center; border-radius: 50px; overflow: hidden">
                    <div
                      style="
                        cursor: pointer;
                        padding: 4px 6px 4px 16px;
                        border: 1px solid var(--td-brand-color);
                        border-bottom-left-radius: 50px;
                        border-top-left-radius: 50px;
                      "
                    >
                      清空购物车
                    </div>
                    <div
                      @click="createOrder"
                      style="
                        color: var(--td-font-white-1);
                        cursor: pointer;
                        padding: 4px 16px 4px 6px;
                        background-color: var(--td-brand-color);
                        border: 1px solid var(--td-brand-color);
                      "
                    >
                      立刻下单
                    </div>
                  </div>
                </div>
              </div>
            </t-col>
          </t-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import 'echarts';
import VChart from 'vue-echarts';
import { onMounted, ref, nextTick } from 'vue';
import { useSettingStore } from '@/store';
import {
  MinusIcon,
  PlusIcon,
  SnowflakeIcon,
  WindyRainIcon,
  ModeLightIcon,
  HomeIcon,
  UserIcon,
  AssignmentUserIcon,
  TimeIcon,
  LocationIcon,
  UserTalkOff1Icon,
  SoundMute1Icon,
  WifiIcon,
  BatteryChargingIcon,
  ImageErrorIcon,
} from 'tdesign-icons-vue-next';

import ApiRoom from '@/api/hotel/ApiRoom';
import ApiRoomOrder from '@/api/hotel/ApiRoomOrder';
import ApiRoomConsumeOrder from '@/api/hotel/ApiRoomConsumeOrder';
import ApiConsumeItem from '@/api/hotel/ApiConsumeItem';
import WsJpg from '@/assets/hotel/img/ws.jpg';
import JK from '@/assets/hotel/img/jk.png';
import JHQ from '@/assets/hotel/img/jhq.png';
import JSQ from '@/assets/hotel/img/jsq.png';
import CSJ from '@/assets/hotel/img/csj.png';
import TYY from '@/assets/hotel/img/tyy.png';
import { useRoute } from 'vue-router';
import lottie from 'lottie-web';

import animationData from '@/assets/hotel/lotties/night-cloudy.json';


// 路由
const route = useRoute();
const settingStore = useSettingStore();
const height = ref(0);
const stateForm = ref({
  roomId: null,
  airConditioner: true,
  mdr: true,
  jy: false,
  lftx: true,
  jhq: false,
  jsq: false,
  csj: false,
  tyy: false,
  l_room_0: false,
  l_room_1: false,
  l_room_2: false,
  l_kt_0: false,
  cs: false,
  cf: false,
  yt: false,
  xst: false,
});
const totalPrice = ref(0);
const consumeItems = ref([]);
const roomDetail = ref({
  id: null,
  preferentialPrice: null,
  weekPrice: null,
  price: null,
  no: '',
  nos: [],
  saleStatus: 1,
  roomStatus: 1,
  floorId: '',
  floorName: '',
  roomTypeId: '',
  picFiles: [],
  roomTypeName: '',
  createTime: '',
  updateTime: '',
  supportingIds: [],
  buildingName: '',
  hotelName: '',
  mainPic: '',
});
const roomConsumeOrders = ref([]);
const roomOrder = ref({
  id: null,
  userId: '',
  vip: false,
  userName: '',
  avatarFileUrl: '',
  customerName: '',
  customerPhone: '',
  roomPrice: '',
  actualRoomPrice: '',
  inDate: '',
  outDate: '',
  roomId: '',
  checkIn: false,
  createTime: '',
  updateTime: '',
});
const activeOpr = ref(0);
const charOption = ref({
  backgroundColor: 'transparent',
  series: [
    {
      type: 'gauge',
      min: 14,
      max: 32,
      radius: '60%', //图表尺寸
      splitNumber: 10,
      startAngle: 210,
      endAngle: -30,
      detail: {
        formatter: '{value}℃',
        fontSize: 14,
      },
      anchor: {
        show: true,
        showAbove: true,
        size: 10,
        itemStyle: {
          borderWidth: 2,
        },
      },
      pointer: {
        icon: 'path://M2090.36389,615.30999 L2090.36389,615.30999 C2091.48372,615.30999 2092.40383,616.194028 2092.44859,617.312956 L2096.90698,728.755929 C2097.05155,732.369577 2094.2393,735.416212 2090.62566,735.56078 C2090.53845,735.564269 2090.45117,735.566014 2090.36389,735.566014 L2090.36389,735.566014 C2086.74736,735.566014 2083.81557,732.63423 2083.81557,729.017692 C2083.81557,728.930412 2083.81732,728.84314 2083.82081,728.755929 L2088.2792,617.312956 C2088.32396,616.194028 2089.24407,615.30999 2090.36389,615.30999 Z',
        length: '75%',
        width: 10,
        offsetCenter: [0, '5%'],
      },
      progress: {
        roundCap: true,
        show: true,
        width: 8,
      },
      itemStyle: {},
      data: [
        {
          value: 26,
          name: '',
        },
      ],
      axisLine: {
        roundCap: true,
        lineStyle: {
          width: 8,
        },
      },
      splitLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      axisLabel: {
        show: false,
      },
      zlevel: 10,
    },
    {
      name: '',
      type: 'gauge',
      splitNumber: 6, //刻度数量
      min: 14,
      max: 32,
      startAngle: 210,
      endAngle: -30,
      radius: '60%', //图表尺寸
      center: ['50%', '50%'],
      zlevel: 1,
      axisLine: {
        show: true,
        lineStyle: {
          width: 0,
          shadowBlur: 0,
          color: [
            [0.2, '#23AFAF'],
            [0.4, '#2270DA'],
            [0.6, '#E99D02'],
            [1, '#F45656'],
          ],
        },
      },
      axisTick: {
        show: true,
        distance: -12,
        lineStyle: {
          color: 'auto',
          width: 2,
        },
        length: 4,
        splitNumber: 5,
      },
      splitLine: {
        distance: -18,
        show: true,
        length: 8,
        lineStyle: {
          color: 'auto',
          width: 2,
        },
      },
      axisLabel: {
        distance: -25,
        fontSize: 10,
      },
      pointer: {
        //仪表盘指针
        show: 0,
        length: '0%',
        width: 1,
      },
      anchor: {
        show: true,
        showAbove: true,
        size: 16,
        itemStyle: {},
      },
      detail: {
        show: false,
      },
      data: [],
    },
  ],
});

const initLottie = () => {
  // 读取动画容器
  const lottieContainer = document.getElementById('weather');
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

const changeAirData = (value: number) => {
  if (value > 0 && charOption.value.series[0].data[0].value >= 32) {
    return;
  }
  if (value < 0 && charOption.value.series[0].data[0].value <= 14) {
    return;
  }
  charOption.value.series[0].data[0].value += value;
};

/**
 * 消费商品
 */
const onConsumeItemChange = () => {
  let price = 0;
  consumeItems.value.forEach((item) => {
    if (item.num) {
      price += item.price * item.num;
    }
  });
  totalPrice.value = price;
};

const createOrder = () => {
  if (totalPrice.value <= 0) {
    return;
  }
  let orderItems: any = [];
  consumeItems.value.forEach((item) => {
    if (item.num) {
      orderItems.push({
        customerItemId: item.id,
        num: item.num,
      });
    }
  });

  ApiRoomConsumeOrder.createBatch({
    data: {
      roomOrderId: roomOrder.value.id,
      items: orderItems,
    },
    success: () => {
      ApiRoomConsumeOrder.all({
        data: {
          roomOrderId: roomOrder.value.id,
        },
        success: (res: any) => {
          roomConsumeOrders.value = res;
        },
      });
    },
  });
};

onMounted(() => {
  const { id } = route.query;
  nextTick(() => {
    height.value = document.body.offsetHeight - 160;
  });
  window.addEventListener('resize', () => {
    height.value = document.body.offsetHeight - 160;
  });
  ApiRoom.detail({
    id: id,
    success: (res: any) => {
      if (res && res.picFiles) {
        res.picFiles.forEach((item: any) => {
          if (item.mainPic) {
            res.mainPic = item.fileUrl;
          }
        });
      }
      roomDetail.value = res;
    },
  });
  ApiRoomOrder.roomNowOrder(id, {
    success: (res: any) => {
      if (res) {
        res.userName = res.userName ?? res.customerName;
        roomOrder.value = res;
        ApiRoomConsumeOrder.all({
          data: { roomOrderId: res.id },
          success: (res: any) => {
            roomConsumeOrders.value = res;
          },
        });
      }
    },
  });
  ApiConsumeItem.all({
    success: (res: any) => {
      consumeItems.value = res;
    },
  });
  initLottie();
});
</script>

<style scoped lang="less">
.state-main {
  display: flex;
  border-radius: 10px;
  align-items: center;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  object-fit: cover;

  .opr-fiter:hover {
    border: 2px solid rgba(255, 255, 255, 0.4);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  }

  .opr-fiter {
    max-height: 100%;
    overflow: auto;
    display: inline-block;
    padding: 30px;
    transition: 0.4s;
    border-radius: var(--td-radius-large);
    background: rgba(255, 255, 255, 0.1);
    -webkit-backdrop-filter: blur(6px);
    backdrop-filter: blur(6px);
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.2);
    border: 2px solid rgba(255, 255, 255, 0.1);

    .card-item {
      box-sizing: border-box;
      padding: 16px;
      width: 100%;
      border-radius: var(--td-radius-medium);
      background-color: var(--td-bg-color-container-show-deep);
    }

    .oprs {
      z-index: 2;
      display: flex;
      width: 100%;
      flex: 1;
    }
  }

  .opr:hover {
    border: 2px solid rgba(255, 255, 255, 0.9);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  }

  .opr {
    padding: 20px 0;
    min-width: 64px;
    margin-right: 30px;
    transition: 0.4s;
    overflow: hidden;
    border-radius: 30px;
    background-color: var(--td-bg-color-container-show-deep);
    border: 2px solid rgba(255, 255, 255, 0.2);

    .opr-item:hover,
    .opr-item.active {
      color: var(--td-text-color-primary);
    }

    .opr-item {
      color: var(--td-text-color-placeholder);
      cursor: pointer;
      border-radius: 60px;
      font-size: 26px;
      height: 60px;
      width: 60px;
      padding: 17px;
    }
  }
}
</style>
