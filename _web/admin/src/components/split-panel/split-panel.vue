<template>
  <div class="split-panel" ref="splitPanelRef">
    <div class="left-panel" :style="{ width: leftWidth + 'px' }">
      <slot name="left"></slot>
    </div>
    <div class="split-line" ref="splitLineRef" @mousedown="handleMouseDown" :class="{ 'on-move': onMove }"></div>
    <div class="right-panel" style="flex: 1">
      <slot name="right"></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useEventListener } from '@vueuse/core'

const props = defineProps({
  leftWidth: {
    required: true,
    type: Number,
    default: 200
  },
  minLeft: {
    required: true,
    type: Number,
    default: 200
  },
  maxLeft: {
    required: true,
    type: Number,
    default: 200
  },
})

const splitPanelRef = ref(null)
const splitLineRef = ref(null)
const onMove = ref(false)

// 计算左右面板的 flex 值
const leftWidth = ref(0)

// 鼠标按下时记录初始位置
const handleMouseDown = (event: MouseEvent) => {
  document.body.style.userSelect = 'none' // 防止选中文本
  onMove.value = true
  // 监听鼠标移动事件
  useEventListener('mousemove', (event: MouseEvent) => {
    if (!onMove.value) {
      return
    }
    const dew = event.clientX - splitPanelRef.value.offsetLeft
    if (props.minLeft && dew < props.minLeft) {
      leftWidth.value = props.minLeft;
      return;
    }
    if (props.maxLeft && dew > props.maxLeft) {
      leftWidth.value = props.maxLeft;
      return;
    }
    leftWidth.value = dew
  })

  // 监听鼠标抬起事件
  useEventListener('mouseup', () => {
    document.body.style.userSelect = '' // 恢复文本选择
    onMove.value = false
    removeEventListener('mouseup', null)
  })
}

onMounted(() => {
  // 添加全局鼠标事件监听器
  leftWidth.value = props.leftWidth
})

onUnmounted(() => {
  // 移除全局鼠标事件监听器
})
</script>

<style scoped>
.split-panel {
  display: flex;
  height: 100%;
  width: 100%;
}

.left-panel,
.right-panel {
  width: 50%;
  overflow-y: auto;
}

.on-move.split-line,
.split-line:hover {
  opacity: 1;
  width: 4px;
  margin: 0 5px;
  background-color: var(--td-bg-color-component-active);
}

.split-line {
  border-radius: 10px;
  opacity: 0.5;
  margin: 0 6px;
  width: 2px;
  transition: all 0.1s linear;
  background-color: var(--td-bg-color-component-active);
  cursor: ew-resize;
}
</style>
