<template>
  <div :class="layoutCls">
    <div v-if="layout === 'side'" :class="sideHeaderCls">
      <div v-if="showMenu" class="side-header-topbar">
        <div class="header-left-content">
          <l-breadcrumb v-if="settingStore.state.showBreadcrumb" in-header />
        </div>
        <div class="operations-container">
          <notice />
          <t-tooltip placement="bottom" content="帮助文档">
            <t-button theme="default" shape="square" variant="text" @click="navToHelper">
              <t-icon name="help-circle" />
            </t-button>
          </t-tooltip>
          <t-tooltip placement="bottom" content="系统设置">
            <t-button theme="default" shape="square" variant="text" @click="toggleSettingPanel">
              <t-icon name="setting" />
            </t-button>
          </t-tooltip>
        </div>
      </div>
      <div v-if="settingStore.state.isUseTabsRouter" class="side-header-tabs-wrap">
        <layout-tabs :class="`${prefix}-header-tabs`" />
      </div>
    </div>
    <t-head-menu v-else-if="showMenu" :class="menuCls" :theme="menuTheme" expand-type="popup" :value="active">
      <template #logo>
        <div class="header-left-content"></div>
      </template>
      <template v-if="layout !== 'side'" #default>
        <menu-content class="header-menu" :nav-data="menu" />
      </template>
      <template #operations>
        <div class="operations-container">
          <!-- 搜索框 -->
          <!--          <search v-if="layout !== 'side'" :layout="layout" />-->

          <!-- 全局通知 -->
          <notice />
          <t-tooltip placement="bottom" content="帮助文档">
            <t-button theme="default" shape="square" variant="text" @click="navToHelper">
              <t-icon name="help-circle" />
            </t-button>
          </t-tooltip>
          <t-tooltip placement="bottom" content="系统设置">
            <t-button theme="default" shape="square" variant="text" @click="toggleSettingPanel">
              <t-icon name="setting" />
            </t-button>
          </t-tooltip>
        </div>
      </template>
    </t-head-menu>
    <layout-tabs v-if="layout !== 'side' && settingStore.state.isUseTabsRouter" :class="`${prefix}-header-tabs`" />
  </div>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

import { prefix } from '@/config/global';
import { getActive } from '@/router';
import { useSettingStore } from '@/store';
import type { MenuRoute } from '@/types/interface';

import LBreadcrumb from './Breadcrumb.vue';
import LayoutTabs from './LayoutTabs.vue';
import MenuContent from './MenuContent.vue';
import Notice from './Notice.vue';

const props = defineProps({
  theme: {
    type: String,
    default: 'light',
  },
  layout: {
    type: String,
    default: 'top',
  },
  showLogo: {
    type: Boolean,
    default: true,
  },
  showMenu: {
    type: Boolean,
    default: true,
  },
  menu: {
    type: Array as PropType<MenuRoute[]>,
    default: (): MenuRoute[] => [],
  },
  isFixed: {
    type: Boolean,
    default: false,
  },
  isCompact: {
    type: Boolean,
    default: false,
  },
  maxLevel: {
    type: Number,
    default: 3,
  },
});

const route = useRoute();
const settingStore = useSettingStore();

const toggleSettingPanel = () => {
  settingStore.updateConfig({
    showSettingPanel: true,
  });
};

const active = computed(() => getActive(route));

const layoutCls = computed(() => [
  `${prefix}-header-layout`,
  {
    [`${prefix}-header-layout-fixed`]: props.showMenu && props.isFixed && props.layout !== 'side',
  },
]);

const sideHeaderCls = computed(() => [
  `${prefix}-header-side-shell`,
  {
    [`${prefix}-header-side-shell-fixed`]: props.isFixed,
    [`${prefix}-header-side-shell-fixed-compact`]: props.isFixed && props.isCompact,
  },
]);

const menuCls = computed(() => {
  const { isFixed, layout, isCompact } = props;
  return [
    {
      [`${prefix}-header-menu`]: !isFixed,
      [`${prefix}-header-menu-fixed`]: isFixed,
      [`${prefix}-header-menu-fixed-side`]: layout === 'side' && isFixed,
      [`${prefix}-header-menu-fixed-side-compact`]: layout === 'side' && isFixed && isCompact,
    },
  ];
});
const menuTheme = computed(() => props.theme as 'light' | 'dark');

const navToHelper = () => {
  window.open('http://icframework.chinahg.top');
};
</script>
<style lang="less" scoped>
@layout-min-width: 900px;

.@{starter-prefix}-header {
  &-layout {
    display: flex;
    flex-direction: column;
    min-width: 0;
    background: transparent;

    &-fixed {
      padding-top: 0;
    }
  }

  &-side-shell {
    display: flex;
    flex-direction: column;
    min-width: 0;
    box-sizing: border-box;
    margin: 20px 0 10px;
    background-color: var(--td-bg-color-container);
    border-radius: 20px;
    overflow: hidden;
    border: 1px solid color-mix(in srgb, var(--td-component-stroke) 70%, transparent);
    box-shadow: 0 10px 30px color-mix(in srgb, var(--td-bg-color-container-hover) 18%, transparent);

    &-fixed {
      position: relative;
      top: auto !important;
      right: auto !important;
      left: auto !important;
      z-index: 1001;
      transform: none !important;
      transition: none;

      &-compact {
        left: auto !important;
      }
    }
  }

  &-menu-fixed {
    position: fixed;
    top: 0;
    z-index: 1001;

    :deep(.t-head-menu__inner) {
      padding-right: var(--td-comp-margin-xl);
    }

    &-side {
      left: 232px;
      right: 0;
      z-index: 10;
      width: auto;
      transition: all 0.3s;

      &-compact {
        left: 64px;
      }
    }
  }

  &-logo-container {
    cursor: pointer;
    display: inline-flex;
  }

  &-tabs {
    width: 100%;
  }
}

.side-header-topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-width: 0;
  min-height: 56px;
  padding: 0 20px 0 24px;
  background: var(--td-bg-color-container);
}

.side-header-tabs-wrap {
  overflow: hidden;
  min-width: 0;
  padding: 0 20px;
  background-color: var(--td-bg-color-container);
  border-top: 1px solid color-mix(in srgb, var(--td-component-stroke) 72%, transparent);
}

.header-menu {
  flex: 1;
  display: inline-flex;

  :deep(.t-menu__item) {
    min-width: unset;
  }
}

.header-left-content {
  display: flex;
  align-items: center;
  min-width: 0;
  overflow: hidden;
  flex: 1;

  :deep(.t-breadcrumb) {
    min-width: 0;
    white-space: nowrap;
    font: var(--td-font-body-small);
  }

  :deep(.t-breadcrumb__item) {
    color: var(--td-text-color-secondary);
  }

  :deep(.t-breadcrumb__inner) {
    color: inherit;
  }

  :deep(.t-breadcrumb__item:last-child) {
    color: var(--td-text-color-primary);
  }
}

.operations-container {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-shrink: 0;
  gap: 6px;

  .t-popup__reference {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .t-button {
    margin-left: 0;
  }
}

.header-operate-left {
  display: flex;
  align-items: normal;
  line-height: 0;
  padding-left: var(--td-comp-margin-xl);
}

.header-logo-container {
  width: 184px;
  height: 26px;
  display: flex;
  margin-left: 24px;
  color: var(--td-text-color-primary);

  .t-logo {
    width: 100%;
    height: 100%;

    &:hover {
      cursor: pointer;
    }
  }

  &:hover {
    cursor: pointer;
  }
}

.header-user-account {
  display: inline-flex;
  align-items: center;
  color: var(--td-text-color-primary);
}

:deep(.t-head-menu__inner) {
  border-bottom: 1px solid var(--td-component-stroke);
}

:deep(.tdesign-starter-header-tabs .t-tabs__header) {
  margin: 0;
}

:deep(.tdesign-starter-header-tabs .t-tabs__nav-wrap) {
  overflow: hidden;
}

:deep(.tdesign-starter-header-tabs .t-tabs__nav) {
  min-height: auto;
  padding: 0;
  background: transparent;
}

:deep(.tdesign-starter-header-tabs .t-tabs__nav-item) {
  margin: 0;
  border-radius: 2px !important;
  border: none !important;
}

:deep(.tdesign-starter-header-tabs.t-tabs) {
  background: transparent;
}

.t-menu--light {
  .header-user-account {
    color: var(--td-text-color-primary);
  }
}

.t-menu--dark {
  .t-head-menu__inner {
    border-bottom: 1px solid var(--td-gray-color-10);
  }

  .side-header-tabs-wrap {
    background: color-mix(in srgb, var(--td-gray-color-11) 35%, transparent);
  }

  .header-user-account {
    color: rgba(255, 255, 255, 0.55);
  }
}
</style>

<!-- eslint-disable-next-line vue-scoped-css/enforce-style-type -->
<style lang="less"></style>
