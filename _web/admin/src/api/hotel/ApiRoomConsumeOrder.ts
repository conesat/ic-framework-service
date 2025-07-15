import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/room-consume-order';

class ApiRoomConsumeOrder extends BaseService {
  createBatch(param: RequestParam) {
    http.postJson({
      url: `${path}/create-batch`,
      ...param
    });
  }
}

export default new ApiRoomConsumeOrder(path);
