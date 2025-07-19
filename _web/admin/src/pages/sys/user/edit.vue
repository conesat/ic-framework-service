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
        <div class="form-basic-container-title">编辑用户</div>
        <!-- 表单内容 -->
        <t-row class="row-gap" :gutter="[32, 24]">
          <t-col :span="6">
            <t-form-item label="账号" name="username">
              <t-input v-model="formData.username" :disabled="formData.id !== ''" placeholder="请输入内容"/>
            </t-form-item>

            <t-form-item label="姓名" name="name">
              <t-input v-model="formData.name" placeholder="请输入内容"/>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="头像" name="avatarFileId">
              <cropper
                style="border-radius: 50%"
                :cover-width="400"
                :disabled="formData.su"
                :cover-height="400"
                :use-type="FileUseTypes.avatar"
                :url="formData.avatarFileUrl"
                @uploaded="onAvatarUploaded"
              ></cropper>
            </t-form-item>
          </t-col>

          <template v-if="!formData.id">
            <t-col :span="6">
              <t-form-item name="passwd" label="密码">
                <t-input v-model="formData.passwd" autocomplete="new-password" type="password" :maxlength="50"
                         placeholder="请输入密码"></t-input>
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item name="repasswd" label="确认密码">
                <t-input v-model="formData.repasswd" autocomplete="new-password" type="password" :maxlength="50"
                         placeholder="请确认密码"></t-input>
              </t-form-item>
            </t-col>
          </template>
          <t-col :span="6">
            <t-form-item label="性别" name="sex">
              <t-radio-group v-model="formData.sex">
                <t-radio :value="1"> 男</t-radio>
                <t-radio :value="2"> 女</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="是否启用" name="status">
              <t-radio-group v-model="formData.status"
                             :disabled="formData.su">
                <t-radio :value="1"> 启用</t-radio>
                <t-radio :value="0"> 禁用</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item name="phone" label="手机号">
              <t-input v-model="formData.phone" :maxlength="11" placeholder="请输入11数位手机号"></t-input>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item name="email" label="邮箱">
              <t-auto-complete v-model="formData.email"
                               autocomplete="off"
                               :options="emailOptions" filterable
                               :maxlength="100"
                               placeholder="请输入邮箱号"></t-auto-complete>
            </t-form-item>
          </t-col>

          <t-col :span="6">
            <t-form-item label="部门" name="depId">
              <t-tag-input v-model="deps" clearable
                           placeholder="请输选择部门"
                           @clear="(e)=>{deps = [];e.e.stopPropagation();}"
                           @click="showSelectDep = true">
                <template #valueDisplay="{ value, onClose }">
                  <t-tag
                    v-for="(item, index) in value"
                    :key="item"
                    closable
                    @close="() => onClose(index)"
                  >
                    {{ item.name }}
                  </t-tag>
                </template>
              </t-tag-input>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="岗位" name="depId">
              <t-tag-input v-model="pos" clearable
                           placeholder="请输选择岗位"
                           @clear="(e)=>{pos = [];e.e.stopPropagation();}"
                           @click="showSelectPos = true">
                <template #valueDisplay="{ value, onClose }">
                  <t-tag
                    v-for="(item, index) in value"
                    :key="item"
                    closable
                    @close="() => onClose(index)"
                  >
                    {{ item.name }}
                  </t-tag>
                </template>
              </t-tag-input>
            </t-form-item>
          </t-col>
          <t-col :span="6" v-if="!formData.su">
            <t-form-item label="角色" name="roles">
              <t-tag-input v-model="roles" clearable
                           placeholder="请输选择角色"
                           @clear="(e)=>{roles = [];e.e.stopPropagation();}"
                           @click="showSelectRole = true">
                <template #valueDisplay="{ value, onClose }">
                  <t-tag
                    v-for="(item, index) in value"
                    :key="item"
                    closable
                    :disabled="formData.su"
                    @close="() => onClose(index)"
                  >
                    {{ item.name }}
                  </t-tag>
                </template>
              </t-tag-input>
            </t-form-item>
          </t-col>

        </t-row>
      </div>
    </div>

    <div class="form-submit-container">
      <div class="form-submit-sub">
        <div class="form-submit-left">
          <t-button theme="primary" class="form-submit-confirm" type="submit"> 提交</t-button>
          <t-button type="reset" class="form-submit-cancel" theme="default" variant="base" :disabled="formData.su">
            重置
          </t-button>
        </div>
      </div>
    </div>
    <t-dialog v-model:visible="showSelectRole" header="选择角色" :footer="false" width="800">
      <role-select :mutilate="true"
                   v-if="showSelectRole"
                   @selected="onRoleSelected"
                   @cancel="showSelectRole = false"
                   :select-ids="roles.map((item:any) => item.id)"
                   user-type="SYSTEM_USER"></role-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectDep" header="选择部门" :footer="false" width="800">
      <dep-select v-if="showSelectDep"
                  :mutilate="true"
                  :select-ids="deps.map((item:any) => item.id)"
                  @selected="onDepSelected"
                  @cancel="showSelectDep = false"
      ></dep-select>
    </t-dialog>
    <t-dialog v-model:visible="showSelectPos" header="选择岗位" :footer="false" width="600">
      <position-select v-if="showSelectPos"
                       :mutilate="true"
                       :select-ids="pos.map((item:any) => item.id)"
                       @selected="onPosSelected"
                       @cancel="showSelectPos = false"
      ></position-select>
    </t-dialog>
  </t-form>
</template>

<script lang="ts">
export default {
  name: 'ManagerEdit',
};
</script>

<script setup lang="ts">
import type {AutoCompleteProps, SubmitContext, UploadProps} from 'tdesign-vue-next';
import {FormRule, MessagePlugin} from 'tdesign-vue-next';
import {computed, onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';

import ApiUser from '@/api/sys/ApiUser';
import Cropper from '@/components/upload/cropper.vue';
import router from '@/router';
import RoleSelect from "@/pages/sys/role/select.vue";
import PositionSelect from "@/pages/sys/position/select.vue";
import {encryptPasswd} from "@/utils/passwd-utils";
import {FileUseTypes} from "@/constants/file-user-types";
import DepSelect from "@/pages/sys/dept/select.vue";
import {closeOrBack} from "@/utils/url-utils";


// 自定义校验 start -------------------
const usernameValidator: any = (val: string) => {
  const re = /^[a-zA-z]\w{3,15}$/;
  if (re.test(val)) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '账号为字母、数字、下划线组成，字母开头，4-16位', type: 'error'};
};
const passwdValidator: any = (val: string) => {
  const re = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
  if (re.test(val)) {
    return {result: true, type: 'success'};
  }
  return {result: false, message: '密码需要同时含有数字和字母，且长度要在8-16位之间', type: 'error'};
};
const repasswd: any = (val: string) => {
  return new Promise((resolve) => {
    const timer = setTimeout(() => {
      resolve(formData.value.passwd === val);
      clearTimeout(timer);
    });
  });
};
// 自定义校验 end -------------------

// 定义变量 start -------------------
const showSelectRole = ref(false);
const showSelectDep = ref(false);
const showSelectPos = ref(false);

// 路由
const route = useRoute();
// 表单
const formData = ref({
  id: '',
  username: '',
  name: '',
  sex: 1,
  status: 1,
  su: false,
  passwd: '',
  phone: '',
  email: '',
  repasswd: '',
  avatarFileId: '',
  avatarFileUrl: '',
  posIds: [],
  depIds: [],
  roleIds: [],
});

const pos = ref([]);
const deps = ref([]);
const roles = ref([]);


const emailSuffix = ['@qq.com', '@163.com', '@gmail.com'];
const emailOptions = computed<AutoCompleteProps['options']>(() => {
  const emailPrefix = formData.value.email ? formData.value.email.split('@')[0] : "";
  if (!emailPrefix) return [];
  return emailSuffix.map((suffix) => emailPrefix + suffix);
});

// 表单校验
const FORM_RULES: Record<string, FormRule[]> = {
  name: [{required: true, message: '姓名不能为空', type: 'error'}],
  status: [{required: true, number: true, message: '状态不能为空', type: 'error'}],
  roleIds: [{required: true, message: '角色不能为空', type: 'error'}],
  username: [
    {required: true, message: '账号不能为空', type: 'error'},
    // 自定义校验规则：不同的值可以有不同的校验结果，不同的校验类型
    {validator: usernameValidator},
  ],
  passwd: [
    {required: true, message: '密码不能为空', type: 'error'},
    // 自定义校验规则：不同的值可以有不同的校验结果，不同的校验类型
    {validator: passwdValidator},
  ],
  repasswd: [
    {required: true, message: '请确认密码', type: 'error'},
    // 自定义校验规则：自定义异步校验规则
    {validator: repasswd, message: '两次密码不一致'},
  ],
};
// 定义变量 end -------------------

// 定义方法 start -------------------
// 部门选择回调
const onDepSelected = (res: any) => {
  showSelectDep.value = false
  deps.value = res
}

// 岗位选择回调
const onPosSelected = (res: any) => {
  showSelectPos.value = false
  pos.value = res
}

// 角色选择回调
const onRoleSelected = (res: any) => {
  showSelectRole.value = false
  roles.value = res
}
// 重置表单
const onReset = () => {
  formData.value.status = 1;
};
// 提交表单
const onSubmit = (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    formData.value.depIds = deps.value ? deps.value.map((item: any) => item.id) : [];
    formData.value.roleIds = roles.value ? roles.value.map((item: any) => item.id) : [];
    formData.value.posIds = pos.value ? pos.value.map((item: any) => item.id) : [];
    if (!formData.value.id) {
      ApiUser.key(formData.value.username, {
        success: (res: any) => {
          const form = JSON.parse(JSON.stringify(formData.value));
          form.passwd = encryptPasswd(form.username, form.passwd, res);
          ApiUser.edit({
            data: form,
            success: (res: any) => {
              MessagePlugin.success('已完成');
              closeOrBack(route, router)
            }
          });
        }
      });
    } else {
      ApiUser.edit({
        data: formData.value,
        success: (res: any) => {
          MessagePlugin.success('已完成');
          router.back();
        }
      });
    }
  }
};
const onAvatarUploaded = (res: any) => {
  formData.value.avatarFileId = res.id
}
// 定义方法 end -------------------

// vue生命周期
onMounted(() => {
  const {id, depId, depName, posId, posName} = route.query;
  if (depId) {
    deps.value = [{id: depId, name: depName}]
  }
  if (posId) {
    pos.value = [{id: posId, name: posName}]
  }

  if (id) {
    ApiUser.detail({
      id: id,
      success: (res: any) => {
        deps.value = res.deps ? res.deps : [];
        pos.value = res.pos ? res.pos : [];
        roles.value = res.roles ? res.roles : [];

        res.status = res.status ? res.status.code : 1;
        res.sex = res.sex ? res.sex.code : 1;

        delete res.deps;
        delete res.pos;
        delete res.roles;

        formData.value = res;
      }
    });
  }
});
</script>

<style lang="less" scoped></style>
