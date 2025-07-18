<template>
  <div>
    <template v-for="item in list" :key="item.path">
      <template v-if="!item.children || !item.children.length || item.meta?.single">
        <t-menu-item v-if="getHref(item)" :name="item.path" :value="getPath(item)" @click="openHref(getHref(item)[0])">
          <template #icon>
            <component :is="menuIcon(item)" class="t-icon"></component>
          </template>
          {{ item.title }}
        </t-menu-item>
        <t-menu-item v-else :name="item.path" :value="getPath(item)" :to="item.path">
          <template #icon>
            <component :is="menuIcon(item)" class="t-icon"></component>
          </template>
          {{ item.title }}
        </t-menu-item>
      </template>
      <t-submenu v-else :name="item.path" :value="item.path" :title="item.title">
        <template #icon>
          <component :is="menuIcon(item)" class="t-icon"></component>
        </template>
        <menu-content v-if="item.children" :nav-data="item.children" />
      </t-submenu>
    </template>
  </div>
</template>
<script setup lang="tsx">
import type { PropType } from 'vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { h } from 'vue';

import { getActive } from '@/router';
import type { MenuRoute } from '@/types/interface';

type ListItemType = MenuRoute & { icon?: string, svgIcon?: string };


const props = defineProps({
  navData: {
    type: Array as PropType<MenuRoute[]>,
    default: (): MenuRoute[] => [],
  },
});

const route = useRoute();
const active = computed(() => getActive(route));

const list = computed(() => {
  const { navData } = props;
  return getMenuList(navData);
});
const menuIcon = (item: ListItemType) => {
  if (item.svgIcon) {
    let svg = svgIcons.get(item.svgIcon);
    return svg.default;
  }
  if (typeof item.icon === 'string') return h('t-icon', { name: item.icon });
  return item.icon;
};

const getMenuList = (list: MenuRoute[], basePath?: string): ListItemType[] => {
  if (!list || list.length === 0) {
    return [];
  }
  // 如果meta中有orderNo则按照从小到大排序
  list.sort((a, b) => {
    return (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0);
  });
  return list
    .map((item) => {
      const path = basePath && !item.path.includes(basePath) ? `${basePath}${!basePath.endsWith("/") && !item.path.startsWith("/") ? '/' : ''}${item.path}` : item.path;
      return {
        path,
        title: item.meta?.title,
        icon: item.meta?.icon,
        svgIcon: item.meta?.svgIcon,
        children: getMenuList(item.children, path),
        meta: item.meta,
        redirect: item.redirect,
      };
    })
    .filter((item) => item.meta && item.meta.hidden !== true);
};

const getHref = (item: MenuRoute) => {
  const { frameSrc, frameBlank } = item.meta;
  if (frameSrc && frameBlank) {
    return frameSrc.match(/(http|https):\/\/([\w.]+\/?)\S*/);
  }
  return null;
};

const getPath = (item: ListItemType) => {
  const activeLevel = active.value.split('/').length;
  const pathLevel = item.path.split('/').length;
  if (activeLevel > pathLevel && active.value.startsWith(item.path)) {
    return active.value;
  }

  if (active.value === item.path) {
    return active.value;
  }

  return item.meta?.single ? item.redirect : item.path;
};

const openHref = (url: string) => {
  window.open(url);
};
const getSvg: any = () => {
  let m = new Map()
  const modules = import.meta.glob('@/assets/svg-icons/*.svg', { eager: true });
  Object.keys(modules).forEach(key => {
    m.set(key.replace('/src/assets/svg-icons/', ''), modules[key])
  });
  return m;
};

const svgIcons = getSvg()
</script>

<style lang="less" scoped>
:deep(t-icon) {
  width: 20px;
  height: 20px;

  path {
    fill: var(--td-text-color-secondary);
  }
}
</style>
