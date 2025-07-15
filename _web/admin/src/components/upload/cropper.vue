<template>
  <div style="overflow: hidden"
       :style="{ width: width+'px', height: height+'px'}">
    <t-button variant="outline" v-if="button" @click="pickerPic" :disabled="onUpload" :loading="onUpload">
      <template #icon>
        <cloud-upload-icon/>
      </template>
      <div style="margin-left: 10px">上传文件</div>
    </t-button>
    <t-image-viewer :images="[imgData ? imgData : url]" v-else>
      <template #trigger="{ open }">
        <div class="imager-parent">
          <t-image
            @click="pickerPic"
            :src="imgData ? imgData : url"
            :style="{ width: width+'px', height: height+'px',cursor:disabled?'no-drop':'pointer', 'background-color':backgroundColor}"
            :error="renderCustomIcon"
            fit="cover"
          />
          <div class="func-btn" v-if="imgData || url">
            <BrowseIcon class="icon" size="1.4em" @click="open"/>
            <DeleteIcon v-if="!onUpload && showDelete" class="icon" size="1.4em" style="color: var(--td-error-color)"
                        @click="deleteImg"/>
            <ImageEditIcon v-if="!onUpload && !showDelete" class="icon" size="1.4em"
                           @click="pickerPic"/>
          </div>
          <div v-if="onUpload" class="upload-mask" :style="{height:Math.max(0, 100-uploadProcess)+'%'}"></div>
        </div>
      </template>
    </t-image-viewer>


    <input :disabled="disabled" v-show="false" type="file" ref="fileInput" @change="selectFile($event)"
           :accept="accept"/>
    <t-dialog :visible.sync="showCropperDialog"
              :width="dialogWidth+50+'px'"
              :height="dialogHeight+'px'"
              header="裁剪"
              @cancel="cancel" @confirm="onCropper">
      <div :style="{height:dialogHeight+'px',width:dialogWidth+'px'}" style="margin: 0 auto">
        <vue-cropper
          ref="cropper"
          :img="img"
          :autoCrop="true"
          :centerBox="centerBox"
          :fixed="true"
          :fixedNumber="[coverWidth,coverHeight]"
          :infoTrue="infoTrue"
          :maxImgSize="Math.min(dialogWidth, dialogHeight)"
          :autoCropWidth="coverWidth + 'px'"
          :autoCropHeight="coverHeight + 'px'"
          :outputSize="1"
          outputType="png"
        ></vue-cropper>
      </div>
    </t-dialog>

  </div>

</template>
<script setup lang="tsx">
import {ImageErrorIcon, ImageIcon, BrowseIcon, DeleteIcon, ImageEditIcon, CloudUploadIcon} from 'tdesign-icons-vue-next';
import {ImageProps} from 'tdesign-vue-next';
import 'vue-cropper/dist/index.css'
import {VueCropper} from 'vue-cropper';
import {ref} from "vue";
import {uploadFile} from "@/components/upload/upload";
import {FileUseType} from "@/constants/file-user-types";

const emit = defineEmits(['clear', 'uploaded']);

const renderCustomIcon: ImageProps['error'] = () => {
  if (!imgData.value) {
    return <ImageIcon size="24"/>;
  }
  return <ImageErrorIcon size="24"/>;
};

const cropper = ref(null)
const props = defineProps({
  /**
   * 是否以按钮形式展示
   */
  button: {
    type: Boolean,
    default: false
  },
  /**
   * 裁剪图片高度
   */
  coverHeight: {
    type: Number,
    default: 500
  },
  /**
   * 裁剪图片宽度
   */
  coverWidth: {
    type: Number,
    default: 500
  },
  /**
   *弹窗高度
   */
  dialogHeight: {
    type: Number,
    default: 500
  },
  /**
   * 弹窗宽度
   */
  dialogWidth: {
    type: Number,
    default: 500
  },
  /**
   *组件高度
   */
  height: {
    type: Number,
    default: 80
  },
  /**
   * 组件宽度
   */
  width: {
    type: Number,
    default: 80
  },
  /**
   * 接收图片类型
   */
  accept: {
    type: String,
    default: 'image/*'
  },
  /**
   * 预览图片
   */
  url: {
    type: String
  },
  disabled: {
    type: Boolean
  },
  backgroundColor: {
    type: String
  },
  /**
   * 图片用途
   */
  useType: {
    type: FileUseType,
    required: true
  },
  /**
   * 是否展示删除按钮
   */
  showDelete: {
    type: Boolean,
    default: true
  },
  /**
   * 限制最大只能图片的大小
   */
  centerBox: {
    type: Boolean,
    default: false
  },
  infoTrue: Boolean,
})
const selectFileName = ref('')
const uploadProcess = ref(0)
const onUpload = ref(false)
const fileInput = ref(null)
const imgData = ref(null)
const img = ref('')
const showCropperDialog = ref(false)
const selectFile = (e: any) => {
  let reader = new FileReader()
  reader.onload = (e) => {
    if (typeof e.target.result === 'object') {
      img.value = window.URL.createObjectURL(new Blob([e.target.result]))
    } else {
      img.value = e.target.result
    }
    showCropperDialog.value = true
  }
  selectFileName.value = e.target.files[0].name
  //转化为base64
  reader.readAsDataURL(e.target.files[0])
}

const pickerPic = () => {
  fileInput.value.click()
}

const cancel = () => {
  showCropperDialog.value = false
}

const deleteImg = () => {
  fileInput.value.value = ''
  imgData.value = null
  emit('clear');
}

const onCropper = () => {
  if (!props.useType) {
    console.log("未指定图片用途")
    return
  }
  cropper.value.getCropBlob((data: Blob) => {
    onUpload.value = true
    showCropperDialog.value = false
    imgData.value = new File([data], selectFileName.value, {lastModified: new Date().getDate()});
    uploadFile(imgData.value, props.useType, (res: number) => {
      uploadProcess.value = res
    }, (res: any) => {
      onUpload.value = false
      emit('uploaded', res)
    }, () => {
      onUpload.value = false
    })
  })
}
</script>

<style scoped lang="less">
.imager-parent:hover {
  .func-btn {
    display: flex;
  }
}

.imager-parent {
  position: relative;

  .func-btn {
    background-color: var(--td-mask-disabled);
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
    display: none;
    align-items: center;
    padding: 0 10px;

    .icon {
      color: var(--td-text-color-primary);
      cursor: pointer;
      flex: 1;
    }
  }

  .upload-mask {
    transition: height 0.1s linear;
    background-color: var(--td-mask-active);
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 0;
    z-index: 2;
  }
}
</style>
