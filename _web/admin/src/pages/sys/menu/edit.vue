<template>
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
        <div class="form-basic-container-title">编辑</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="菜单名" name="name">
              <t-input v-model="formData.name" placeholder="请输入菜单名" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="路径简写" name="path">
              <t-input v-model="formData.path" placeholder="请输入路径简写" :maxlength="255"
                       :disabled="formData.system"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="上级菜单" name="parentId">
              <t-input :value="formData.parentName" placeholder="请选择上级菜单" clearable
                       :disabled="formData.system"
                       @clear="(e)=>{formData.parentId = null;formData.parentName = '';formData.menuType=1;e.e.stopPropagation();}"
                       @focus="showSelectParent = true"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="icon" name="icon">
              <icon-show v-if="formData.icon" slot="prefix-icon" :name="formData.icon" :size="20"
                         :type="formData.iconType" style="margin-right: 10px"></icon-show>
              <t-input :value="formData.icon" placeholder="请选择icon"
                       :maxlength="255"
                       clearable
                       @clear="(e)=>{formData.icon='';formData.iconType=null;e.e.stopPropagation();}"
                       @focus="showSelectIcon = true">
              </t-input>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="类型" name="menuType">
              <t-select v-model="formData.menuType" placeholder="请选择类型" :maxlength="255"
                        :disabled="formData.system">
                <t-option :value="1" label="布局" :key="1"></t-option>
                <t-option :value="2" label="新窗口" :key="2"></t-option>
                <t-option :value="3" label="iframe" :key="3"></t-option>
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6" v-if="formData.menuType == 1">
            <t-form-item label="布局路径" name="url">
              <t-input v-model="formData.url" placeholder="请输入布局路径" :maxlength="255"
                       :disabled="formData.system"/>
            </t-form-item>
          </t-col>

          <t-col :span="6" v-if="formData.menuType != 1">
            <t-form-item label="跳转地址" name="url">
              <t-input v-model="formData.url" placeholder="请输入跳转地址" :maxlength="255"/>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="归属平台类型" name="menuPlatformType">
              <t-select v-model="formData.menuPlatformType" placeholder="请选择归属平台类型" :maxlength="255"
                        :disabled="formData.parentId != null || formData.system">
                <t-option :value="1" label="管理端" key="1"></t-option>
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="用户类型" name="userType">
              <t-select v-model="formData.userType" placeholder="用户类型" :maxlength="255"
                        :disabled="formData.parentId != null || formData.system">
                <t-option value="USER" label="系统用户" key="1"></t-option>
              </t-select>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是否隐藏菜单" name="hidden">
              <t-radio-group v-model="formData.hidden">
                <t-radio :value="true">是</t-radio>
                <t-radio :value="false">否</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是否保持状态" name="keepAlive">
              <t-radio-group v-model="formData.keepAlive">
                <t-radio :value="true">是</t-radio>
                <t-radio :value="false">否</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="是与否有效" name="status">
              <t-radio-group v-model="formData.status">
                <t-radio :value="1">启用</t-radio>
                <t-radio :value="0">禁用</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="排序号" name="orderNo">
              <t-input-number :min="0" v-model="formData.orderNo" placeholder="请输入排序号"/>
            </t-form-item>
          </t-col>
          <template v-if="formData.id">
            <t-col :span="6">
              <t-form-item label="创建时间" name="createTime">
                <t-input :value="formData.createTime" placeholder="" disabled/>
              </t-form-item>
            </t-col>

            <t-col :span="6">
              <t-form-item label="更新时间" name="updateTime">
                <t-input :value="formData.updateTime" placeholder="" disabled/>
              </t-form-item>
            </t-col>
          </template>

        </t-row>
      </div>
    </div>

    <div class="form-submit-container">
      <div class="form-submit-sub">
        <div class="form-submit-left">
          <t-button theme="primary" class="form-submit-confirm" type="submit">提交</t-button>
          <t-button type="reset" class="form-submit-cancel" theme="default" variant="base">重置</t-button>
        </div>
      </div>
    </div>

    <t-dialog v-model:visible="showSelectParent" header="选择上级菜单" :footer="false" width="800">
      <menu-select
        v-if="showSelectParent"
        :ne="formData.id"
        @selected="onParentSelected"
        @cancel="showSelectParent = false"
        user-type="manager"></menu-select>
    </t-dialog>

    <t-dialog v-model:visible="showSelectIcon" header="选择图标" :footer="false" width="800">
      <svg-icon-select @selected="selectedIcon"></svg-icon-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">

  export default {
    name: 'menuEdit',
  };
</script>

<script setup lang="ts">
  import type {SubmitContext} from 'tdesign-vue-next';
  import {FormRule, MessagePlugin} from 'tdesign-vue-next';
  import {onMounted, ref} from 'vue';
  import {useRoute} from 'vue-router';

  import ApiMenu from '@/api/sys/ApiMenu';
  import SvgIconSelect from '@/components/svg-icon-select/index.vue';
  import IconShow from '@/components/svg-icon-select/icon-show.vue';
  import router from '@/router';
  import MenuSelect from "./select.vue";

  import {MENU_TYPE} from "@/constants";
  // 自定义校验 start -------------------
  const urlValidator: any = (val: string) => {
    if (formData.value.menuType == 2 && !val.match(/(http|https):\/\/([\w.]+\/?)\S*/)) {
      return {result: false, message: '新窗口地址必须以http或https开头', type: 'error'};
    }
    return {result: true, type: 'success'};
  };
  // 自定义校验 end -------------------

  // 定义变量 start -------------------
  // 路由
  const route = useRoute();
  // 显示选择上级弹窗
  const showSelectParent = ref(false);
  const showSelectIcon = ref(false);
  const menuTypes = ref(MENU_TYPE);
  // 表单
  const formData = ref({
    id: null,
    name: '',
    parentId: null,
    userType: 'USER',
    parentName: '',
    url: '',
    icon: '',
    path: '',
    iconType: null,
    menuType: 1,
    menuPlatformType: 1,
    status: 1,
    blank: false,
    hidden: false,
    keepAlive: false,
    orderNo: 0,
    system: false,
    createTime: '',
    updateTime: '',
  });
  // 表单校验
  const FORM_RULES: Record<string, FormRule[]> = {
    name: [{required: true, message: '菜单名不能为空', trigger: 'blur'}],
    menuType: [{required: true, message: '类型不能为空', trigger: 'blur'}],
    menuPlatformType: [{required: true, message: '归属平台类型不能为空', trigger: 'blur'}],
    status: [{required: true, message: '是否有效不能为空',}],
    path: [{required: true, message: '路径不能为空',}],
    userType: [{required: true, message: '用户类型不能为空',}],
    url: [{validator: urlValidator}],
    blank: [{required: true, message: '是否新开标签不能为空',}]
  };
  // 定义变量 end -------------------

  // 定义方法 start -------------------
  const onParentSelected = (data: any) => {
    formData.value.menuPlatformType = data.menuPlatformType.code;
    formData.value.parentId = data.id;
    formData.value.parentName = data.name;
    showSelectParent.value = false
  }
  // 重置表单
  const onReset = () => {
  };
  // 提交表单
  const onSubmit = (ctx: SubmitContext) => {
    if (ctx.validateResult === true) {
      ApiMenu.edit({
        data: formData.value,
        success: (res: any) => {
          MessagePlugin.success('已完成');
          router.back();
        }
      });
    }
  };

  const selectedIcon = (icon: any) => {
    formData.value.icon = icon.name;
    formData.value.iconType = icon.type;
    showSelectIcon.value = false;
  }
  // 定义方法 end -------------------

  // vue生命周期
  onMounted(() => {
    const {id} = route.query;
    if (id) {
      ApiMenu.detail({
      id: id,
        success: (res: any) => {
          res.status = res.status ? res.status.code : 1
          res.iconType = res.iconType ? res.iconType.code : 1
          res.menuType = res.menuType ? res.menuType.code : 1
          res.menuPlatformType = res.menuPlatformType ? res.menuPlatformType.code : 1
          formData.value = res;
        }
      });
    }
  });
</script>

<style lang="less" scoped></style>
