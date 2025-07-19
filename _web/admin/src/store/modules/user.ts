import { jwtDecode } from 'jwt-decode';
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

import ApiUserMine from '@/api/sys/ApiUserMine';
import { REFRESH_TOKEN_NAME, TOKEN_NAME, TOKEN_NAME_EXP, TOKEN_SESSION_ID, USER_INFO } from '@/config/global';
import { usePermissionStore } from '@/store';
import type { UserInfo } from '@/types/interface';

const InitUserInfo: UserInfo = {
  id: '',
  avatarFileUrl: '',
  name: '',
  roles: [],
};

export const useUserStore = defineStore(
  'user',
  () => {
    // state
    const token = ref(localStorage.getItem(TOKEN_NAME) || '');
    const tokenSessionId = ref(localStorage.getItem(TOKEN_SESSION_ID) || '');
    const refreshTokenValue = ref(localStorage.getItem(REFRESH_TOKEN_NAME) || '');
    const userInfo = ref<UserInfo>(
      localStorage.getItem(USER_INFO) ? JSON.parse(localStorage.getItem(USER_INFO)!) : { ...InitUserInfo }
    );

    // getters
    const roles = computed(() => userInfo.value?.roles);

    // actions
    async function login(userInfoParam: Record<string, unknown>) {
      const res = await ApiUserMine.login(userInfoParam);
      if (res.token) {
        userInfo.value = {
          avatarFileUrl: res.avatarFileUrl,
          name: res.name,
          id: res.id,
          userType: res.userType,
          username: res.username,
          roles: []
        } as UserInfo;
        setToken(res.token, res.refreshToken);
        localStorage.setItem(USER_INFO, JSON.stringify(userInfo.value));
      } else {
        throw res;
      }
    }

    async function logout() {
      if (token.value) {
        await ApiUserMine.logout()
          .then(() => {
            token.value = '';
          })
          .catch(() => {
            token.value = '';
          });
      }
      userInfo.value = { ...InitUserInfo };
      localStorage.setItem(TOKEN_NAME, '');
      localStorage.setItem(USER_INFO, '');
      localStorage.setItem(REFRESH_TOKEN_NAME, '');
    }

    function refreshToken() {
      return ApiUserMine.refreshToken();
    }

    function setToken(tokenStr: string, refreshToken: string) {
      const jwt = jwtDecode(tokenStr);
      token.value = tokenStr;
      localStorage.setItem(TOKEN_NAME_EXP, jwt.exp.toString());
      localStorage.setItem(TOKEN_SESSION_ID, (jwt as any).token_session_id);
      localStorage.setItem(TOKEN_NAME, tokenStr);
      localStorage.setItem(REFRESH_TOKEN_NAME, refreshToken);
    }

    return {
      token,
      tokenSessionId,
      refreshTokenValue,
      userInfo,
      roles,
      login,
      logout,
      refreshToken,
      setToken,
    };
  },
  {
    persist: {
      afterHydrate: () => {
        const permissionStore = usePermissionStore();
        permissionStore.initRoutes().then();
      },
      key: 'user',
      pick: ['token'],
    },
  }
);
