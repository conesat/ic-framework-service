<template>
  <t-drawer v-model:visible="showSettingPanel" size="408px" :footer="false" :close-btn="true"
    class="setting-drawer-container premium-glass-drawer" @close-btn-click="handleCloseDrawer">
    <template #header>
      <div class="setting-drawer-header">
        <div class="header-content">
          <t-icon name="setting" size="24px" class="header-icon" />
          <div class="header-text">
            <h3>页面配置</h3>
            <p>自定义您的工作空间视觉体验</p>
          </div>
        </div>
      </div>
    </template>

    <div class="setting-container">
      <div class="setting-section">
        <div class="section-title">
          <span>外观模式</span>
        </div>
        <div class="mode-selection-grid">
          <div v-for="item in MODE_OPTIONS" :key="item.type" class="mode-card"
            :class="{ active: formData.mode === item.type }" @click="formData.mode = item.type">
            <div class="mode-card-icon">
              <component :is="getModeIcon(item.type)" />
            </div>
            <span class="mode-card-text">{{ item.text }}</span>
            <div class="active-indicator"></div>
          </div>
        </div>
      </div>

      <div class="setting-section">
        <div class="section-title">
          <span>系统主题色</span>
        </div>
        <div class="color-selection-grid">
          <div v-for="item in DEFAULT_COLOR_OPTIONS" :key="item" class="color-swatch-wrapper"
            :class="{ active: formData.brandTheme === item }" @click="formData.brandTheme = item">
            <div class="color-swatch" :style="{ backgroundColor: item }">
              <t-icon v-if="formData.brandTheme === item" name="check" size="14px" style="color: #fff" />
            </div>
          </div>
          <div class="color-swatch-wrapper custom-color">
            <t-popup destroy-on-close expand-animation placement="bottom-right" trigger="click"
              :visible="isColoPickerDisplay" :overlay-style="{ padding: 0 }" @visible-change="onPopupVisibleChange">
              <template #content>
                <t-color-picker-panel :on-change="changeColor" :color-modes="['monochrome']" format="HEX"
                  :swatch-colors="[]" />
              </template>
              <div class="color-swatch dynamic-color-btn"
                :style="{ backgroundColor: dynamicColor || 'var(--td-component-stroke)' }">
                <t-icon name="edit" size="14px"
                  :style="{ color: dynamicColor ? '#fff' : 'var(--td-text-color-placeholder)' }" />
              </div>
            </t-popup>
          </div>
        </div>
      </div>

      <div class="setting-section">
        <div class="section-title">
          <span>界面元素显示</span>
        </div>
        <div class="premium-switch-list">
          <div class="switch-item">
            <div class="switch-info">
              <span class="switch-label">显示顶部栏 (Header)</span>
              <span class="switch-desc">控制主页面顶部的可见性</span>
            </div>
            <t-switch v-model="formData.showHeader" size="medium" />
          </div>
          <div class="switch-item">
            <div class="switch-info">
              <span class="switch-label">显示面包屑 (Breadcrumbs)</span>
              <span class="switch-desc">显示当前页面的路径导航</span>
            </div>
            <t-switch v-model="formData.showBreadcrumb" size="medium" />
          </div>
          <div class="switch-item">
            <div class="switch-info">
              <span class="switch-label">显示页脚 (Footer)</span>
              <span class="switch-desc">显示页面底部的版权信息</span>
            </div>
            <t-switch v-model="formData.showFooter" size="medium" />
          </div>
          <div class="switch-item">
            <div class="switch-info">
              <span class="switch-label">使用多标签页 (Tabs)</span>
              <span class="switch-desc">开启多任务快速切换导航</span>
            </div>
            <t-switch v-model="formData.isUseTabsRouter" size="medium" />
          </div>
        </div>
      </div>
    </div>
  </t-drawer>
</template>

<script setup lang="ts">
import type { PopupVisibleChangeContext } from 'tdesign-vue-next';
import { computed, onMounted, ref, watchEffect } from 'vue';

import SettingAutoIcon from '@/assets/assets-setting-auto.svg';
import SettingDarkIcon from '@/assets/assets-setting-dark.svg';
import SettingLightIcon from '@/assets/assets-setting-light.svg';
import { DEFAULT_COLOR_OPTIONS } from '@/config/color';
import STYLE_CONFIG from '@/config/style';
import { useSettingStore } from '@/store';

const settingStore = useSettingStore();

const MODE_OPTIONS = [
  { type: 'light', text: '明亮' },
  { type: 'dark', text: '暗黑' },
  { type: 'auto', text: '跟随系统' },
];

const initStyleConfig = () => {
  const styleConfig = { ...STYLE_CONFIG };
  for (const key in styleConfig) {
    if (Object.prototype.hasOwnProperty.call(styleConfig, key)) {
      (styleConfig[key as keyof typeof STYLE_CONFIG] as any) = settingStore.state[key as keyof typeof STYLE_CONFIG];
    }
  }
  return styleConfig;
};

const dynamicColor = computed(() => {
  const isDynamic = DEFAULT_COLOR_OPTIONS.indexOf(formData.value.brandTheme) === -1;
  return isDynamic ? formData.value.brandTheme : '';
});
const formData = ref({ ...initStyleConfig() });
const isColoPickerDisplay = ref(false);

const showSettingPanel = computed({
  get() {
    return settingStore.state.showSettingPanel;
  },
  set(newVal: boolean) {
    settingStore.updateConfig({
      showSettingPanel: newVal,
    });
  },
});

const changeColor = (hex: string) => {
  formData.value.brandTheme = hex;
};

onMounted(() => {
  const btn = document.querySelector('.dynamic-color-btn');
  if (btn) {
    btn.addEventListener('click', () => {
      isColoPickerDisplay.value = true;
    });
  }
});

const onPopupVisibleChange = (visible: boolean, context: PopupVisibleChangeContext) => {
  if (!visible && context.trigger === 'document') {
    isColoPickerDisplay.value = visible;
  }
};

const getModeIcon = (mode: string) => {
  if (mode === 'light') {
    return SettingLightIcon;
  }
  if (mode === 'dark') {
    return SettingDarkIcon;
  }
  return SettingAutoIcon;
};

const handleCloseDrawer = () => {
  settingStore.updateConfig({
    showSettingPanel: false,
  });
};

watchEffect(() => {
  if (formData.value.brandTheme) settingStore.updateConfig(formData.value);
});
</script>

<style lang="less">
.premium-glass-drawer {
  .t-drawer__content-wrapper {
    backdrop-filter: blur(20px);
    background: color-mix(in srgb, var(--td-bg-color-container) 85%, transparent) !important;
  }

  .t-drawer__header {
    padding: 24px;
    border-bottom: 1px solid var(--td-component-stroke);
    background: transparent;
  }

  .t-drawer__body {
    padding: 0;
  }
}

.setting-drawer-header {
  .header-content {
    display: flex;
    align-items: center;
    gap: 16px;

    .header-icon {
      color: var(--td-brand-color);
      padding: 8px;
      background: var(--td-brand-color-light);
      border-radius: 12px;
    }

    .header-text {
      h3 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: var(--td-text-color-primary);
      }

      p {
        margin: 4px 0 0 0;
        font-size: 12px;
        color: var(--td-text-color-placeholder);
      }
    }
  }
}

.setting-container {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.setting-section {
  .section-title {
    font-size: 14px;
    font-weight: 600;
    color: var(--td-text-color-primary);
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    gap: 8px;

    &::after {
      content: '';
      flex: 1;
      height: 1px;
      background: linear-gradient(to right, var(--td-component-stroke), transparent);
    }
  }
}

.mode-selection-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;

  .mode-card {
    position: relative;
    padding: 16px 8px;
    background: var(--td-bg-color-component);
    border: 2px solid transparent;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover {
      transform: translateY(-2px);
      background: var(--td-bg-color-container-hover);
    }

    &.active {
      background: var(--td-bg-color-container);
      border-color: var(--td-brand-color);
      box-shadow: var(--td-shadow-1);

      .mode-card-icon {
        color: var(--td-brand-color);
      }

      .active-indicator {
        opacity: 1;
        transform: scaleX(1);
      }
    }

    .mode-card-icon {
      font-size: 24px;
      color: var(--td-text-color-secondary);
      transition: color 0.3s;
    }

    .mode-card-text {
      font-size: 12px;
      font-weight: 500;
      color: var(--td-text-color-primary);
    }

    .active-indicator {
      position: absolute;
      bottom: 0;
      left: 20%;
      right: 20%;
      height: 3px;
      background: var(--td-brand-color);
      border-radius: 3px 3px 0 0;
      opacity: 0;
      transform: scaleX(0);
      transition: all 0.3s;
    }
  }
}

.color-selection-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;

  .color-swatch-wrapper {
    aspect-ratio: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border-radius: 50%;
    border: 2px solid transparent;
    transition: all 0.2s;

    &:hover {
      transform: scale(1.1);
    }

    &.active {
      border-color: var(--td-brand-color);
      padding: 2px;
    }

    .color-swatch {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: inset 0 0 0 1px rgba(0, 0, 0, 0.05);
    }
  }
}

.premium-switch-list {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .switch-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: var(--td-bg-color-component);
    border-radius: 12px;
    transition: background 0.3s;

    &:hover {
      background: var(--td-bg-color-container-hover);
    }

    .switch-info {
      display: flex;
      flex-direction: column;
      gap: 2px;

      .switch-label {
        font-size: 14px;
        font-weight: 500;
        color: var(--td-text-color-primary);
      }

      .switch-desc {
        font-size: 12px;
        color: var(--td-text-color-placeholder);
      }
    }
  }
}

// 适配暗黑模式
[theme-mode='dark'] {
  .premium-glass-drawer {
    .t-drawer__content-wrapper {
      background: rgba(30, 30, 30, 0.8) !important;
    }
  }
}
</style>
