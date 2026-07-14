<template>
  <t-card class="list-card-container" :bordered="false">
    <div class="page-header">
      <div><h2>电池管理</h2><p>跟踪电池电量、健康度、流转状态和所属站点。</p></div>
      <div class="toolbar"><t-button theme="primary" @click="handleEdit()">新增电池</t-button><t-button theme="default" variant="outline" :disabled="!selectedRowKeys.length" @click="handleDelete()">删除</t-button></div>
    </div>
    <t-row :gutter="[16, 16]" class="query-row">
      <t-col :xs="12" :md="8" :xl="6"><t-input v-model="queryForm.searchKey" clearable placeholder="搜索电池编号、型号或所属站点" @enter="getData(true)"><template #suffix-icon><search-icon size="16px" @click="getData(true)" /></template></t-input></t-col>
      <t-col :xs="12" :md="4" :xl="3"><t-select v-model="queryForm.status" clearable placeholder="全部电池状态" @change="getData(true)"><t-option v-for="option in batteryStatusOptions" :key="option.value" :value="option.value" :label="option.label" /></t-select></t-col>
    </t-row>
    <t-table :data="data" :columns="columns" row-key="id" vertical-align="middle" :hover="true" :pagination="pagination" :selected-row-keys="selectedRowKeys" :loading="dataLoading" :header-affixed-top="headerAffixedTop" @change="handleTableChange" @select-change="handleSelectChange">
      <template #battery="{ row }"><div class="battery-cell"><strong>{{ row.batteryNo }}</strong><span>{{ row.model || '未设置型号' }} · {{ row.voltage || '-' }}V / {{ row.capacity || '-' }}Ah</span></div></template>
      <template #power="{ row }"><div class="metric"><span class="meter"><i :style="{ width: `${metric(row.powerPercent)}%` }" /></span><b>{{ metric(row.powerPercent) }}%</b></div></template>
      <template #health="{ row }"><div class="metric health"><span class="meter"><i :style="{ width: `${metric(row.healthPercent)}%` }" /></span><b>{{ metric(row.healthPercent) }}%</b></div></template>
      <template #status="{ row }"><t-tag variant="light" :theme="batteryStatus(row.status).theme">{{ batteryStatus(row.status).label }}</t-tag></template>
      <template #op="{ row }"><t-space size="small"><t-link theme="primary" hover="color" @click="handleEdit(row.id)">编辑</t-link><t-link theme="danger" hover="color" @click="handleDelete(row)">删除</t-link></t-space></template>
    </t-table>
    <t-dialog v-model:visible="confirmDeleteVisible" header="确认删除" :body="confirmDeleteBody" @confirm="confirmDelete" />
  </t-card>
</template>
<script lang="ts">export default { name: 'swapBatteryIndex' };</script>
<script setup lang="ts">
import { SearchIcon } from 'tdesign-icons-vue-next';
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import ApiSwapBattery from '@/api/energy/ApiSwapBattery';
import { queryDef, paginationDef } from '@/api/common/query';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';
const router = useRouter(); const store = useSettingStore(); const data = ref<any[]>([]); const pagination = ref({ ...paginationDef }); const queryForm = ref({ ...queryDef, status: undefined as number | undefined }); const dataLoading = ref(false); const selectedRowKeys = ref<(string | number)[]>([]); const confirmDeleteVisible = ref(false);
const batteryStatusOptions = [{ value: 1, label: '可租' }, { value: 2, label: '使用中' }, { value: 3, label: '暂存中' }, { value: 4, label: '维修中' }, { value: 0, label: '已停用' }];
const batteryStatus = (value: number) => ({ 1: { label: '可租', theme: 'success' }, 2: { label: '使用中', theme: 'primary' }, 3: { label: '暂存中', theme: 'warning' }, 4: { label: '维修中', theme: 'danger' }, 0: { label: '已停用', theme: 'default' } }[Number(value)] || { label: '未知', theme: 'default' }) as { label: string; theme: any };
const metric = (value: unknown) => Math.max(0, Math.min(100, Number(value) || 0));
const columns: PrimaryTableCol[] = [{ colKey: 'row-select', type: 'multiple', width: 52, fixed: 'left' }, { title: '电池信息', colKey: 'battery', minWidth: 220 }, { title: '所属站点 ID', colKey: 'stationId', width: 150 }, { title: '当前电量', colKey: 'power', width: 145 }, { title: '健康度', colKey: 'health', width: 145 }, { title: '状态', colKey: 'status', width: 105 }, { title: '最近换电', colKey: 'lastSwapTime', width: 170 }, { title: '操作', colKey: 'op', fixed: 'right', width: 110 }];
const headerAffixedTop = computed(() => ({ offsetTop: store.state.isUseTabsRouter ? 48 : 0, container: `.${prefix}-layout` }));
const confirmDeleteBody = computed(() => `确认删除已选择的 ${selectedRowKeys.value.length} 块电池吗？删除后无法恢复。`);
function getData(reload = false) { if (reload) pagination.value.current = 1; dataLoading.value = true; ApiSwapBattery.page({ data: queryForm.value, pagination: pagination.value, success: (res: any) => { data.value = res.records || []; pagination.value.total = res.total || 0; dataLoading.value = false; }, fail: () => { dataLoading.value = false; } }); }
function handleTableChange(changeParams: any) { pagination.value = changeParams.pagination; getData(); }
function handleSelectChange(keys: (string | number)[]) { selectedRowKeys.value = keys; }
function handleEdit(id?: string | number) { router.push(`/energy/swap-battery-edit${id ? `?id=${id}` : ''}`); }
function handleDelete(row?: any) { if (row) selectedRowKeys.value = [row.id]; if (!selectedRowKeys.value.length) return MessagePlugin.warning('请先选择需要删除的电池'); confirmDeleteVisible.value = true; }
function confirmDelete() { ApiSwapBattery.delete({ ids: selectedRowKeys.value, success: () => { MessagePlugin.success('电池已删除'); confirmDeleteVisible.value = false; selectedRowKeys.value = []; getData(); } }); }
onMounted(() => getData());
</script>
<style lang="less" scoped>
.page-header { display: flex; justify-content: space-between; gap: 16px; margin-bottom: 20px; } h2 { margin: 0; font-size: 20px; color: var(--td-text-color-primary); } p { margin: 6px 0 0; color: var(--td-text-color-secondary); } .toolbar, .query-row { margin-bottom: 16px; }.battery-cell { display: flex; flex-direction: column; gap: 4px; }.battery-cell span { color: var(--td-text-color-secondary); font-size: 12px; }.metric { display: flex; align-items: center; gap: 8px; }.metric b { min-width: 34px; color: #2ba471; font-size: 12px; }.metric.health b { color: #0052d9; }.meter { width: 70px; height: 6px; border-radius: 99px; overflow: hidden; background: var(--td-bg-color-secondarycontainer); }.meter i { display: block; height: 100%; border-radius: inherit; background: #2ba471; }.health .meter i { background: #0052d9; } @media screen and (max-width: 768px) { .page-header { align-items: flex-start; flex-direction: column; } }
</style>
