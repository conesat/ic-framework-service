import {MessagePlugin} from 'tdesign-vue-next';

import {request} from '@/utils/request';

const beforeMsg: any = {};
const http = {
  delete(param: RequestParam) {
    request
      .delete<any>({
        url: param.url,
        params: param.data,
      })
      .then((res) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        console.log(error);
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  post(param: RequestParam) {
    request
      .post(
        {
          url: param.url,
          params: param.data,
        },
        {
          withToken: !param.skipToken,
        },
      )
      .then((res) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  postJson(param: RequestParam) {
    request
      .post(
        {
          url: param.url,
          params: param.data,
          headers: {
            'Content-Type': 'application/json',
          },
        },
        {
          withToken: !param.skipToken,
        },
      )
      .then((res) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  upload(param: UploadRequestParam) {
    request
      .post({
        url: param.url,
        params: param.data,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (e: any) => {
          if (param.onprogress) {
            param.onprogress(e.progress * 100);
          }
        },
      })
      .then((res) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  put(param: RequestParam) {
    request
      .put({
        url: param.url,
        params: param.data,
      })
      .then((res) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  get(param: RequestParam) {
    request
      .get({
        url: param.url,
        params: param.data,
      })
      .then((res: any) => {
        this.thenFun(res, param.success, param.fail);
      })
      .catch((error) => {
        this.catchFun(error, param.fail);
      })
      .finally(() => {
        param.finally && param.finally();
      });
  },
  handleParams(params: any) {
    const fd = new FormData();
    for (const key in params) {
      fd.append(key, params[key]);
    }
    return fd;
  },
  thenFun(res: Response, success?: Function, fail?: Function) {
    if (res.code !== 0) {
      if (fail && typeof fail === 'function') {
        fail(res);
      } else {
        const eTime = new Date().getTime();
        if (!beforeMsg[res.msg] || beforeMsg[res.msg] < eTime - 1500) {
          beforeMsg[res.msg] = eTime;
          MessagePlugin.error(res.msg ?? '未知错误').then();
        }
      }
      return;
    }
    if (success && typeof success === 'function') {
      success(res.data);
    }
  },
  catchFun(error?: Function, fail?: Function) {
    console.log(error)
    if (fail && typeof fail === 'function') {
      fail(error);
    } else {
      MessagePlugin.error('请求失败').then();
    }
  },
};

export default http;

export interface RequestParam {
  // 请求地址
  url?: string;
  // 请求参数
  data?: any;
  success?: (response: any | null) => void;
  fail?: (res: any | null) => void;
  finally?: Function;
  // 是否忽略token
  skipToken?: boolean;
}

export interface PageRequestParam extends RequestParam {
  // 分页参数
  pagination?: any;
  success?: (response: PageResponse | null) => void;
}

export interface ChangeEnableRequestParam extends RequestParam {
  ids: (number | string)[];
  // 是否启用
  enable: (number | boolean);
}

export interface DeleteRequestParam extends RequestParam {
  ids: (number | string)[];
}

export interface DetailRequestParam extends RequestParam {
  id: any;
}

export interface UploadRequestParam extends RequestParam {
  // 上传回调
  onprogress?: (progress: number) => void;
}

export interface Response {
  code: number;
  msg?: string;
  data: any;
}

export interface PageResponse {
  index: number;
  size: number;
  total: number;
  pages: number;
  records: any[];
}

