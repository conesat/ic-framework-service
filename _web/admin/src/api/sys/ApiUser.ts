import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/user';

class ApiUser extends BaseService {
  key(username: any, param: RequestParam) {
    http.get({
      url: `/public/user/key?username=${username}`,
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

  refreshToken(): Promise<any> {
    return new Promise((resolve, reject) => {
      http.post({
        url: `${path}/refreshToken`, success: resolve, fail: reject
      });
    });
  }

}

export default new ApiUser(path);

