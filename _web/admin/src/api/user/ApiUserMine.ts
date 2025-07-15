import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http.js';

const path = '/sys/mine/user';

class ApiUserMine extends BaseService {
  key(username: string, param: RequestParam) {
    http.get({
      url: `/public/user/key?username=${username}`,
      ...param
    });
  }

  mine(param: RequestParam) {
    http.get({
      url: `${path}/mine`,
      ...param
    });
  }

  editAvatar(param: RequestParam) {
    http.put({
      url: `${path}/edit-avatar`,
      ...param
    });
  }

  login(parameter: object): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      http.post({
        url: `/public/user/login`,
        data: parameter,
        success: resolve,
        fail: reject
      });
    });
  }

  logout(): Promise<any> {
    return new Promise((resolve, reject) => {
      http.post({
        url: `${path}/logout`,
        skipToken: true,
        success: resolve,
        fail: reject
      });
    });
  }

  refreshToken(): Promise<any> {
    return new Promise((resolve, reject) => {
      http.post({
        url: `${path}/refresh-token`,
        success: resolve,
        fail: reject
      });
    });
  }

  editMine(param: RequestParam) {
    http.put({
      url: `${this.path}/mine`,
      ...param
    });
  }

  updatePassword(param: RequestParam) {
    http.put({
      url: `${this.path}/update-passwd`,
      ...param
    });
  }
}

export default new ApiUserMine(path);
