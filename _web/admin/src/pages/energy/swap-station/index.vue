<template>
  <t-card class="list-card-container" :bordered="false">
    <div class="page-header">
      <div>
        <h2>换电站</h2>
        <p>维护站点位置、营业状态与实时库存。</p>
      </div>
      <div class="toolbar">
        <t-button theme="primary" @click="handleEdit()">新增站点</t-button>
        <t-button theme="default" variant="outline" :disabled="!selectedRowKeys.length" @click="handleDelete()">删除</t-button>
      </div>
    </div>

    <t-row :gutter="[16, 16]" class="query-row">
      <t-col :xs="12" :md="8" :xl="6">
        <t-input v-model="queryForm.searchKey" clearable placeholder="搜索站点名称、编号、城市或地址" @enter="getData(true)">
          <template #suffix-icon><search-icon size="16px" @click="getData(true)" /></template>
        </t-input>
      </t-col>
      <t-col :xs="12" :md="4" :xl="3">
        <t-select v-model="queryForm.status" clearable placeholder="全部营业状态" @change="getData(true)">
          <t-option v-for="option in stationStatusOptions" :key="option.value" :value="option.value" :label="option.label" />
        </t-select>
      </t-col>
    </t-row>

    <t-table
      :data="data"
      :columns="columns"
      row-key="id"
      vertical-align="middle"
      :hover="true"
      :pagination="pagination"
      :selected-row-keys="selectedRowKeys"
      :loading="dataLoading"
      :header-affixed-top="headerAffixedTop"
      @change="handleTableChange"
      @select-change="handleSelectChange"
    >
      <template #station="{ row }">
        <div class="station-cell">
          <strong>{{ row.name }}</strong>
          <span>{{ row.stationCode }} · {{ row.city }}{{ row.district ? ` · ${row.district}` : '' }}</span>
        </div>
      </template>
      <template #inventory="{ row }">
        <div class="inventory-cell">
          <b>{{ row.availableBatteryCount ?? 0 }}</b><span>可用电池</span>
          <b>{{ row.availableReturnSlots ?? 0 }}</b><span>可还空位</span>
        </div>
      </template>
      <template #status="{ row }">
        <t-tag variant="light" :theme="stationStatus(row.status).theme">{{ stationStatus(row.status).label }}</t-tag>
      </template>
      <template #op="{ row }">
        <t-space size="small">
          <t-link theme="primary" hover="color" @click="handleEdit(row.id)">编辑</t-link>
          <t-link theme="danger" hover="color" @click="handleDelete(row)">删除</t-link>
        </t-space>
      </template>
    </t-table>
    <t-dialog v-model:visible="confirmDeleteVisible" header="确认删除" :body="confirmDeleteBody" @confirm="confirmDelete" />
  </t-card>
</template>

<script lang="ts">
export default { name: 'swapStationIndex' };
</script>

<script setup lang="ts">
import { SearchIcon } from 'tdesign-icons-vue-next';
import type { PrimaryTableCol } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import ApiSwapStation from '@/api/energy/ApiSwapStation';
import { queryDef, paginationDef } from '@/api/common/query';
import { prefix } from '@/config/global';
import { useSettingStore } from '@/store';

const router = useRouter();
const store = useSettingStore();
const data = ref<any[]>([]);
const pagination = ref({ ...paginationDef });
const queryForm = ref({ ...queryDef, status: undefined as number | undefined });
const dataLoading = ref(false);
const selectedRowKeys = ref<(string | number)[]>([]);
const selectedRowData = ref<any[]>([]);
const confirmDeleteVisible = ref(false);
const stationStatusOptions = [
  { value: 1, label: '营业中' },
  { value: 2, label: '维护中' },
  { value: 0, label: '已停用' },
];
const stationStatus = (value: number) => ({
  1: { label: '营业中', theme: 'success' },
  2: { label: '维护中', theme: 'warning' },
  0: { label: '已停用', theme: 'default' },
}[Number(value)] || { label: '未知', theme: 'default' }) as { label: string; theme: any };
const columns: PrimaryTableCol[] = [
  { colKey: 'row-select', type: 'multiple', width: 52, fixed: 'left' },
  { title: '站点信息', colKey: 'station', minWidth: 220 },
  { title: '详细地址', colKey: 'address', minWidth: 260, ellipsis: true },
  { title: '营业时间', colKey: 'businessHours', width: 130 },
  { title: '库存概览', colKey: 'inventory', width: 170 },
  { title: '状态', colKey: 'status', width: 100 },
  { title: '评分', colKey: 'rating', width: 80 },
  { title: '更新时间', colKey: 'updateTime', width: 170 },
  { title: '操作', colKey: 'op', fixed: 'right', width: 110 },
];
const headerAffixedTop = computed(() => ({ offsetTop: store.state.isUseTabsRouter ? 48 : 0, container: `.${prefix}-layout` }));
const confirmDeleteBody = computed(() => `确认删除已选择的 ${selectedRowKeys.value.length} 个换电站吗？删除后无法恢复。`);

function getData(reload = false) {
  if (reload) pagination.value.current = 1;
  dataLoading.value = true;
  ApiSwapStation.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res: any) => {
      data.value = res.records || [];
      pagination.value.total = res.total || 0;
      dataLoading.value = false;
    },
    fail: () => { dataLoading.value = false; },
  });
}
function handleTableChange(changeParams: any) {
  pagination.value = changeParams.pagination;
  getData();
}
function handleSelectChange(keys: (string | number)[], context: any) {
  selectedRowKeys.value = keys;
  selectedRowData.value = context.selectedRowData || [];
}
function handleEdit(id?: number | string) {
  router.push(`/energy/swap-station-edit${id ? `?id=${id}` : ''}`);
}
function handleDelete(row?: any) {
  if (row) {
    selectedRowKeys.value = [row.id];
    selectedRowData.value = [row];
  }
  if (!selectedRowKeys.value.length) return MessagePlugin.warning('请先选择需要删除的站点');
  confirmDeleteVisible.value = true;
}
function confirmDelete() {
  ApiSwapStation.delete({
    ids: selectedRowKeys.value,
    success: () => {
      MessagePlugin.success('站点已删除');
      confirmDeleteVisible.value = false;
      selectedRowKeys.value = [];
      selectedRowData.value = [];
      getData();
    },
  });
}
onMounted(() => getData());
</script>

<style lang="less" scoped>
.page-header { display: flex; justify-content: space-between; gap: 16px; margin-bottom: 20px; }
h2 { margin: 0; font-size: 20px; color: var(--td-text-color-primary); }
p { margin: 6px 0 0; color: var(--td-text-color-secondary); }
.toolbar, .query-row { margin-bottom: 16px; }
.station-cell { display: flex; flex-direction: column; gap: 4px; }
.station-cell span { color: var(--td-text-color-secondary); font-size: 12px; }
.inventory-cell { display: grid; grid-template-columns: auto 1fr; column-gap: 8px; row-gap: 4px; align-items: center; }
.inventory-cell b { color: var(--td-brand-color); }
.inventory-cell span { color: var(--td-text-color-secondary); font-size: 12px; }
@media screen and (max-width: 768px) { .page-header { align-items: flex-start; flex-direction: column; } }
</style>
