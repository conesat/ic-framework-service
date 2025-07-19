import { useSettingStore, useTabsRouterStore } from "@/store";
import { RouteLocationNormalizedLoaded, Router, useRoute, useRouter } from "vue-router";

const tabsRouterStore = useTabsRouterStore();
const settingStore = useSettingStore();

export function getAllParams(): URLSearchParams {
  const params = new URLSearchParams();
  let ps = window.location.search.substring(1).split('&');
  for (const psKey in ps) {
    const [key, value] = ps[psKey].split('=');
    params.set(key, decodeURIComponent(value));
  }
  return params;
}

export function getParams(searchKey: String): String {
  let ps = window.location.search.substring(1).split('&');
  for (const psKey in ps) {
    const [key, value] = ps[psKey].split('=');
    if (key === searchKey) {
      return decodeURIComponent(value);
    }
  }
  return "";
}

export function closeOrBack(route: RouteLocationNormalizedLoaded, router: Router) {
  if (getParams("autoClose") === "true") {
    if (settingStore.state.isUseTabsRouter) {
      tabsRouterStore.closeTab(route, router)
    } else if (window.history.length > 1) {
      router.go(-1);
    } else {
      window.close()
    }
  } else {
    router.go(-1);
  }
}
