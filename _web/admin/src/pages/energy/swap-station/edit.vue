<template>
  <t-loading :loading="loading">
    <t-form class="base-form" :data="formData" :rules="FORM_RULES" label-align="top" :label-width="100" @keydown.enter.prevent @reset="onReset" @submit="onSubmit">
      <div class="form-basic-container"><div class="form-basic-item"><div class="form-basic-container-title">{{ formData.id ? '编辑换电站' : '新增换电站' }}</div><p class="form-tip">站点信息会同步到小程序附近站点与详情页。</p>
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :xs="12" :lg="6"><t-form-item label="站点名称" name="name"><t-input v-model="formData.name" :maxlength="80" placeholder="例如：科技园换电站" /></t-form-item></t-col>
          <t-col :xs="12" :lg="6"><t-form-item label="站点编号" name="stationCode"><t-input v-model="formData.stationCode" :maxlength="32" placeholder="例如：SZ-NS-001" /></t-form-item></t-col>
          <t-col :xs="12" :lg="6"><t-form-item label="城市" name="city"><t-input v-model="formData.city" :maxlength="40" placeholder="例如：深圳市" /></t-form-item></t-col>
          <t-col :xs="12" :lg="6"><t-form-item label="区域" name="district"><t-input v-model="formData.district" :maxlength="40" placeholder="例如：南山区" /></t-form-item></t-col>
          <t-col :span="12"><t-form-item label="详细地址" name="address"><t-input v-model="formData.address" :maxlength="255" placeholder="请输入可用于导航的详细地址" /></t-form-item></t-col>
          <t-col :xs="12" :lg="6"><t-form-item label="经度" name="longitude"><t-input-number v-model="formData.longitude" :decimal-places="6" placeholder="例如：113.945320" /></t-form-item></t-col>
          <t-col :xs="12" :lg="6"><t-form-item label="纬度" name="latitude"><t-input-number v-model="formData.latitude" :decimal-places="6" placeholder="例如：22.540210" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="营业时间" name="businessHours"><t-input v-model="formData.businessHours" :maxlength="50" placeholder="06:00–24:00" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="联系电话" name="contactPhone"><t-input v-model="formData.contactPhone" :maxlength="30" placeholder="请输入联系电话" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="营业状态" name="status"><t-select v-model="formData.status"><t-option :value="1" label="营业中" /><t-option :value="2" label="维护中" /><t-option :value="0" label="已停用" /></t-select></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="电柜总格口数" name="totalCabinets"><t-input-number v-model="formData.totalCabinets" :min="0" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="可用电池数" name="availableBatteryCount"><t-input-number v-model="formData.availableBatteryCount" :min="0" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="可还空位数" name="availableReturnSlots"><t-input-number v-model="formData.availableReturnSlots" :min="0" /></t-form-item></t-col>
          <t-col :xs="12" :lg="4"><t-form-item label="站点评分" name="rating"><t-input-number v-model="formData.rating" :min="0" :max="5" :step="0.1" :decimal-places="1" /></t-form-item></t-col>
          <t-col :xs="12" :lg="8"><t-form-item label="服务设施" name="facilities"><t-input v-model="formData.facilities" :maxlength="2000" placeholder="例如：卫生间、休息区、饮水机、停车位" /></t-form-item></t-col>
          <t-col :span="12"><t-form-item label="站点公告" name="announcement"><t-textarea v-model="formData.announcement" :maxlength="2000" placeholder="请输入将展示在小程序站点详情页的公告" /></t-form-item></t-col>
        </t-row>
      </div></div>
      <div class="form-submit-container"><div class="form-submit-sub"><div class="form-submit-left"><t-button theme="primary" class="form-submit-confirm" type="submit">保存站点</t-button><t-button type="reset" class="form-submit-cancel" theme="default" variant="base">重置</t-button></div></div></div>
    </t-form>
  </t-loading>
</template>
<script lang="ts">export default { name: 'swapStationEdit' };</script>
<script setup lang="ts">
import type { SubmitContext } from 'tdesign-vue-next';
import type { FormRule } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import ApiSwapStation from '@/api/energy/ApiSwapStation';
import router from '@/router';
import { closeOrBack } from '@/utils/url-utils';
const route = useRoute(); const loading = ref(true);
const formData = ref<any>({ id: null, stationCode: '', name: '', city: '', district: '', address: '', longitude: null, latitude: null, businessHours: '06:00–24:00', contactPhone: '', status: 1, totalCabinets: 0, availableBatteryCount: 0, availableReturnSlots: 0, rating: 5, facilities: '', announcement: '', createTime: '', updateTime: '' });
const FORM_RULES: Record<string, FormRule[]> = { name: [{ required: true, message: '请输入站点名称', trigger: 'blur' }], stationCode: [{ required: true, message: '请输入站点编号', trigger: 'blur' }], city: [{ required: true, message: '请输入城市', trigger: 'blur' }], address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }], status: [{ required: true, number: true, message: '请选择营业状态', trigger: 'change' }] };
function onReset() { if (!formData.value.id) Object.assign(formData.value, { stationCode: '', name: '', city: '', district: '', address: '', longitude: null, latitude: null, businessHours: '06:00–24:00', contactPhone: '', status: 1, totalCabinets: 0, availableBatteryCount: 0, availableReturnSlots: 0, rating: 5, facilities: '', announcement: '' }); }
function onSubmit(context: SubmitContext) { if (context.validateResult !== true) return; ApiSwapStation.edit({ data: formData.value, success: () => { MessagePlugin.success('站点已保存'); closeOrBack(route, router); } }); }
onMounted(() => { const { id } = route.query; if (!id) { loading.value = false; return; } ApiSwapStation.detail({ id, success: (res: any) => { formData.value = { ...formData.value, ...res }; loading.value = false; }, fail: () => { loading.value = false; } }); });
</script>
<style lang="less" scoped>.form-tip { margin: -4px 0 20px; color: var(--td-text-color-secondary); }</style>
