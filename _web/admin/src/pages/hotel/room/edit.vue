<template>
  <t-form
    ref="form"
    class="base-form"
    :data="formData"
    :rules="FORM_RULES"
    label-align="top"
    :label-width="100"
    @reset="onReset"
    @submit="onSubmit"
  >
    <div class="form-basic-container">
      <div class="form-basic-item">
        <div class="form-basic-container-title">编辑房间</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6" v-if="!formData.id && !formData.extKey">
            <t-form-item label="房间号【请回车确认，可以输入多个】" name="nos">
              <t-tagInput v-model="formData.nos" placeholder="请输入房间号" :max="30" />
            </t-form-item>
          </t-col>

          <t-col :span="6" v-else>
            <t-form-item label="房间号" name="no">
              <t-input v-model="formData.no" placeholder="请输入房间号" :maxlength="255" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="门市价" name="price">
              <t-input-number v-model="formData.price" placeholder="请输入价格" :min="0" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="所属楼层" name="floorId">
              <t-input
                v-model="formData.floorName"
                placeholder="请选择所属楼层"
                :maxlength="255"
                clearable
                @clear="
                  (e) => {
                    formData.floorId = null;
                    formData.floorName = null;
                    e.e.stopPropagation();
                  }
                "
                @focus="() => (showSelectFloor = true)"
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="周末价" name="weekPrice">
              <t-input-number v-model="formData.weekPrice" placeholder="请输入价格" :min="0" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="所属房型" name="roomTypeId">
              <t-input
                v-model="formData.roomTypeName"
                placeholder="请输入所属房型"
                clearable
                @clear="
                  (e) => {
                    formData.roomTypeId = null;
                    formData.roomTypeName = null;
                    e.e.stopPropagation();
                  }
                "
                @focus="() => (showSelectRoomType = true)"
              />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="优惠价" name="preferentialPrice">
              <t-input-number v-model="formData.preferentialPrice" placeholder="请输入价格" :min="0" />
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="房间状态" name="roomStatus">
              <t-select v-model="formData.roomStatus" placeholder="请选择房间状态">
                <t-option
                  :disabled="item.code === 0"
                  :value="item.code"
                  :label="item.text"
                  :key="item.code"
                  v-for="item in roomStatus"
                ></t-option>
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是否在售" name="saleStatus">
              <t-radio-group v-model="formData.saleStatus">
                <t-radio :value="1"> 在售</t-radio>
                <t-radio :value="0"> 下架</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="配套设施" name="supportingIds">
              <t-select
                v-model="formData.supportingIds"
                :options="supportingList"
                :keys="{ value: 'id', label: 'name' }"
                placeholder="请选择"
                multiple
              >
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="扩展key" name="extKey">
              <t-input v-model="formData.extKey" placeholder="请输入扩展key用于关联3d模型" :maxlength="255" />
            </t-form-item>
          </t-col>

          <template v-if="formData.id">
            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input v-model="formData.createTime" placeholder="请输入创建时间" :maxlength="255" disabled />
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input v-model="formData.updateTime" placeholder="请输入更新时间" :maxlength="255" disabled />
              </t-form-item>
            </t-col>
          </template>

          <t-col :span="12">
            <t-form-item label="图片" name="picFileId">
              <t-space direction="vertical">
                <cropper
                  style="border-radius: var(--td-radius-default); width: auto; height: auto"
                  :cover-width="1920"
                  :cover-height="1080"
                  :button="true"
                  :use-type="FileUseTypes.hotelPic"
                  background-color="var(--td-bg-color-container-hover)"
                  @uploaded="onUploaded"
                ></cropper>

                <div style="margin: -10px">
                  <t-image-viewer
                    :key="showImageIndex"
                    v-model:visible="imagerView"
                    :default-index="showImageIndex"
                    :images="formData.picFiles.map((item) => item.fileUrl)"
                  >
                    <template #trigger>
                      <div class="img-list-item" v-for="(item, index) in formData.picFiles">
                        <t-image
                          alt="test"
                          :src="item.fileUrl"
                          :style="{
                            border: item.mainPic ? '1px solid var(--td-text-color-brand)' : '1px solid transparent',
                          }"
                          style="width: 212px; height: 125px; border-radius: var(--td-radius-default)"
                        />
                        <div class="cover">
                          <div style="margin: 0 auto">
                            <t-space direction="vertical" style="text-align: center">
                              <t-space>
                                <t-link
                                  hover="color"
                                  @click="
                                    () => {
                                      showImageIndex = index;
                                      imagerView = true;
                                    }
                                  "
                                >
                                  <span><BrowseIcon size="1.4em" /> 预览</span>
                                </t-link>
                                <t-link
                                  hover="color"
                                  @click="
                                    () => {
                                      formData.picFiles.splice(index, 1);
                                    }
                                  "
                                  theme="danger"
                                >
                                  <span><DeleteIcon size="1.4em" /> 删除</span>
                                </t-link>
                              </t-space>
                              <t-button v-if="!item.mainPic" size="small" @click="setMainPic(index)">置为主图</t-button>
                              <t-button v-else size="small" theme="default">主图</t-button>
                            </t-space>
                          </div>
                        </div>
                      </div>
                    </template>
                  </t-image-viewer>
                </div>
              </t-space>
            </t-form-item>
          </t-col>
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

    <t-dialog v-model:visible="showSelectFloor" header="选择楼层" :footer="false" width="800">
      <floor-select
        v-if="showSelectFloor"
        @cancel="showSelectFloor = false"
        @selected="floorSelected"
      ></floor-select>
    </t-dialog>

    <t-dialog v-model:visible="showSelectRoomType" header="选择房型" :footer="false" width="800">
      <room-type-select
        v-if="showSelectRoomType"
        @cancel="showSelectRoomType = false"
        @selected="roomTypeSelected"
      ></room-type-select>
    </t-dialog>
  </t-form>
</template>

<script lang="tsx">
export default {
  name: 'roomEdit',
};
</script>

<script setup lang="tsx">
import floorSelect from '@/pages/hotel/floor/select.vue';
import roomTypeSelect from '@/pages/hotel/room-type/select.vue';
import { BrowseIcon, DeleteIcon } from 'tdesign-icons-vue-next';
import type { SelectProps, SubmitContext, UploadProps } from 'tdesign-vue-next';
import { FormRule, MessagePlugin } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { closeOrBack } from '@/utils/url-utils';
import ApiRoom from '@/api/hotel/ApiRoom';
import router from '@/router';
import ApiFloor from '@/api/hotel/ApiFloor';
import ApiSupporting from '@/api/hotel/ApiSupporting';
import { FileUseTypes } from '@/constants/file-user-types';
import Cropper from '@/components/upload/cropper.vue';

// 自定义校验 start -------------------
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showImageIndex = ref(0);
const imagerView = ref(false);
const showSelectFloor = ref(false);
const showSelectRoomType = ref(false);
const supportingList = ref([]);
const roomStatus = ref([]);
// 路由
const route = useRoute();
const optionRender = (option: { name: string; id: string; iconFileUrl: string }) => {
  return (
    <div style="display: flex;align-items: center">
      <t-image
        fit="contain"
        src={option.iconFileUrl}
        style="width: 24px;height: 24px;background-color: rgba(255,255,255,0.5);border-radius: 4px;margin-right:10px"
      />
      <div style="line-height:24px">{option.name}</div>
    </div>
  );
};

// 表单
const formData = ref({
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
  extKey: '',
  picFiles: [],
  roomTypeName: '',
  createTime: '',
  updateTime: '',
  supportingIds: [],
});


// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  no: [{ required: true, message: '房间号不能为空', type: 'error', trigger: 'change' }],
  nos: [{ required: true, message: '房间号不能为空', type: 'error', trigger: 'change' }],
  floorId: [{ required: true, message: '所属楼层不能为空', type: 'error', trigger: 'change' }],
  roomTypeId: [{ required: true, message: '所属房型不能为空', type: 'error', trigger: 'change' }],
  price: [{ required: true, number: true, message: '价格不能为空', type: 'error', trigger: 'change' }],
  saleStatus: [{ required: true, number: true, message: '销售状态不能为空', type: 'error', trigger: 'change' }],
  roomStatus: [{ required: true, number: true, message: '房间状态不能为空', type: 'error', trigger: 'change' }],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 重置表单
const onReset = () => {};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.nos.length === 0) {
      formData.value.nos = [formData.value.no];
    }
    ApiRoom.edit({
      data: {
        picFiles: JSON.stringify(formData.value.picFiles),
        ...formData.value,
      },
      success: (res: any) => {
        MessagePlugin.success('已完成');
        closeOrBack(route, router);
      },
    });
  }
};

const floorSelected = (val: any) => {
  showSelectFloor.value = false;
  formData.value.floorId = val.id;
  formData.value.floorName = val.name;
};
const roomTypeSelected = (val: any) => {
  showSelectRoomType.value = false;
  formData.value.roomTypeId = val.id;
  formData.value.roomTypeName = val.name;
};
const onUploaded = (res: any) => {
  if (!formData.value.picFiles) {
    formData.value.picFiles = [];
  }
  formData.value.picFiles.push({
    fileId: res.id,
    fileUrl: res.url,
  });
};

const setMainPic = (index: number) => {
  formData.value.picFiles.forEach((item: any) => {
    item.mainPic = false;
  });
  formData.value.picFiles[index].mainPic = true;
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const { id, floorId, extKey } = route.query;
  if (id) {
    ApiRoom.detail({
      id: id,
      success: (res: any) => {
        res.roomStatus = res.roomStatus ? res.roomStatus.code : 1;
        res.saleStatus = res.saleStatus ? res.saleStatus.code : 1;
        res.supportingIds = res.supportingVOS ? res.supportingVOS.map((item: any) => item.id) : [];
        res.picFiles = res.picFiles ? res.picFiles : [];
        formData.value = res;
      },
    });
  } else if (floorId) {
    ApiFloor.detail({
      id: floorId,
      success: (res: any) => {
        formData.value.floorId = res.id;
        formData.value.floorName = res.name;
      },
    });
  }
  if (extKey) {
    formData.value.extKey = extKey.toString();
  }
  ApiRoom.roomStatusEnums({
    success: (res: any) => {
      roomStatus.value = res;
    },
  });
  ApiSupporting.all({
    success: (res: any) => {
      supportingList.value = res.map((item: any) => ({
        ...item,
        // options 自定义下拉选项关键代码
        content: () => optionRender(item),
      }));
    },
  });
});
</script>

<style lang="less" scoped>
.img-list-item:hover {
  .cover {
    opacity: 1;
  }
}

.img-list-item {
  border-radius: var(--td-radius-default);
  display: inline-block;
  padding: 10px;
  position: relative;

  .cover {
    position: absolute;
    left: 10px;
    top: 10px;
    bottom: 10px;
    right: 10px;
    background-color: var(--td-mask-active);
    z-index: 9;
    opacity: 0;
    transition: 0.2s;
    display: flex;
    align-items: center;
  }
}
</style>
