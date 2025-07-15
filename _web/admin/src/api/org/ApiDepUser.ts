import BaseService from '@/api/common/baseService';
import http, { PageRequestParam, RequestParam } from '@/api/common/http';

const path = '/sys/dep-user';

class ApiDepUser extends BaseService {
  setManager(param: RequestParam) {
    http.post({
      url: `${path}/manager/${param.data.id}`,
      ...param
    });
  }

  pageDetail(param: PageRequestParam) {
    http.post({
      url: `${path}/page-detail`,
      ...param
    });
  }
}

export default new ApiDepUser(path);
