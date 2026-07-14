<template>
  <t-card class="list-card-container" :bordered="false">
    <div class="page-header"><div><h2>换电订单</h2><p>统一查看换电、暂存与租赁业务的订单流转。</p></div><div class="toolbar"><t-button theme="primary" @click="handleEdit()">新建订单</t-button><t-button theme="default" variant="outline" :disabled="!selectedRowKeys.length" @click="handleDelete()">删除</t-button></div></div>
    <t-row :gutter="[16, 16]" class="query-row">
      <t-col :xs="12" :md="7" :xl="5"><t-input v-model="queryForm.searchKey" clearable placeholder="搜索订单号、用户 ID、站点 ID" @enter="getData(true)"><template #suffix-icon><search-icon size="16px" @click="getData(true)" /></template></t-input></t-col>
      <t-col :xs="6" :md="3" :xl="2"><t-select v-model="queryForm.orderType" clearable placeholder="全部类型" @change="getData(true)"><t-option v-for="option in orderTypeOptions" :key="option.value" :value="option.value" :label="option.label" /></t-select></t-col>
      <t-col :xs="6" :md="3" :xl="2"><t-select v-model="queryForm.status" clearable placeholder="全部状态" @change="getData(true)"><t-option v-for="option in orderStatusOptions" :key="option.value" :value="option.value" :label="option.label" /></t-select></t-col>
    </t-row>
    <t-table :data="data" :columns="columns" row-key="id" vertical-align="middle" :hover="true" :pagination="pagination" :selected-row-keys="selectedRowKeys" :loading="dataLoading" :header-affixed-top="headerAffixedTop" @change="handleTableChange" @select-change="handleSelectChange">
      <template #order="{ row }"><div class="order-cell"><strong>{{ row.orderNo }}</strong><span>用户 {{ row.userId || '-' }} · 站点 {{ row.stationId || '-' }} · 电池 {{ row.batteryId || '-' }}</span></div></template>
      <template #orderType="{ row }"><t-tag variant="light" theme="primary">{{ orderType(row.orderType) }}</t-tag></template>
      <template #status="{ row }"><t-tag variant="light" :theme="orderStatus(row.status).theme">{{ orderStatus(row.status).label }}</t-tag></template>
      <template #amount="{ row }"><b class="amount">¥{{ money(row.amount) }}</b></template>
      <template #op="{ row }"><t-space size="small"><t-link theme="primary" hover="color" @click="handleEdit(row.id)">详情</t-link><t-link theme="danger" hover="color" @click="handleDelete(row)">删除</t-link></t-space></template>
    </t-table>
    <t-dialog v-model:visible="confirmDeleteVisible" header="确认删除" :body="confirmDeleteBody" @confirm="confirmDelete" />
  </t-card>
</template>
<script lang="ts">export default { name: 'swapOrderIndex' };</script>
<script setup lang="ts">
import { SearchIcon } from 'tdesign-icons-vue-next';
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import ApiSwapOrder from '@/api/energy/ApiSwapOrder';
import { queryDef, paginationDef } from '@/api/common/query';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';
const router = useRouter(); const store = useSettingStore(); const data = ref<any[]>([]); const pagination = ref({ ...paginationDef }); const queryForm = ref({ ...queryDef, orderType: undefined as number | undefined, status: undefined as number | undefined }); const dataLoading = ref(false); const selectedRowKeys = ref<(string | number)[]>([]); const confirmDeleteVisible = ref(false);
const orderTypeOptions = [{ value: 1, label: '换电' }, { value: 2, label: '暂存' }, { value: 3, label: '租赁' }]; const orderStatusOptions = [{ value: 1, label: '进行中' }, { value: 2, label: '已完成' }, { value: 3, label: '异常' }, { value: 4, label: '已取消' }];
const orderType = (value: number) => ({ 1: '换电', 2: '暂存', 3: '租赁' }[Number(value)] || '未知'); const orderStatus = (value: number) => ({ 1: { label: '进行中', theme: 'primary' }, 2: { label: '已完成', theme: 'success' }, 3: { label: '异常', theme: 'danger' }, 4: { label: '已取消', theme: 'default' } }[Number(value)] || { label: '未知', theme: 'default' }) as { label: string; theme: any }; const money = (value: unknown) => Number(value || 0).toFixed(2);
const columns: PrimaryTableCol[] = [{ colKey: 'row-select', type: 'multiple', width: 52, fixed: 'left' }, { title: '订单信息', colKey: 'order', minWidth: 255 }, { title: '业务类型', colKey: 'orderType', width: 105 }, { title: '订单状态', colKey: 'status', width: 105 }, { title: '实付金额', colKey: 'amount', width: 110 }, { title: '开始时间', colKey: 'startTime', width: 170 }, { title: '结束时间', colKey: 'endTime', width: 170 }, { title: '备注', colKey: 'remark', minWidth: 160, ellipsis: true }, { title: '操作', colKey: 'op', fixed: 'right', width: 110 }];
const headerAffixedTop = computed(() => ({ offsetTop: store.state.isUseTabsRouter ? 48 : 0, container: `.${prefix}-layout` })); const confirmDeleteBody = computed(() => `确认删除已选择的 ${selectedRowKeys.value.length} 条订单吗？删除后无法恢复。`);
function getData(reload = false) { if (reload) pagination.value.current = 1; dataLoading.value = true; ApiSwapOrder.page({ data: queryForm.value, pagination: pagination.value, success: (res: any) => { data.value = res.records || []; pagination.value.total = res.total || 0; dataLoading.value = false; }, fail: () => { dataLoading.value = false; } }); }
function handleTableChange(changeParams: any) { pagination.value = changeParams.pagination; getData(); } function handleSelectChange(keys: (string | number)[]) { selectedRowKeys.value = keys; } function handleEdit(id?: string | number) { router.push(`/energy/swap-order-edit${id ? `?id=${id}` : ''}`); } function handleDelete(row?: any) { if (row) selectedRowKeys.value = [row.id]; if (!selectedRowKeys.value.length) return MessagePlugin.warning('请先选择需要删除的订单'); confirmDeleteVisible.value = true; } function confirmDelete() { ApiSwapOrder.delete({ ids: selectedRowKeys.value, success: () => { MessagePlugin.success('订单已删除'); confirmDeleteVisible.value = false; selectedRowKeys.value = []; getData(); } }); }
onMounted(() => getData());
</script>
<style lang="less" scoped>
.page-header { display: flex; justify-content: space-between; gap: 16px; margin-bottom: 20px; } h2 { margin: 0; font-size: 20px; color: var(--td-text-color-primary); } p { margin: 6px 0 0; color: var(--td-text-color-secondary); } .toolbar, .query-row { margin-bottom: 16px; }.order-cell { display: flex; flex-direction: column; gap: 4px; }.order-cell span { color: var(--td-text-color-secondary); font-size: 12px; }.amount { color: var(--td-brand-color); font-variant-numeric: tabular-nums; } @media screen and (max-width: 768px) { .page-header { align-items: flex-start; flex-direction: column; } }
</style>
