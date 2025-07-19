
import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/mine',
    component: Layout,
    name: 'mine',
    hidden: true,
    children: [
      {
        path: 'mine-index',
        name: 'mine-index',
        component: () => import('@/pages/sys/mine/index.vue'),
        meta: {
          title: '个人中心',
        },
      },
      {
        path: 'edit',
        name: 'edit',
        component: () => import('@/pages/sys/mine/edit.vue'),
        meta: {
          title: '编辑个人信息',
        },
      },
      {
        path: 'edit-password',
        name: 'edit-password',
        component: () => import('@/pages/sys/mine/edit-password.vue'),
        meta: {
          title: '修改密码',
        },
      },
    ],
  },
  {
    path: '/mine-notice',
    component: Layout,
    name: 'notice',
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'index',
        component: () => import('@/pages/sys/notice-receiver/mine-index.vue'),
        meta: {
          title: '公告列表',
        },
      },
      {
        path: 'detail',
        name: 'detail',
        component: () => import('@/pages/sys/notice/detail.vue'),
        meta: {
          title: '公告详情',
        },
      },
    ],
  },
];
