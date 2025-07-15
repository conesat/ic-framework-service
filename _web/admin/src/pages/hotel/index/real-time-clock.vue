<template>
  <div>
    {{ formattedTime }}
  </div>
</template>

<script lang="ts">
import {ref, onMounted, onBeforeUnmount} from 'vue';

export default {
  name: 'RealTimeClock',
  setup() {
    const currentTime = ref(new Date());
    const formattedTime = ref('');

    const updateTime = () => {
      currentTime.value = new Date();
      formattedTime.value = currentTime.value.toLocaleTimeString();
    };

    onMounted(() => {
      updateTime();
      const intervalId = setInterval(updateTime, 1000);

      // 清理定时器
      onBeforeUnmount(() => {
        clearInterval(intervalId);
      });
    });

    return {
      formattedTime
    };
  }
};
</script>
