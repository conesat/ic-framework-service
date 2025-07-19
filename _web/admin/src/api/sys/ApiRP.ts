import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/role-permission';

class ApiRP extends BaseService {
  editBatch(param: RequestParam) {
    http.post({
      url: `${path}/edit-batch`,
      ...param
    });
  }

  allPermission(roleId: any, param: RequestParam) {
    http.get({
      url: `${path}/all-permission/${roleId}`,
      ...param
    });
  }
}

export default new ApiRP(path);
