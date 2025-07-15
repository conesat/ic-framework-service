<template>
  <router-view :class="[mode]"/>
</template>
<script setup lang="ts">
import {computed} from 'vue';

import {useSettingStore} from '@/store';

const store = useSettingStore();

const mode = computed(() => {
  return store.displayMode;
});
</script>
<style lang="less">
#nprogress .bar {
  background: var(--td-brand-color) !important;
}

/*滚动条整体样式*/
::-webkit-scrollbar {
  /*高宽分别对应横竖滚动条的尺寸*/
  width: var(--td-size-3);
  height: var(--td-size-3);
}

/*滚动条里面小方块*/
::-webkit-scrollbar-thumb {
  border-radius: var(--td-size-3);
  -webkit-box-shadow: var(--td-shadow-1);
  background: var(--td-scrollbar-hover-color);
}

/*滚动条里面轨道*/
::-webkit-scrollbar-track {
  -webkit-box-shadow: var(--td-shadow-1);
  border-radius: var(--td-size-3);
  background: var(--td-scrollbar-color);
}


</style>
