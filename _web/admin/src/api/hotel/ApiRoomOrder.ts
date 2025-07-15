import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/room-order';

class ApiRoomOrder extends BaseService {
  roomNowOrder(roomId: any, param: RequestParam) {
    http.get({
      url: `${path}/room-now-order?roomId=${roomId}`,
      ...param
    });
  }
}

export default new ApiRoomOrder(path);
