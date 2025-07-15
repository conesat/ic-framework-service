import BaseService from '@/api/common/baseService';
import http, {RequestParam} from '@/api/common/http';

const path = '/sys/room';

class ApiRoom extends BaseService {
  roomStatusEnums(param: RequestParam) {
    http.get({
      url: `${path}/room-status-enums`,
      ...param
    });
  }

  roomStatus(param: RequestParam) {
    http.get({
      url: `${path}/room-status`,
      ...param
    });
  }

  buildRoom(param: RequestParam) {
    http.post({
      url: `${path}/all`,
      ...param
    });
  }

  updatePrice(param: RequestParam) {
    http.put({
      url: `${path}/update-price`,
      ...param
    });
  }

  updateState(param: RequestParam) {
    http.put({
      url: `${path}/update-status`,
      ...param
    });
  }

  updateExtKey(param: RequestParam) {
    http.put({
      url: `${path}/update-ext-key`,
      ...param
    });
  }

  statics(param: RequestParam) {
    http.get({
      url: `${path}/statics`,
      ...param
    });
  }

  detailByExtKey(param: RequestWithExtKeyParam) {
    http.get({
      url: `${path}/detail-by-ext-key/${param.extKey}`,
      ...param
    });
  }
}
export interface RequestWithExtKeyParam extends RequestParam{
  // 外部key
  extKey: string;
}

export default new ApiRoom(path);
