import keys from 'lodash/keys';
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { Color } from 'tvision-color';

import { DARK_CHART_COLORS, LIGHT_CHART_COLORS } from '@/config/color';
import STYLE_CONFIG from '@/config/style';
import { generateColorMap, insertThemeStylesheet } from '@/utils/color';

const defaultState = {
  ...STYLE_CONFIG,
  showSettingPanel: false,
  colorList: {} as Record<string, any>,
  chartColors: LIGHT_CHART_COLORS,
};

export type TState = typeof defaultState & {
  [key: string]: any;
};
export type TStateKey = keyof typeof defaultState;

export const useSettingStore = defineStore('setting', () => {
  // state
  const state = ref({ ...defaultState });

  // getters
  const showSidebar = computed(() => state.value.layout !== 'top');
  const showSidebarLogo = computed(() => state.value.layout === 'side');
  const showHeaderLogo = computed(() => state.value.layout !== 'side');
  const displayMode = computed((): 'dark' | 'light' => {
    if (state.value.mode === 'auto') {
      const media = window.matchMedia('(prefers-color-scheme:dark)');
      if (media.matches) {
        return 'dark';
      }
      return 'light';
    }
    return state.value.mode as 'dark' | 'light';
  });

  // actions
  async function changeMode(mode: 'dark' | 'light' | 'auto') {
    let theme = mode;
    if (mode === 'auto') {
      const media = window.matchMedia('(prefers-color-scheme:dark)');
      if (media.matches) {
        theme = 'dark';
      } else {
        theme = 'light';
      }
    }
    const isDarkMode = theme === 'dark';
    document.documentElement.setAttribute('theme-mode', isDarkMode ? 'dark' : '');
    state.value.chartColors = isDarkMode ? DARK_CHART_COLORS : LIGHT_CHART_COLORS;
  }

  function changeBrandTheme(brandTheme: string) {
    const mode = displayMode.value;
    const colorKey = `${brandTheme}[${mode}]`;
    let colorMap = state.value.colorList[colorKey];
    if (colorMap === undefined) {
      const [{ colors: newPalette, primary: brandColorIndex }] = Color.getColorGradations({
        colors: [brandTheme],
        step: 10,
        remainInput: false,
      });
      colorMap = generateColorMap(brandTheme, newPalette, mode as 'light' | 'dark', brandColorIndex);
      state.value.colorList[colorKey] = colorMap;
    }
    insertThemeStylesheet(brandTheme, colorMap, mode as 'light' | 'dark');
    document.documentElement.setAttribute('theme-color', brandTheme);
  }

  function updateConfig(payload: Partial<TState>) {
    for (const key in payload) {
      if (payload[key as TStateKey] !== undefined) {
        (state.value as TState)[key] = payload[key as TStateKey];
      }
      if (key === 'mode') {
        changeMode(payload[key] as 'dark' | 'light' | 'auto');
      }
      if (key === 'brandTheme') {
        changeBrandTheme(payload[key]);
      }
    }
  }

  return {
    state,
    showSidebar,
    showSidebarLogo,
    showHeaderLogo,
    displayMode,
    changeMode,
    changeBrandTheme,
    updateConfig,
  };
}, {
  persist: true,
});
