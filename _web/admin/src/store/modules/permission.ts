import { defineStore } from 'pinia';
import { ref } from 'vue';
import { RouteRecordRaw } from 'vue-router';

import { RouteItem } from '@/api/model/permissionModel';
import { getMenuList } from '@/api/permission';
import router, { fixedRouterList, homepageRouterList } from '@/router';
import { transformObjectToRoute } from '@/utils/route';

export const usePermissionStore = defineStore('permission', () => {
  const whiteListRouters = ref(['/login']);
  const routers = ref<Array<RouteRecordRaw>>([]);
  const removeRoutes = ref<Array<RouteRecordRaw>>([]);
  const asyncRoutes = ref<Array<RouteRecordRaw>>([]);

  async function initRoutes() {
    const accessedRouters = asyncRoutes.value;
    routers.value = [...homepageRouterList, ...accessedRouters, ...fixedRouterList];
  }

  async function buildAsyncRoutes() {
    try {
      const asyncRoutesList: Array<RouteItem> = await getMenuList();
      asyncRoutes.value = transformObjectToRoute(asyncRoutesList);
      await initRoutes();
      return asyncRoutes.value;
    } catch (error) {
      throw new Error("Can't build routes");
    }
  }

  async function restoreRoutes() {
    asyncRoutes.value.forEach((item) => {
      if (item.name) {
        router.removeRoute(item.name);
      }
    });
    asyncRoutes.value = [];
  }

  return {
    whiteListRouters,
    routers,
    removeRoutes,
    asyncRoutes,
    initRoutes,
    buildAsyncRoutes,
    restoreRoutes,
  };
});
