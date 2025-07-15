// axios配置  可自行根据项目进行更改，只需更改该文件即可，其他文件可以不动
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import isString from 'lodash/isString';
import merge from 'lodash/merge';

import ApiUserMine from '@/api/user/ApiUserMine';
import { REFRESH_TOKEN_NAME, TOKEN_NAME, TOKEN_NAME_EXP } from '@/config/global';
import { ContentTypeEnum } from '@/constants';
import { useUserStore } from '@/store';

import { VAxios } from './Axios';
import type { AxiosTransform, CreateAxiosOptions } from './AxiosTransform';
import { formatRequestDate, joinTimestamp, setObjToUrlParams } from './utils';

const env = import.meta.env.MODE || 'development';

// 如果是mock模式 或 没启用直连代理 就不配置host 会走本地Mock拦截 或 Vite 代理
const host = env === 'mock' || import.meta.env.VITE_IS_REQUEST_PROXY !== 'true' ? '' : import.meta.env.VITE_API_URL;

// 是否正在刷新的标志
declare global {
  interface Window {
    isRefreshing: boolean;
  }
}
window.isRefreshing = false;
// 存储请求的数组
let cacheRequestArr: any[] = [];

// 将所有的请求都push到数组中,其实数组是[function(token){}, function(token){},...]
function cacheRequestArrHandle(cb: any) {
  cacheRequestArr.push(cb);
}

// 数组中的请求得到新的token之后自执行，用新的token去重新发起请求
function afreshRequest(token: any) {
  cacheRequestArr.forEach((cb) => cb(token));
  cacheRequestArr = [];
}

// 判断token是否即将过期
function isTokenExpired() {
  return new Date().getTime() / 1000 > parseInt(localStorage.getItem(TOKEN_NAME_EXP), 10);
}

// 数据处理，方便区分多种处理方式
const transform: AxiosTransform = {
  // 处理请求数据。如果数据不是预期格式，可直接抛出错误
  transformRequestHook: (res, options) => {
    const { isReturnNativeResponse } = options;

    // 如果204无内容直接返回
    const method = res.config.method?.toLowerCase();
    if (res.status === 204 && ['put', 'patch', 'delete'].includes(method)) {
      return res;
    }
    if (res.status !== 200) {
      throw new Error('请求失败');
    }

    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) {
      return res;
    }
    // 错误的时候返回
    const { data } = res;
    const userStore = useUserStore();
    const { pathname } = window.location;
    if (pathname !== '/login' && !res.config.url.endsWith('/logout')) {
      // 初始化系统 5000+
      // 登录 4000+
      if (
        data.code === 4001 ||
        data.code === 4002 ||
        data.code === 4003 ||
        data.code === 5000 ||
        data.code === 5001 ||
        data.code === 5002
      ) {
        userStore.logout().then();
        setTimeout(() => {
          window.location.reload();
        }, 1500);
        return data;
      }
    }
    return data;
  },

  // 请求前处理配置
  beforeRequestHook: (config, options) => {
    const { apiUrl, isJoinPrefix, urlPrefix, joinParamsToUrl, formatDate, joinTime = true } = options;

    // 添加接口前缀
    if (isJoinPrefix && urlPrefix && isString(urlPrefix)) {
      config.url = `${urlPrefix}${config.url}`;
    }

    // 将baseUrl拼接
    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`;
    }
    const params = config.params || {};
    const data = config.data || false;

    if (formatDate && data && !isString(data)) {
      formatRequestDate(data);
    }
    if (config.method?.toUpperCase() === 'GET') {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        // 兼容restful风格
        config.url = `${config.url + params}${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
    } else if (!isString(params)) {
      if (formatDate) {
        formatRequestDate(params);
      }
      if (
        Reflect.has(config, 'data') &&
        config.data &&
        (Object.keys(config.data).length > 0 || data instanceof FormData)
      ) {
        config.data = data;
        config.params = params;
      } else {
        // 非GET请求如果没有提供data，则将params视为data
        config.data = params;
        config.params = undefined;
      }
      if (joinParamsToUrl) {
        config.url = setObjToUrlParams(config.url, { ...config.params, ...config.data });
      }
    } else {
      // 兼容restful风格
      config.url += params;
      config.params = undefined;
    }
    return config;
  },

  // 请求拦截器处理
  requestInterceptors: (config: any, options) => {
    // 请求之前处理config
    const token = localStorage.getItem(TOKEN_NAME);
    const refreshTokenValue = localStorage.getItem(REFRESH_TOKEN_NAME);
    if (token && refreshTokenValue && (config as Recordable)?.requestOptions?.withToken !== false) {
      // jwt token
      (config as Recordable).headers.Authorization = options.authenticationScheme
        ? `${options.authenticationScheme} ${token}`
        : token;
      if (config.url.endsWith(`${ApiUserMine.path}/refresh-token`)) {
        (config as Recordable).headers['Refresh-Token'] = options.authenticationScheme
          ? `${options.authenticationScheme} ${refreshTokenValue}`
          : refreshTokenValue;
      } else if (isTokenExpired() && refreshTokenValue) {
        // 判断token是否即将过期，且不是请求刷新token的接口
        // 所有的请求来了，先判断是否正在刷新token，
        // 如果不是，将刷新token标志置为true并请求刷新token.
        // 如果是，则先将请求缓存到数组中
        // 等到刷新完token后再次重新请求之前缓存的请求接口即可
        if (!window.isRefreshing) {
          // 标志改为true，表示正在刷新
          window.isRefreshing = true;
          useUserStore()
            .refreshToken()
            .then((res: { token: string; refreshToken: string } | null) => {
              if (res) {
                useUserStore().setToken(res.token, res.refreshToken);
                // 将刷新的token替代老的token
                config.headers.Authorization = res.token;
                // 刷新token完成后重新请求之前的请求
                afreshRequest(res.token);
              } else {
                useUserStore().setToken('', '');
              }
            })
            .catch((err: any) => {
              console.log(`refreshToken err =>${err}`);
            })
            .finally(() => {
              window.isRefreshing = false;
            });
          // 下面这段代码一定要写，不然第一个请求的接口带过去的token还是原来的，要将第一个请求也缓存起来
          return new Promise((resolve) => {
            cacheRequestArrHandle((token: string) => {
              config.headers.Authorization = token; // token为刷新完成后传入的token
              // 将请求挂起
              resolve(config);
            });
          });
        }
        return new Promise((resolve) => {
          cacheRequestArrHandle((token: string) => {
            config.headers.Authorization = token; // token为刷新完成后传入的token
            // 将请求挂起
            resolve(config);
          });
        });
      } else {
        return config;
      }
    }
    return config as InternalAxiosRequestConfig;
  },

  // 响应拦截器处理
  responseInterceptors: (res) => {
    return res;
  },

  // 响应错误处理
  responseInterceptorsCatch: (error: any, instance: AxiosInstance) => {
    const { config } = error;
    if (!config?.requestOptions?.retry) return Promise.reject(new Error(error.message || 'Unknown error'));

    config.retryCount = config.retryCount || 0;

    if (config.retryCount >= config.requestOptions.retry.count)
      return Promise.reject(new Error(error.message || 'Unknown error'));

    config.retryCount += 1;

    const backoff = new Promise((resolve) => {
      setTimeout(() => {
        resolve(config);
      }, config.requestOptions.retry.delay || 1);
    });
    config.headers = { ...config.headers, 'Content-Type': ContentTypeEnum.Json };
    return backoff.then((config) => instance.request(config));
  },
};

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  return new VAxios(
    merge(
      <CreateAxiosOptions>{
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
        // 例如: authenticationScheme: 'Bearer'
        authenticationScheme: '',
        // 超时
        timeout: 10 * 1000,
        // 携带Cookie
        withCredentials: true,
        // 头信息
        headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded, Platform: 'WEB' },
        // 数据处理方式
        transform,
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 接口地址
          apiUrl: host,
          // 是否自动添加接口前缀
          isJoinPrefix: true,
          // 接口前缀
          // 例如: https://www.baidu.com/api
          // urlPrefix: '/api'
          urlPrefix: import.meta.env.VITE_API_URL_PREFIX,
          // 是否返回原生响应头 比如：需要获取响应头时使用该属性
          isReturnNativeResponse: false,
          // 需要对返回数据进行处理
          isTransformResponse: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: true,
          // 是否加入时间戳
          joinTime: true,
          // 忽略重复请求
          ignoreRepeatRequest: true,
          // 是否携带token
          withToken: true,
          // 重试
          retry: {
            count: 3,
            delay: 1000,
          },
        },
      },
      opt || {},
    ),
  );
}

export const request = createAxios();
