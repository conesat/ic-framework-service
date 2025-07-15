import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/online-user';

class ApiOnlineUser extends BaseService {
  pageMine(param: RequestParam) {
    http.post({
      url: `${path}/page-mine`,
      ...param
    });
  }

  deleteMine(param: RequestParam) {
    http.delete({
        url: `${this.path}/mine`,
        ...param
      }
    );
  }
}

export default new ApiOnlineUser(path);
