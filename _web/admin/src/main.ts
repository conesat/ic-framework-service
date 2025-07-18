/* eslint-disable simple-import-sort/imports */
import TDesign from 'tdesign-vue-next';
import { createApp } from 'vue';
import TDesignChat from '@tdesign-vue-next/chat'; // 引入chat组件

import App from './App.vue';
import router from './router';
import { store } from './store';

import 'tdesign-vue-next/es/style/index.css';
import '@/style/index.less';
import './permission';

import '@/assets/theme/tencent-cloud.css';

const app = createApp(App);

app.use(TDesign).use(store).use(router).use(TDesignChat);
app.mount('#app');
