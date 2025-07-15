import BaseService from '@/api/common/baseService';
import http,{RequestParam} from '@/api/common/http';

const path = '/sys/supporting';

class ApiSupporting extends BaseService {
  types(param: RequestParam) {
    http.get({
      url:`${path}/types`,
      ...param
    });
  }
}

export default new ApiSupporting(path);
