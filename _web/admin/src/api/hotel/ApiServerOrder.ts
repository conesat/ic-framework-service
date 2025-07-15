import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/server-order';

class ApiServerOrder extends BaseService {
  stateEnums(param: RequestParam) {
    http.get({
      url: `/public/server-order/state-enums`,
      ...param
    });
  }

  targetEnums(param: RequestParam) {
    http.get({
      url: `/public/server-order/target-enums`,
      ...param
    });
  }

  assign(param: RequestParam) {
    http.put({
        url: `${this.path}/assign`,
        ...param
      }
    );
  }
}

export default new ApiServerOrder(path);
