import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/role-menu';

class ApiRoleMenu extends BaseService {
  editBatch(param: RequestParam) {
    http.post({
      url: `${path}/edit-batch`,
      ...param
    });
  }
}

export default new ApiRoleMenu(path);
