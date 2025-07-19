<template>
  <l-header v-if="settingStore.state.showHeader" :show-logo="settingStore.showHeaderLogo"
    :theme="settingStore.displayMode" :layout="settingStore.state.layout" :is-fixed="settingStore.state.isHeaderFixed"
    :menu="headerMenu as MenuRoute[]" :is-compact="settingStore.state.isSidebarCompact" />
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { computed } from 'vue';

import { usePermissionStore, useSettingStore } from '@/store';
import type { MenuRoute } from '@/types/interface';
import LHeader from './Header.vue';

const permissionStore = usePermissionStore();
const settingStore = useSettingStore();
const { routers: menuRouters } = storeToRefs(permissionStore);
const headerMenu = computed(() => {
  if (settingStore.state.layout === 'mix') {
    if (settingStore.state.splitMenu) {
      return menuRouters.value.map((menu) => ({
        ...menu,
        children: [] as MenuRoute[],
      }));
    }
    return [];
  }
  return menuRouters.value as MenuRoute[];
});
</script>
