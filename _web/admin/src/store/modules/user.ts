import {jwtDecode} from 'jwt-decode';
import { defineStore } from 'pinia';

import ApiUserMine from '@/api/user/ApiUserMine';
import { REFRESH_TOKEN_NAME, TOKEN_NAME, TOKEN_NAME_EXP, TOKEN_SESSION_ID, USER_INFO } from '@/config/global';
import { usePermissionStore } from '@/store';
import type { UserInfo } from '@/types/interface';

const InitUserInfo: UserInfo = {
  id: '', // 用户id
  avatarFileUrl: '', // 头像，用于展示在页面右上角头像处
  name: '', // 用户名，用于展示在页面右上角头像处
  roles: [], // 前端权限模型使用 如果使用请配置modules/permission-fe.ts使用
};

export const useUserStore = defineStore({
  id: 'user',
  state: () => ({
    token: localStorage.getItem(TOKEN_NAME),
    tokenSessionId: localStorage.getItem(TOKEN_SESSION_ID),
    refreshTokenValue: localStorage.getItem(REFRESH_TOKEN_NAME),
    userInfo: localStorage.getItem(USER_INFO) ? JSON.parse(localStorage.getItem(USER_INFO)) : { ...InitUserInfo },
  }),
  getters: {
    roles: (state) => {
      return state.userInfo?.roles;
    },
  },
  actions: {
    async login(userInfo: Record<string, unknown>) {
      const res = await ApiUserMine.login(userInfo);
      if (res.token) {
        this.userInfo = {
          avatarFileUrl: res.avatarFileUrl,
          name: res.name,
          id: res.id,
          userType: res.userType,
          username: res.username,
        };
        this.setToken(res.token, res.refreshToken);
        localStorage.setItem(USER_INFO, JSON.stringify(this.userInfo));
      } else {
        throw res;
      }
    },
    async logout() {
      if (this.token) {
        await ApiUserMine.logout()
          .then(() => {
            this.token = '';
          })
          .catch(() => {
            this.token = '';
          });
      }
      this.userInfo = { ...InitUserInfo };
      localStorage.setItem(TOKEN_NAME, '');
      localStorage.setItem(USER_INFO, '');
      localStorage.setItem(REFRESH_TOKEN_NAME, '');
    },
    refreshToken() {
      return ApiUserMine.refreshToken();
    },
    setToken(token: string, refreshToken: string) {
      const jwt = jwtDecode(token);
      this.token = token;
      localStorage.setItem(TOKEN_NAME_EXP, jwt.exp.toString());
      localStorage.setItem(TOKEN_SESSION_ID, (jwt as any).token_session_id);
      localStorage.setItem(TOKEN_NAME, token);
      localStorage.setItem(REFRESH_TOKEN_NAME, refreshToken);
    },
  },
  persist: {
    afterHydrate: () => {
      const permissionStore = usePermissionStore();
      permissionStore.initRoutes().then();
    },
    key: 'user',
    pick: ['token'],
  },
});
