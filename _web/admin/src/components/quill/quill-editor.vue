<template>
  <div ref="quillRef" class="quill-editor">
    <div ref="editorRef"></div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, provide, ref, watch} from 'vue';
import Quill from 'quill';
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

const editorRef = ref(null);
const quillRef = ref(null);
let isInternalUpdate = false;
let quill: any;

const props = defineProps<{
  modelValue: string;
}>();

const emit = defineEmits(['update:modelValue']);

onMounted(() => {
  quill = new Quill(editorRef.value, {
    theme: 'snow'
  });

  quill.on('text-change', (delta: any, oldDelta: any, source: any) => {
    if (source === 'user') {
      isInternalUpdate = true;
      emit('update:modelValue', quill.root.innerHTML);
    }
  });

  // 初始化内容
  quill.clipboard.dangerouslyPasteHTML(props.modelValue);
});

watch(
  () => props.modelValue,
  (newVal) => {
    if (!isInternalUpdate) {
      if (quill && quill.clipboard) {
        quill.clipboard.dangerouslyPasteHTML(newVal);
      }
    }
    isInternalUpdate = false;
  },
  {immediate: true}
);

provide('quill', quill);
</script>

<style scoped lang="less">
.quill-editor {
  min-height: 140px;
}
</style>
