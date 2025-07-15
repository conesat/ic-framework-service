<template>
  <div>
    <h3 style="text-align: center;color: var(--td-text-color-primary)">IC 图标</h3>
    <div class="grid">
      <div class="cell" v-for="(item, key) in svgIcons" :key="key">
        <div class="icon-parent" @click="selected(key.replace('/src/assets/svg-icons/', ''), 1)">
          <component :is="item" class="t-icon"></component>
        </div>
      </div>
    </div>
    <h3 style="text-align: center;color: var(--td-text-color-primary)">TDesign 图标</h3>
    <div class="grid">
      <div class="cell" v-for="(item, index) in tdIcons" :key="index">
        <div class="icon-parent" @click="selected(item.stem, 2)">
          <t-icon :name="item.stem"/>
          <div>{{item.stem}}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const emit = defineEmits(['selected']);

import {computed, ref} from 'vue';
import {manifest} from 'tdesign-icons-vue-next'; // 获取全部图标的列表

const props = defineProps();

// 获取全部图标的列表
const tdIcons = ref(manifest);

const modules = import.meta.glob('@/assets/svg-icons/*.svg', {eager: true});
const svgIcons = ref(modules)

const selected = (name: string, type: number) => {
  emit('selected', {name, type});
}
</script>
<style lang="less" scoped>
.grid {
  margin: 20px 0;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-gap: 0; /* 网格间隙 */

  .cell {
    text-align: center; /* 文本居中 */
    place-items: center;

    .icon-parent:hover {
      background-color: var(--td-bg-color-component-active);
    }

    .icon-parent {
      width: 100%;
      display: inline-block;
      padding: 16px;
      border-radius: var(--td-border-radius-default);
      cursor: pointer;
      transition: background-color 0.1s linear;

      .t-icon {
        width: 30px;
        height: 30px;
        color: var(--td-text-color-primary);
      }
    }
  }
}

</style>
