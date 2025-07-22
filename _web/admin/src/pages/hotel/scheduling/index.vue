<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleClickEdit()">批量排班</t-button>
          <t-button variant="base" theme="default" :disabled="!selectedRowKeys.length" @click="handleClickDelete()"
          >清空排班
          </t-button
          >
          <p v-if="!!selectedRowKeys.length" class="selected-count">已选{{ selectedRowKeys.length }}项</p>
        </div>
        <div class="search-input">
          <t-input v-model="queryForm.searchKey" placeholder="请输入你需要搜索的内容" clearable @enter="getData(true)">
            <template #suffix-icon>
              <search-icon size="16px" @click="getData(true)"/>
            </template>
          </t-input>
        </div>
      </t-row>
      <t-table
        :data="data"
        :columns="columns"
        row-key="userId"
        vertical-align="top"
        :hover="true"
        :pagination="pagination"
        :selected-row-keys="selectedRowKeys"
        :loading="dataLoading"
        :header-affixed-top="headerAffixedTop"
        :sort="sort"
        :multipleSort="true"
        @sort-change="sortChange"
        @change="reHandleChange"
        @select-change="reHandleSelectChange"
        @cell-click="handleCellClickEdit"
      >
      </t-table>
    </t-card>

    <t-dialog v-model:visible="confirmDeleteVisible" header="确认" :body="confirmDeleteBody"
              @confirm="onConfirmDelete"/>


    <t-drawer :visible.sync="editSchedulingShow"
              size="360"
              header="编辑排班"
              :closeOnOverlayClick="true"
              :closeBtn="true"
              @closeBtnClick="editSchedulingShow = false"
              @overlayClick="editSchedulingShow = false">
      <t-form labelAlign="top"
              :rules="FORM_RULES"
              ref="roomOrderFormRef"
              v-if="editSchedulingShow"
              :data="editScheduling">
        <t-form-item label="">
          职员：{{ editScheduling.userName }}
        </t-form-item>
        <t-form-item label="">
          日期：{{ editScheduling.dateStart }}
        </t-form-item>

        <t-form-item label="">
          签到：
          <t-tag variant="light"
                 :theme="editScheduling.signStatus.code === 1 ? 'success':
                      (editScheduling.signStatus.code === 0 ? 'default':'danger')">
            {{ editScheduling.signStatus.text }}
          </t-tag>
        </t-form-item>
        <t-form-item label="班次：" name="timeScopeId" style="flex: 1">
          <t-input
            v-model="editScheduling.timeScopeName"
            placeholder="请选择班次"
            :readonly="true"
            :show-clear-icon-on-empty="true"
            clearable
            @clear="(e)=>{editScheduling.timeScopeName=null;editScheduling.timeScopeId=null;e.e.stopPropagation()}"
            @click="showSelectTimeScope = true"
          />
        </t-form-item>
        <t-form-item label="签到时间" name="signInTime">
          <t-time-picker style="width: 100%" v-model="editScheduling.signInTime" placeholder="强选择签到时间"/>
        </t-form-item>
        <t-form-item label="签退时间" name="signOutTime">
          <t-time-picker style="width: 100%" v-model="editScheduling.signOutTime" placeholder="强选择签退时间"/>
        </t-form-item>
      </t-form>
      <template #footer>
        <t-button theme="primary"
                  @click="onSubmitScheduling">
          {{ editScheduling.id ? '修改排班' : '保存排班' }}
        </t-button>
        <t-button theme="default"
                  v-if="editScheduling.id" @click="deleteScheduling">
          取消排班
        </t-button>
        <t-button theme="default" @click="editSchedulingShow = false"> 关闭</t-button>
      </template>
    </t-drawer>

    <t-dialog v-model:visible="showSelectTimeScope" header="选择班次" :footer="false" width="800"
              @cancel="showSelectTimeScope = false">
      <time-scope-select
        v-if="showSelectTimeScope"
        @cancel="showSelectTimeScope = false"
        @selected="onTimeScopeSelected"></time-scope-select>
    </t-dialog>
  </div>
</template>

<script lang="tsx">
export default {
  name: 'schedulingIndex',
};
</script>

<script setup lang="tsx">
import {SearchIcon} from 'tdesign-icons-vue-next';
import {FormRule, MessagePlugin, PrimaryTableCol, SubmitContext} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';

import ApiScheduling from '@/api/hotel/ApiScheduling';
import {prefix} from '@/config/global';
import {useSettingStore} from '@/store';
import dayjs from "dayjs";
import TimeScopeSelect from "@/pages/hotel/time-scope/select.vue";
import {closeOrBack} from "@/utils/url-utils";
import ApiRoomOrder from "@/api/hotel/ApiRoomOrder";
import {queryDef, paginationDef} from "@/api/common/query";

// 定义变量 start -------------------
// 排序
const sort = ref([{
  // 按照 name 字段进行排序
  sortBy: '',
  // 是否按照降序进行排序
  descending: false,
}]);
// 读取设置内容
const store = useSettingStore();
// 列表数据
const data = ref([]);
// 分页
const pagination = ref(paginationDef);
// 查询表单,包括分页
const queryForm = ref({
  ...queryDef,
  dateStart: '',
  dateEnd: '',
});
// 数据是否加载中
const dataLoading = ref(false);
// 是否显示确认删除弹窗
const confirmDeleteVisible = ref(false);
// 被选中的key
const selectedRowKeys = ref([]);
// 被选中的数据
const selectedRowData = ref([]);
const editScheduling = ref({
  id: '',
  dateStart: '',
  signInTime: '',
  userName: '',
  signOutTime: '',
  timeScopeId: '',
  timeScopeName: '',
  signStatus: {text: "", code: 0}
});
const editSchedulingShow = ref(false);
const showSelectTimeScope = ref(false);
const roomOrderFormRef = ref(null);
// 路由
const router = useRouter();

// 被选中的数据
const columns: any = ref([
  {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
  {title: '员工', colKey: 'userName', fixed: 'left'},
]);

// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  timeScopeId: [{required: true, message: '班次不能为空', type: 'error'}]
};
// 定义变量 end -------------------

// 定义方法 start -------------------

const initDate = (start: any, end: any) => {
  queryForm.value.dateStart = start;
  queryForm.value.dateEnd = end;
  columns.value = [
    {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
    {title: '员工', colKey: 'userName', fixed: 'left'},
  ]
  let s = dayjs(start)
  let e = dayjs(end)
  let x = 1;
  while (!s.isAfter(e) && x++ < 30) {
    let key = s.format('YYYY-MM-DD')
    columns.value.push(
      {
        title: s.format('MM月DD日'),
        colKey: key,
        fixed: '',
        cell: (h: any, {row}: { row: any }) => {
          if (!row[key]) {
            return ''
          }
          let color = row[key].signStatus.code == 1 ? (row[key].signOutTime ? 'var(--td-brand-color-6)' : 'var(--td-success-color-6)') :
            (row[key].signStatus.code == 0 ? 'var(--td-gray-color-6)' : 'var(--td-error-color-6)')
          let style = `width: 6px;height: 6px;background-color: ${color};border-radius: 50%;margin-right:4px`
          return <t-tag variant="light">
            <div style="cursor: pointer;display:flex;align-items: center;">
              <div style={style}></div>
              <div>{row[key].timeScopeName}</div>
            </div>
          </t-tag>;
        },
      },
    )
    s = s.add(1, 'day')
  }
};

// 获取列表数据
const getData = async (reload ?: boolean) => {
  if (reload) {
    pagination.value.current = 1;
  }
  dataLoading.value = true;
  ApiScheduling.page({
    data: queryForm.value,
    pagination: pagination.value,
    success: (res) => {
      res.records.forEach((user: any) => {
        user.schedulingList.forEach((item: any) => {
          user[item.date] = item;
        })
      })
      data.value = res.records;
      pagination.value.total = res.total;
      dataLoading.value = false;
    }
  });
};
// 重置选中数据
const resetIdx = () => {
  selectedRowData.value = [];
  selectedRowKeys.value = [];
};
// 构建确认删除内容
const confirmDeleteBody = () => {
  if (selectedRowData.value.length > 0) {
    const names = [];
    for (let i = 0; i < selectedRowData.value.length && i < 3; i++) {
      names.push(selectedRowData.value[i].userName);
    }
    return `确认清空 ${
      names.join(',') + (selectedRowData.value.length > names.length ? ` ...等 ${selectedRowData.value.length} 个职工排班数据吗？` : ' 排班数据吗？')
    }，删除后无法恢复`;
  }
  return '';
};
// 确认删除事件
const onConfirmDelete = () => {
  confirmDeleteVisible.value = false;
  // 真实业务请发起请求
  ApiScheduling.deleteByUserIds(selectedRowKeys.value, (res: any) => {
    MessagePlugin.success('删除成功');
    resetIdx();
    getData();
  });
};
// 表单选中数据变化
const reHandleSelectChange = (val: (string | number)[], e: any) => {
  selectedRowKeys.value = val;
  selectedRowData.value = e.selectedRowData;
};
// 排序变化
const sortChange = (sortInfo: any) => {
  // 对于受控属性而言，这里的赋值很重要，不可缺少
  sort.value = sortInfo;
  queryForm.value.orders = JSON.stringify(sort.value)
};
// 表单参数变化 包括过滤、分页
const reHandleChange = (changeParams: any, triggerAndData: unknown) => {
  pagination.value = changeParams.pagination;
  getData();
};
// 编辑/详情按钮事件
const handleClickEdit = (id?: any) => {
  router.push(`/hotel/scheduling-edit?id=${id ? id : ''}`);
};
const handleCellClickEdit = (e: any) => {
  editScheduling.value = {
    ...e.row[e.col.colKey],
    userIds: [e.row.userId],
    dateStart: e.col.colKey,
    dateEnd: e.col.colKey,
    userName: e.row.userName,
    timeScopeName: e.row[e.col.colKey].timeScopeName + ` ${e.row[e.col.colKey].timeScopeStartTime}-${e.row[e.col.colKey].timeScopeEndTime}`,
  }
  editSchedulingShow.value = true
};
// 单个删除按钮事件
const handleClickDelete = (row?: any) => {
  if (row) {
    selectedRowKeys.value = [row.id];
    selectedRowData.value = [row];
  }
  if (selectedRowData.value.length > 0) {
    confirmDeleteVisible.value = true;
  } else {
    MessagePlugin.warning('请先选择数据');
  }
};
// 处理表单顶部距离
const headerAffixedTop = computed(
  () =>
    ({
      offsetTop: store.isUseTabsRouter ? 48 : 0,
      container: `.${prefix}-layout`,
    } as any),
);

const onTimeScopeSelected = (val: any) => {
  showSelectTimeScope.value = false;
  editScheduling.value.timeScopeId = val.id
  editScheduling.value.timeScopeName = val.name + ` ${val.startTime}-${val.endTime}`
};

// 提交表单
const onSubmitScheduling = () => {
  roomOrderFormRef.value.validate().then((res: any) => {
    if (res === true) {
      ApiScheduling.edit({
        data: editScheduling.value,
        success: (res: any) => {
          MessagePlugin.success('已完成');
          pagination.value.current = 1
          getData()
          editSchedulingShow.value = false
        }
      });
    }
  })
};
const deleteScheduling = () => {
  ApiScheduling.delete({
    ids: [editScheduling.value.id],
    success: (res: any) => {
      MessagePlugin.success('已完成');
      pagination.value.current = 1
      getData()
      editSchedulingShow.value = false
    }
  });
};
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  initDate(dayjs().format('YYYY-MM-DD'), dayjs().add(30, 'day').format('YYYY-MM-DD'));
  getData();
});
</script>

<style lang="less" scoped>
</style>
