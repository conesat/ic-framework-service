import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/mine/notice';

class ApiMineNotice extends BaseService {
  read(id: any, param?: RequestParam) {
    http.put({
      url: `${path}/read?id=${id}`,
      ...param
    });
  }
}

export default new ApiMineNotice(path);
