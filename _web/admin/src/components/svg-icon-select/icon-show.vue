<template>
  <div class="icon">
    <div v-if="type === 1 && name">
      <component :is="svgIcons['/src/assets/svg-icons/' + name]"
                 :style="{width: `${size}px`, height: `${size}px`}" class="t-icon"></component>
    </div>
    <t-icon v-else-if="type === 2 &&name" :style="{fontSize: `${size}px`}" :name="name"/>
  </div>
</template>
<script setup lang="ts">
const emit = defineEmits(['selected']);
import {computed, ref} from 'vue';

const props = defineProps({
  type: {
    type: Number,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  size: {
    type: Number,
    default: 24
  }
});
const modules = import.meta.glob('@/assets/svg-icons/*.svg', {eager: true});
const svgIcons = ref(modules)


</script>
<style lang="less" scoped>
.t-icon {
  color: var(--td-text-color-primary);
}
</style>
