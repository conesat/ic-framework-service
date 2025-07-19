<template>
  <t-loading :loading="dataLoading">
    <t-space direction="vertical" style="width: 100%">
      <t-form
        ref="form"
        class="base-form"
        :data="formData"
        :rules="FORM_RULES"
        label-align="top"
        :label-width="100"
        @keydown.enter.prevent
        @reset="onReset"
        @submit="onSubmit"
      >
        <div class="form-basic-container">
          <div class="form-basic-item">
            <div class="form-basic-container-title">编辑酒店</div>
            <!-- 表单内容 -->
            <t-row class="row-gap" :gutter="[32, 24]">
              <t-col :span="6">
                <t-form-item label="名称" name="name">
                  <t-input v-model="formData.name" placeholder="请输入名称" :maxlength="255"/>
                </t-form-item>
              </t-col>

              <t-col :span="6">
                <t-form-item label="星级" name="startLevel">
                  <t-rate v-model="formData.starLevel"></t-rate>
                </t-form-item>
              </t-col>

              <t-col :span="6">
                <t-form-item label="电话" name="tel">
                  <t-input v-model="formData.tel" placeholder="请输入电话" :maxlength="255"/>
                </t-form-item>

                <t-form-item label="酒店经理" name="managerUserId">
                  <t-input v-model="formData.managerUserName" placeholder="请选择酒店经理" :maxlength="255"
                           clearable
                           @clear="(e) => {formData.managerUserId = null;formData.managerUserName=null;e.e.stopPropagation();}"
                           @focus="() => showSelectUser = true"/>
                </t-form-item>

                <t-form-item label="地址" name="address">
                  <t-input v-model="formData.address" placeholder="请输入地址" :maxlength="255"/>
                </t-form-item>
              </t-col>
              <t-col :span="6">
                <t-form-item label="封面" name="picFileId">
                  <cropper
                    style="border-radius: var(--td-radius-default)"
                    :cover-width="1920"
                    :cover-height="1080"
                    :width="322"
                    :height="190"
                    :use-type="FileUseTypes.hotelPic"
                    background-color="var(--td-bg-color-container-hover)"
                    :url="formData.picFileUrl"
                    @uploaded="onUploaded"
                    @clear="(e)=>{formData.picFileUrl=null;formData.picFileId=null;e.e.stopPropagation()}"
                  ></cropper>
                </t-form-item>
              </t-col>
              <t-col :span="12">
                <t-form-item label="位置" name="latitude">
                  <map-select style="height: 400px;border-radius: var(--td-radius-medium);overflow: hidden"
                              city="北海市"
                              :lat="formData.latitude"
                              :lng="formData.longitude"
                              @change="e => {formData.latitude = e.lat;formData.longitude = e.lng;formData.address = e.address}"/>
                </t-form-item>
              </t-col>

              <template v-if="formData.id">

                <t-col :span="6">
                  <t-form-item label="创建时间" name="createTime">
                    <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255" disabled/>
                  </t-form-item>
                </t-col>

                <t-col :span="6">
                  <t-form-item label="更新时间" name="updateTime">
                    <t-input v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255" disabled/>
                  </t-form-item>
                </t-col>

              </template>
            </t-row>
          </div>
        </div>

        <div class="form-submit-container">
          <div class="form-submit-sub">
            <div class="form-submit-left">
              <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
              <t-button type="reset" class="form-submit-cancel" theme="default" variant="base"> 重置</t-button>
            </div>
          </div>
        </div>

      </t-form>
      <t-card v-if="formData.id" :bordered="false">
        <t-tabs v-model="tab" @change="changeTab">
          <t-tab-panel value="building" label="楼栋">
            <building-table :hotel-id="formData.id" @show-floor="showFloor"></building-table>
          </t-tab-panel>
          <t-tab-panel value="floor" :label="`${filter.buildingName}的楼层`" v-if="filter.buildingId">
            <floor-table :building-id="filter.buildingId"
                         @show-room="showRoom"></floor-table>
          </t-tab-panel>
          <t-tab-panel value="room" :label="`${filter.floorName}的房间`" v-if="filter.floorId">
            <room-table :floor-id="filter.floorId"></room-table>
          </t-tab-panel>
        </t-tabs>
      </t-card>
      <t-dialog v-model:visible="showSelectUser" header="选择用户" :footer="false" width="800">
        <user-select v-if="showSelectUser" @cancel="showSelectUser = false"
                     @selected="userSelected"></user-select>
      </t-dialog>
    </t-space>
  </t-loading>

</template>

<script lang="ts">

export default {
  name: 'hotelEdit',
};
</script>

<script setup lang="ts">
import userSelect from '@/pages/sys/user/select.vue';
import mapSelect from '@/components/map/select.vue';
import type {SubmitContext} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {closeOrBack} from "@/utils/url-utils";
import ApiHotel from '@/api/hotel/ApiHotel';
import router from '@/router';
import {FileUseTypes} from "@/constants/file-user-types";
import Cropper from "@/components/upload/cropper.vue";
import BuildingTable from "@/pages/hotel/building/table.vue";
import FloorTable from "@/pages/hotel/floor/table.vue";
import RoomTable from "@/pages/hotel/room/table.vue";

// 数据是否加载中
const dataLoading = ref(false);

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: null,
  name: '',
  starLevel: 0,
  tel: '',
  picFileId: '',
  picFileUrl: '',
  managerUserId: null,
  managerUserName: '',
  address: '',
  latitude: null,
  longitude: null,
  createTime: '',
  updateTime: '',
});
// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '名称不能为空', type: 'error', trigger: 'change'}],
};
const tab = ref('building');
const showSelectUser = ref(false);
const filter = ref({
  hotelId: null,
  buildingId: null,
  buildingName: null,
  floorId: null,
  floorName: null,
  roomId: null,
});
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    ApiHotel.edit({
      data: formData.value,
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router)
      }
    });
  }
};
const userSelected = (val: any) => {
  showSelectUser.value = false
  formData.value.managerUserId = val.id
  formData.value.managerUserName = val.name
};
const changeTab = () => {
  if (tab.value === 'building') {
    filter.value.buildingId = null
    filter.value.buildingName = null
    filter.value.floorId = null
    filter.value.floorName = null
    window.localStorage.setItem('hotel-filter', JSON.stringify(filter.value))
  } else if (tab.value === 'floor') {
    filter.value.floorId = null
    filter.value.floorName = null
    window.localStorage.setItem('hotel-filter', JSON.stringify(filter.value))
  }
}
const showFloor = (building: any) => {
  filter.value.buildingId = building.id
  filter.value.buildingName = building.name
  filter.value.floorId = null
  filter.value.floorName = null
  tab.value = 'floor'
  window.localStorage.setItem('hotel-filter', JSON.stringify(filter.value))
}
const showRoom = (floor: any) => {
  filter.value.floorId = floor.id
  filter.value.floorName = floor.name
  tab.value = 'room'
  window.localStorage.setItem('hotel-filter', JSON.stringify(filter.value))
}
const onUploaded = (res: any) => {
  formData.value.picFileId = res.id
  formData.value.picFileUrl = res.url
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id} = route.query;
  if (id) {
    filter.value.hotelId = id
    dataLoading.value = true;
    ApiHotel.detail({
      id: id,
      success: (res: any) => {
        formData.value = res;
        dataLoading.value = false;
      }
    });
    let storedFilter: any = window.localStorage.getItem('hotel-filter')
    if (storedFilter) {
      storedFilter = JSON.parse(storedFilter)
      if (storedFilter.hotelId === id) {
        filter.value = storedFilter
        if (filter.value.floorId) {
          tab.value = 'room'
        } else if (filter.value.buildingId) {
          tab.value = 'floor'
        }
      }
    }
  }
});
</script>

<style lang="less" scoped></style>
