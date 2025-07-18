import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { TRouterInfo, TTabRouterType } from '@/types/interface';
import { RouteLocationNormalizedLoaded, Router } from 'vue-router';

const homeRoute: Array<TRouterInfo> = [
  {
    path: '/dashboard/base',
    routeIdx: 0,
    title: '仪表盘',
    name: 'DashboardBase',
    isHome: true,
  },
];

const defaultState = {
  tabRouterList: homeRoute,
  isRefreshing: false,
};

const ignoreCacheRoutes = ['login'];

export const useTabsRouterStore = defineStore('tabsRouter', () => {
  // state
  const tabRouterList = ref<TRouterInfo[]>([...homeRoute]);
  const isRefreshing = ref(false);

  // getters
  const tabRouters = computed(() => tabRouterList.value);
  const refreshing = computed(() => isRefreshing.value);

  // actions
  function toggleTabRouterAlive(routeIdx: number) {
    isRefreshing.value = !isRefreshing.value;
    tabRouterList.value[routeIdx].isAlive = !tabRouterList.value[routeIdx].isAlive;
  }

  function appendTabRouterList(newRoute: TRouterInfo) {
    const needAlive = !ignoreCacheRoutes.includes(newRoute.name as string) && newRoute.meta?.keepAlive !== false;
    if (!tabRouterList.value.find((route: TRouterInfo) => route.path === newRoute.path)) {
      tabRouterList.value = tabRouterList.value.concat({ ...newRoute, isAlive: needAlive });
    }
  }

  function subtractCurrentTabRouter(newRoute: TRouterInfo) {
    const { routeIdx } = newRoute;
    tabRouterList.value = tabRouterList.value.slice(0, routeIdx).concat(tabRouterList.value.slice(routeIdx + 1));
  }

  function closeTab(route: RouteLocationNormalizedLoaded, router: Router) {
    const options = {
      index: 0,
      value: route.path,
    };
    for (let i = tabRouterList.value.length - 1; i > 0; i--) {
      const tab = tabRouterList.value[i];
      if (tab.path === route.path) {
        options.index = i;
        break;
      }
    }
    const nextRouter = tabRouterList.value[options.index - 1] || tabRouterList.value[options.index + 1];
    subtractCurrentTabRouter({ path: options.value as string, routeIdx: options.index } as TRouterInfo);
    if ((options.value as string) === route.path) router.push({ path: nextRouter.path, query: nextRouter.query });
  }

  function subtractTabRouterBehind(newRoute: TRouterInfo) {
    const { routeIdx } = newRoute;
    const homeIdx: number = tabRouterList.value.findIndex((route: TRouterInfo) => route.isHome);
    let list: Array<TRouterInfo> = tabRouterList.value.slice(0, routeIdx + 1);
    if (routeIdx < homeIdx) {
      list = list.concat(homeRoute);
    }
    tabRouterList.value = list;
  }

  function subtractTabRouterAhead(newRoute: TRouterInfo) {
    const { routeIdx } = newRoute;
    const homeIdx: number = tabRouterList.value.findIndex((route: TRouterInfo) => route.isHome);
    let list: Array<TRouterInfo> = tabRouterList.value.slice(routeIdx);
    if (routeIdx > homeIdx) {
      list = homeRoute.concat(list);
    }
    tabRouterList.value = list;
  }

  function subtractTabRouterOther(newRoute: TRouterInfo) {
    const { routeIdx } = newRoute;
    const homeIdx: number = tabRouterList.value.findIndex((route: TRouterInfo) => route.isHome);
    tabRouterList.value = routeIdx === homeIdx ? homeRoute : homeRoute.concat([tabRouterList.value?.[routeIdx]]);
  }

  function removeTabRouterList() {
    tabRouterList.value = [];
  }

  function initTabRouterList(newRoutes: TRouterInfo[]) {
    newRoutes?.forEach((route: TRouterInfo) => appendTabRouterList(route));
  }

  return {
    tabRouterList,
    isRefreshing,
    tabRouters,
    refreshing,
    toggleTabRouterAlive,
    appendTabRouterList,
    subtractCurrentTabRouter,
    closeTab,
    subtractTabRouterBehind,
    subtractTabRouterAhead,
    subtractTabRouterOther,
    removeTabRouterList,
    initTabRouterList,
  };
}, {
  persist: true,
});
