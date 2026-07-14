<template>
  <t-card class="list-card-container" :bordered="false">
    <div class="page-header"><div><h2>维修工单</h2><p>集中处理用户上报的换电站和电池故障。</p></div><div class="toolbar"><t-button theme="primary" @click="handleEdit()">新建工单</t-button><t-button theme="default" variant="outline" :disabled="!selectedRowKeys.length" @click="handleDelete()">删除</t-button></div></div>
    <t-row :gutter="[16, 16]" class="query-row"><t-col :xs="12" :md="7" :xl="5"><t-input v-model="queryForm.searchKey" clearable placeholder="搜索工单编号、故障类型、用户 ID" @enter="getData(true)"><template #suffix-icon><search-icon size="16px" @click="getData(true)" /></template></t-input></t-col><t-col :xs="12" :md="4" :xl="3"><t-select v-model="queryForm.status" clearable placeholder="全部工单状态" @change="getData(true)"><t-option v-for="option in repairStatusOptions" :key="option.value" :value="option.value" :label="option.label" /></t-select></t-col></t-row>
    <t-table :data="data" :columns="columns" row-key="id" vertical-align="middle" :hover="true" :pagination="pagination" :selected-row-keys="selectedRowKeys" :loading="dataLoading" :header-affixed-top="headerAffixedTop" @change="handleTableChange" @select-change="handleSelectChange">
      <template #workOrder="{ row }"><div class="order-cell"><strong>{{ row.orderNo }}</strong><span>{{ row.repairType || '未分类' }} · 用户 {{ row.userId || '-' }} · 站点 {{ row.stationId || '-' }}</span></div></template>
      <template #status="{ row }"><t-tag variant="light" :theme="repairStatus(row.status).theme">{{ repairStatus(row.status).label }}</t-tag></template>
      <template #description="{ row }"><span class="ellipsis">{{ row.description || '-' }}</span></template>
      <template #op="{ row }"><t-space size="small"><t-link theme="primary" hover="color" @click="handleEdit(row.id)">{{ Number(row.status) === 1 ? '受理' : '详情' }}</t-link><t-link theme="danger" hover="color" @click="handleDelete(row)">删除</t-link></t-space></template>
    </t-table>
    <t-dialog v-model:visible="confirmDeleteVisible" header="确认删除" :body="confirmDeleteBody" @confirm="confirmDelete" />
  </t-card>
</template>
<script lang="ts">export default { name: 'repairOrderIndex' };</script>
<script setup lang="ts">
import { SearchIcon } from 'tdesign-icons-vue-next';
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import ApiRepairOrder from '@/api/energy/ApiRepairOrder';
import { queryDef, paginationDef } from '@/api/common/query';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';
const router = useRouter(); const store = useSettingStore(); const data = ref<any[]>([]); const pagination = ref({ ...paginationDef }); const queryForm = ref({ ...queryDef, status: undefined as number | undefined }); const dataLoading = ref(false); const selectedRowKeys = ref<(string | number)[]>([]); const confirmDeleteVisible = ref(false);
const repairStatusOptions = [{ value: 1, label: '待处理' }, { value: 2, label: '处理中' }, { value: 3, label: '已完成' }, { value: 4, label: '已取消' }]; const repairStatus = (value: number) => ({ 1: { label: '待处理', theme: 'warning' }, 2: { label: '处理中', theme: 'primary' }, 3: { label: '已完成', theme: 'success' }, 4: { label: '已取消', theme: 'default' } }[Number(value)] || { label: '未知', theme: 'default' }) as { label: string; theme: any };
const columns: PrimaryTableCol[] = [{ colKey: 'row-select', type: 'multiple', width: 52, fixed: 'left' }, { title: '工单信息', colKey: 'workOrder', minWidth: 245 }, { title: '问题描述', colKey: 'description', minWidth: 220 }, { title: '联系电话', colKey: 'contactPhone', width: 140 }, { title: '工单状态', colKey: 'status', width: 105 }, { title: '处理人 ID', colKey: 'handlerId', width: 130 }, { title: '上报时间', colKey: 'createTime', width: 170 }, { title: '处理时间', colKey: 'repairTime', width: 170 }, { title: '操作', colKey: 'op', fixed: 'right', width: 110 }];
const headerAffixedTop = computed(() => ({ offsetTop: store.state.isUseTabsRouter ? 48 : 0, container: `.${prefix}-layout` })); const confirmDeleteBody = computed(() => `确认删除已选择的 ${selectedRowKeys.value.length} 张维修工单吗？删除后无法恢复。`);
function getData(reload = false) { if (reload) pagination.value.current = 1; dataLoading.value = true; ApiRepairOrder.page({ data: queryForm.value, pagination: pagination.value, success: (res: any) => { data.value = res.records || []; pagination.value.total = res.total || 0; dataLoading.value = false; }, fail: () => { dataLoading.value = false; } }); } function handleTableChange(changeParams: any) { pagination.value = changeParams.pagination; getData(); } function handleSelectChange(keys: (string | number)[]) { selectedRowKeys.value = keys; } function handleEdit(id?: string | number) { router.push(`/energy/repair-order-edit${id ? `?id=${id}` : ''}`); } function handleDelete(row?: any) { if (row) selectedRowKeys.value = [row.id]; if (!selectedRowKeys.value.length) return MessagePlugin.warning('请先选择需要删除的工单'); confirmDeleteVisible.value = true; } function confirmDelete() { ApiRepairOrder.delete({ ids: selectedRowKeys.value, success: () => { MessagePlugin.success('工单已删除'); confirmDeleteVisible.value = false; selectedRowKeys.value = []; getData(); } }); }
onMounted(() => getData());
</script>
<style lang="less" scoped>
.page-header { display: flex; justify-content: space-between; gap: 16px; margin-bottom: 20px; } h2 { margin: 0; font-size: 20px; color: var(--td-text-color-primary); } p { margin: 6px 0 0; color: var(--td-text-color-secondary); } .toolbar, .query-row { margin-bottom: 16px; }.order-cell { display: flex; flex-direction: column; gap: 4px; }.order-cell span { color: var(--td-text-color-secondary); font-size: 12px; }.ellipsis { display: -webkit-box; overflow: hidden; color: var(--td-text-color-secondary); -webkit-box-orient: vertical; -webkit-line-clamp: 2; } @media screen and (max-width: 768px) { .page-header { align-items: flex-start; flex-direction: column; } }
</style>
