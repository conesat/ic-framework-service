<template>
  <div v-if="currentCrumb" :class="breadcrumbCls">
    <component v-if="parentIconComponent" :is="parentIconComponent" class="breadcrumb-icon breadcrumb-icon--component" />
    <t-icon v-else :name="parentIconName" class="breadcrumb-icon" />
    <span v-if="parentCrumb" class="breadcrumb-parent">{{ parentCrumb.title }}</span>
    <span v-if="parentCrumb" class="breadcrumb-separator">|</span>
    <span class="breadcrumb-current">{{ currentCrumb.title }}</span>
  </div>
</template>

<script setup lang="ts">
import { computed, unref } from 'vue';
import { useRoute } from 'vue-router';

const svgModules = import.meta.glob('@/assets/svg-icons/*.svg', { eager: true });
const svgIcons = new Map<string, any>();

Object.keys(svgModules).forEach((key) => {
  svgIcons.set(key.replace('/src/assets/svg-icons/', ''), svgModules[key]);
});

const props = defineProps({
  inHeader: {
    type: Boolean,
    default: false,
  },
});

const route = useRoute();

const breadcrumbCls = computed(() => [
  'tdesign-breadcrumb',
  {
    'tdesign-breadcrumb--header': props.inHeader,
  },
]);

const crumbs = computed(() => {
  return route.matched
    .filter((record) => !record.meta?.hiddenBreadcrumb)
    .map((record) => ({
      to: record.path.startsWith('/') ? record.path : `/${record.path}`,
      title: (record.meta?.title as string) ?? record.name ?? record.path,
      icon: unref(record.meta?.icon),
      svgIcon: record.meta?.svgIcon,
    }));
});

const currentCrumb = computed(() => {
  const current = crumbs.value.at(-1);
  return current ?? null;
});

const parentCrumb = computed(() => {
  if (crumbs.value.length < 2) {
    return null;
  }
  return crumbs.value.at(-2) ?? null;
});

const iconCrumb = computed(() => {
  const matchedRecord = route.matched.find((record) => record.meta?.icon || record.meta?.svgIcon);
  if (matchedRecord) {
    return {
      icon: unref(matchedRecord.meta?.icon),
      svgIcon: matchedRecord.meta?.svgIcon,
    };
  }
  return parentCrumb.value ?? currentCrumb.value;
});

const parentIconComponent = computed(() => {
  const svgIcon = iconCrumb.value?.svgIcon;
  if (svgIcon) {
    const svgKey = svgIcon.endsWith('.svg') ? svgIcon : `${svgIcon}.svg`;
    const svgModule = svgIcons.get(svgKey);
    if (svgModule && typeof svgModule !== 'function') {
      return svgModule.default;
    }
  }
  const icon = iconCrumb.value?.icon;
  return typeof icon === 'string' || !icon ? null : icon;
});

const parentIconName = computed(() => {
  const icon = iconCrumb.value?.icon;
  return typeof icon === 'string' ? icon : 'folder-open';
});
</script>

<style scoped lang="less">
.tdesign-breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
  margin-bottom: 24px;
  color: var(--td-text-color-secondary);
  font: var(--td-font-body-small);
}

.tdesign-breadcrumb--header {
  margin-bottom: 0;
}

.breadcrumb-icon {
  flex-shrink: 0;
  font-size: 17px;
  color: var(--td-brand-color);
}

.breadcrumb-icon--component {
  width: 17px;
  height: 17px;

  :deep(svg) {
    width: 100%;
    height: 100%;
    display: block;
  }

  :deep(path) {
    fill: var(--td-brand-color);
    stroke: var(--td-brand-color);
  }
}

.breadcrumb-parent,
.breadcrumb-current {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb-parent {
  font-size: 16px;
  font-weight: 700;
  line-height: 1;
  color: var(--td-text-color-primary);
}

.breadcrumb-separator {
  flex-shrink: 0;
  font-size: 13px;
  color: var(--td-text-color-placeholder);
}

.breadcrumb-current {
  font-size: 13px;
  line-height: 1;
  color: var(--td-text-color-secondary);
}
</style>
