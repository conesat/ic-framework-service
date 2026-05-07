<template>
  <t-tabs
    drag-sort
    theme="card"
    :class="`${prefix}-layout-tabs-nav`"
    :value="$route.path"
    @change="handleChangeCurrentTab"
    @remove="handleRemove"
    @drag-sort="handleDragend"
  >
    <t-tab-panel
      v-for="(routeItem, index) in tabRouters"
      :key="`${routeItem.path}_${index}`"
      :value="routeItem.path"
      :removable="!routeItem.isHome"
      :draggable="!routeItem.isHome"
    >
      <template #label>
        <t-dropdown
          trigger="context-menu"
          :min-column-width="128"
          :popup-props="{
            overlayClassName: 'route-tabs-dropdown',
            onVisibleChange: (visible: boolean, ctx: PopupVisibleChangeContext) =>
              handleTabMenuClick(visible, ctx, routeItem.path),
            visible: activeTabPath === routeItem.path,
          }"
        >
          <template v-if="!routeItem.isHome">
            {{ routeItem.title }}
          </template>
          <t-icon v-else name="home" />
          <template #dropdown>
            <t-dropdown-menu>
              <t-dropdown-item @click="() => handleRefresh(routeItem, index)">
                <t-icon name="refresh" />
                刷新
              </t-dropdown-item>
              <t-dropdown-item v-if="index > 1" @click="() => handleCloseAhead(routeItem.path, index)">
                <t-icon name="arrow-left" />
                关闭左侧
              </t-dropdown-item>
              <t-dropdown-item v-if="index < tabRouters.length - 1" @click="() => handleCloseBehind(routeItem.path, index)">
                <t-icon name="arrow-right" />
                关闭右侧
              </t-dropdown-item>
              <t-dropdown-item v-if="tabRouters.length > 2" @click="() => handleCloseOther(routeItem.path, index)">
                <t-icon name="close-circle" />
                关闭其它
              </t-dropdown-item>
            </t-dropdown-menu>
          </template>
        </t-dropdown>
      </template>
    </t-tab-panel>
  </t-tabs>
</template>

<script setup lang="ts">
import type { PopupVisibleChangeContext } from 'tdesign-vue-next';
import { computed, nextTick, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { prefix } from '@/config/global';
import { useTabsRouterStore } from '@/store';
import type { TRouterInfo, TTabRemoveOptions } from '@/types/interface';

const route = useRoute();
const router = useRouter();

const tabsRouterStore = useTabsRouterStore();
const tabRouters = computed(() => tabsRouterStore.tabRouters.filter((item) => item.isAlive || item.isHome));
const activeTabPath = ref('');

const handleChangeCurrentTab = (path: any) => {
  const { tabRouters } = tabsRouterStore;
  const currentRoute = tabRouters.find((item) => item.path === path);
  router.push({ path, query: currentRoute.query });
};

const handleRemove = (options: TTabRemoveOptions) => {
  const { tabRouters } = tabsRouterStore;
  const nextRouter = tabRouters[options.index + 1] || tabRouters[options.index - 1];

  tabsRouterStore.subtractCurrentTabRouter({ path: options.value as string, routeIdx: options.index });
  if ((options.value as string) === route.path) router.push({ path: nextRouter.path, query: nextRouter.query });
};

const handleRefresh = (currentRoute: TRouterInfo, routeIdx: number) => {
  tabsRouterStore.toggleTabRouterAlive(routeIdx);
  nextTick(() => {
    tabsRouterStore.toggleTabRouterAlive(routeIdx);
    router.replace({ path: currentRoute.path, query: currentRoute.query });
  });
  activeTabPath.value = null;
};

const handleCloseAhead = (path: string, routeIdx: number) => {
  tabsRouterStore.subtractTabRouterAhead({ path, routeIdx });
  handleOperationEffect('ahead', routeIdx);
};

const handleCloseBehind = (path: string, routeIdx: number) => {
  tabsRouterStore.subtractTabRouterBehind({ path, routeIdx });
  handleOperationEffect('behind', routeIdx);
};

const handleCloseOther = (path: string, routeIdx: number) => {
  tabsRouterStore.subtractTabRouterOther({ path, routeIdx });
  handleOperationEffect('other', routeIdx);
};

const handleOperationEffect = (type: 'other' | 'ahead' | 'behind', routeIndex: number) => {
  const currentPath = router.currentRoute.value.path;
  const { tabRouters } = tabsRouterStore;

  const currentIdx = tabRouters.findIndex((item) => item.path === currentPath);
  const needRefreshRouter =
    (type === 'other' && currentIdx !== routeIndex) ||
    (type === 'ahead' && currentIdx < routeIndex) ||
    (type === 'behind' && currentIdx === -1);
  if (needRefreshRouter) {
    const nextRouteIdx = type === 'behind' ? tabRouters.length - 1 : 1;
    const nextRouter = tabRouters[nextRouteIdx];
    router.push({ path: nextRouter.path, query: nextRouter.query });
  }

  activeTabPath.value = null;
};

const handleTabMenuClick = (visible: boolean, ctx: PopupVisibleChangeContext, path: string) => {
  if (ctx.trigger === 'document') activeTabPath.value = null;
  if (visible) activeTabPath.value = path;
};

const handleDragend = (options: { currentIndex: number; targetIndex: number }) => {
  const { tabRouters } = tabsRouterStore;

  [tabRouters[options.currentIndex], tabRouters[options.targetIndex]] = [
    tabRouters[options.targetIndex],
    tabRouters[options.currentIndex],
  ];
};
</script>
