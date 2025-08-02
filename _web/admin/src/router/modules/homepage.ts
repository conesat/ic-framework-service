import { DashboardIcon } from 'tdesign-icons-vue-next';
import { shallowRef } from 'vue';

import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard/base',
    name: 'dashboard',
    meta: {
      title: '仪表盘',
      icon: shallowRef(DashboardIcon),
      orderNo: 0,
    },
    children: [
      {
        path: 'docs',
        name: 'docs',
        component: () => import('@/pages/dashboard/docs/index.vue'),
        meta: {
          title: '关于框架',
        },
      },
      {
        path: 'base',
        name: 'DashboardBase',
        component: () => import('@/pages/dashboard/base/index.vue'),
        meta: {
          title: '概览仪表盘',
        },
      },
      {
        path: 'detail',
        name: 'DashboardDetail',
        component: () => import('@/pages/dashboard/detail/index.vue'),
        meta: {
          title: '统计报表',
        },
      },
      {
        path: 'ops',
        name: 'DashboardOps',
        component: () => import('@/pages/dashboard/ops/index.vue'),
        meta: {
          title: '运维监控',
        },
      },
    ],
  },
];
