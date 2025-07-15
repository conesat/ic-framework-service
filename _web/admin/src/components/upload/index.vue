<template>
  <t-upload
    ref="uploadRef"
    v-model="files"
    :files="modelValue"
    :accept="accept"
    :data="data"
    :disabled="disabled"
    :draggable="draggable"
    :defaultFiles="defaultFiles"
    :multiple="multiple"
    :tips="tips"
    :request-method="requestMethod"
    :on-fail="handleRequestFail"
  ></t-upload>
</template>
<script setup lang="ts">
import {uploadFile} from "@/components/upload/upload";
import {computed, ref} from "vue";
import {RadioGroupProps, UploadFile, UploadInstanceFunctions, UploadProps} from "tdesign-vue-next";
import {FileUseType} from "@/constants/file-user-types";

const emit = defineEmits(['clear', 'uploaded']);
const props = defineProps({
  modelValue: {
    type: Array,
    default: ''
  },
  defaultFiles: Array,
  /**
   * 文件用途
   */
  useType: {
    type: FileUseType,
    required: true
  },
  theme: String,
  accept: String,
  tips: String,
  data: Object,
  disabled: Boolean,
  draggable: Boolean,
  multiple: Boolean,
  name: Boolean,
  format: Function
})
const onUpload = ref(false)
const files = ref<UploadProps['value']>([]);

const uploadRef = ref<UploadInstanceFunctions>();
const uploadMethod = ref<RadioGroupProps['value']>('requestSuccessMethod');

// file 为等待上传的文件信息，用于提供给上传接口。file.raw 表示原始文件
const requestSuccessMethod: UploadProps['requestMethod'] = (file: UploadFile) => {
  console.log(uploadRef.value);
  return new Promise((resolve) => {
    uploadFile(file.raw, props.useType, (res: number) => {
      uploadRef.value.uploadFilePercent({
        file,
        percent: res,
      });
    }, (res: any) => {
      uploadRef.value.uploadFilePercent({
        file,
        percent: 100,
      });
      resolve({
        status: 'success',
        response: {
          url: 'https://tdesign.gtimg.com/site/avatar.jpg',
        },
      });
      onUpload.value = false
      emit('uploaded', res)
    }, () => {
      onUpload.value = false
    })
  });
};
const requestFailMethod: UploadProps['requestMethod'] = (file) => {
  return new Promise((resolve) => {
    resolve({
      status: 'fail',
      error: '上传失败，请检查文件是否符合规范',
      response: {},
    });
  });
};
const handleRequestFail: UploadProps['onFail'] = (e) => {
  console.log(e);
};
const requestMethod = computed<UploadProps['requestMethod']>(() =>
  uploadMethod.value === 'requestSuccessMethod' ? requestSuccessMethod : requestFailMethod,
);

</script>

<style scoped lang="less">

</style>
