<script setup lang="ts">
const props = defineProps({
  order: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <div
    class="blink-border item">
    <div class="item-line">
      <div class="title">
        客房编号
      </div>
      <div class="content">{{ order.roomNo }}</div>
    </div>
    <div class="item-line">
      <div class="title">
        订单内容
      </div>
      <div class="content">{{ order.targetContent }}</div>
    </div>
    <div class="item-line">
      <div class="title">
        服务人员
      </div>
      <div class="content">{{ order.serverUserName ?? '无人处理' }}</div>
    </div>
    <div class="item-line">
      <div class="title">
        期望送达时间
      </div>
      <div class="content">{{ order.doTime ? order.doTime.substring(5, 16) : '随时' }}</div>
    </div>
  </div>
</template>

<style scoped lang="less">
.item {
  cursor: pointer;
  background: rgba(0, 0, 0, 0.2);
  padding: 10px 20px;
  border-radius: 4px;
  border: 1px rgba(255, 255, 255, 0.2) solid;
  white-space: nowrap;

  .item-line:first-child {
    margin-top: 0;
  }

  .item-line {
    margin-top: 4px;
    display: flex;

    .title {
      word-break: break-all;
      color: #999;
      max-width: 80px;
    }

    .content {
      white-space: break-spaces;
      word-break: break-all;
      flex: 1;
      width: 80px;
      text-align: right;
      color: #fff;
      margin-left: 10px;
      max-width: 100px;
    }
  }
}

@keyframes blink-border {
  0%, 100% {
    border-color: transparent;
    box-shadow: none;
  }
  50% {
    border-color: rgba(156, 99, 255, 0.4);
    box-shadow: 0 0 4px rgba(255, 224, 99, 0.3);
  }
}

.blink-border {
  animation: blink-border 1.5s infinite;
}
</style>
