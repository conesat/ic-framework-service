<template>
  <div :class="sideNavCls">
    <t-menu :class="menuCls" :theme="theme" :value="active" :data="active" :collapsed="collapsed"
      :default-expanded="defaultExpanded">
      <template #logo>
        <span v-if="showLogo" :class="`${prefix}-side-nav-logo-wrapper`" @click="goHome">
          <component :is="getLogo()" :class="`${prefix}-side-nav-logo-${collapsed ? 't' : 'tdesign'}-logo`" />
        </span>
        <t-button v-if="!settingStore.state.isSidebarCompact" size="small" theme="default" shape="square"
          @click="changeCollapsed">
          <t-icon :name="collapsed ? 'chevron-right' : 'chevron-left'" size="16px" />
        </t-button>
      </template>
      <menu-content :nav-data="menu" />
      <template #operations>
        <div class="side-nav-operations">
          <t-dropdown :min-column-width="120" trigger="click" placement="right-bottom">
            <template #dropdown>
              <t-dropdown-menu>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleNav('/mine/mine-index')">
                  <t-icon name="user-circle"></t-icon>个人中心
                </t-dropdown-item>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleNav('/mine/edit-password')">
                  <t-icon name="user-password"></t-icon>修改密码
                </t-dropdown-item>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleLogout">
                  <t-icon name="poweroff"></t-icon>退出登录
                </t-dropdown-item>
              </t-dropdown-menu>
            </template>
            <div class="user-info-container" :class="{ 'collapsed': collapsed }">
              <t-avatar size="32px" class="user-avatar"
                :image="user.userInfo.avatarFileUrl ? user.userInfo.avatarFileUrl : ''">
                {{ (user.userInfo.name || '').substring(0, 1) }}
              </t-avatar>
              <div v-if="!collapsed" class="user-details">
                <div class="user-name">{{ user.userInfo.name || 'Admin' }}</div>
                <div class="user-role">{{ user.userInfo.roles?.[0] || '管理员' }}</div>
              </div>
              <t-icon v-if="!collapsed" name="more" class="more-icon" />
            </div>
          </t-dropdown>
          <div class="version-container"> {{ !collapsed ? 'IC Framework' : '' }} {{ pgk.version }} </div>
        </div>
      </template>
    </t-menu>
    <div :class="`${prefix}-side-nav-placeholder${collapsed ? '-hidden' : ''}`"></div>
  </div>
</template>

<script setup lang="ts">
import union from 'lodash/union';
import type { PropType } from 'vue';
import { computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

import AssetLogoFull from '@/assets/assets-logo-full.svg?component';
import AssetLogo from '@/assets/assets-t-logo.svg?component';
import { prefix } from '@/config/global';
import { getActive, getRoutesExpanded } from '@/router';
import { useSettingStore, useUserStore } from '@/store';
import type { MenuRoute } from '@/types/interface';

import pgk from '../../../package.json';
import MenuContent from './MenuContent.vue';

const MIN_POINT = 992 - 1;

const props = defineProps({
  menu: {
    type: Array as PropType<MenuRoute[]>,
    default: (): MenuRoute[] => [],
  },
  showLogo: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  isFixed: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  layout: {
    type: String as PropType<string>,
    default: '',
  },
  headerHeight: {
    type: String as PropType<string>,
    default: '64px',
  },
  theme: {
    type: String as PropType<'light' | 'dark'>,
    default: 'light',
  },
  isCompact: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
});

const collapsed = computed(() => useSettingStore().state.isSidebarCompact);

const route = useRoute();
const active = computed(() => getActive(route));
const user = useUserStore();

const changeCollapsed = () => {
  settingStore.updateConfig({
    isSidebarCompact: !settingStore.state.isSidebarCompact,
  });
};
const defaultExpanded = computed(() => {
  const path = getActive(route);
  const parentPath = path.substring(0, path.lastIndexOf('/'));
  const expanded = getRoutesExpanded();
  return union(expanded, parentPath === '' ? [] : [parentPath]);
});

const sideNavCls = computed(() => {
  const { isCompact } = props;
  return [
    `${prefix}-sidebar-layout`,
    {
      [`${prefix}-sidebar-compact`]: isCompact,
    },
  ];
});

const menuCls = computed(() => {
  const { showLogo, isFixed, layout } = props;
  return [
    `${prefix}-side-nav`,
    {
      [`${prefix}-side-nav-no-logo`]: !showLogo,
      [`${prefix}-side-nav-no-fixed`]: !isFixed,
      [`${prefix}-side-nav-mix-fixed`]: layout === 'mix' && isFixed,
    },
  ];
});

const router = useRouter();
const settingStore = useSettingStore();

const autoCollapsed = () => {
  const isCompact = window.innerWidth <= MIN_POINT;
  settingStore.updateConfig({
    isSidebarCompact: isCompact,
  });
};

onMounted(() => {
  autoCollapsed();
  window.onresize = () => {
    autoCollapsed();
  };
});

const goHome = () => {
  if (settingStore.state.isSidebarCompact) {
    settingStore.updateConfig({
      isSidebarCompact: false,
    });
    return;
  }
  router.push('/dashboard/base');
};

const getLogo = () => {
  if (collapsed.value) return AssetLogo;
  return AssetLogoFull;
};

const handleNav = (url: string) => {
  router.push(url);
};

const handleLogout = () => {
  router.push({
    path: '/login',
    query: { redirect: encodeURIComponent(router.currentRoute.value.fullPath) },
  });
};
</script>

<style lang="less" scoped>
.side-nav-operations {
  padding: 8px;
}

.user-info-container {
  display: flex;
  align-items: center;
  padding: 8px;
  cursor: pointer;
  border-radius: var(--td-radius-medium);
  transition: all 0.3s;

  &:hover {
    background-color: var(--td-bg-color-container-hover);

    .more-icon {
      color: var(--td-text-color-primary);
    }
  }

  .more-icon {
    margin-left: 4px;
    color: var(--td-text-color-placeholder);
    transition: all 0.3s;
  }

  &.collapsed {
    padding: 8px 0;
    justify-content: center;
  }
}

.user-avatar {
  flex: 0 0 32px;
  min-width: 32px;
  min-height: 32px;
}

.user-details {
  margin-left: 12px;
  flex: 1;
  overflow: hidden;
  text-align: left;

  .user-name {
    font: var(--td-font-title-small);
    color: var(--td-text-color-primary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.2;
  }

  .user-role {
    font: var(--td-font-link-small);
    color: var(--td-text-color-placeholder);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.2;
    margin-top: 2px;
  }
}

.version-container {
  margin-top: 4px;
  font-size: 10px;
  color: var(--td-text-color-placeholder);
  text-align: center;
  opacity: 0.6;
}

.operations-dropdown-container-item {
  width: 100%;
  display: flex;
  align-items: center;

  :deep(.t-dropdown__item-text) {
    display: flex;
    align-items: center;
  }

  .t-icon {
    font-size: var(--td-comp-size-xxxs);
    margin-right: var(--td-comp-margin-s);
  }

  :deep(.t-dropdown__item) {
    width: 100%;
    margin-bottom: 0px;
  }
}
</style>
