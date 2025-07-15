import BaseService from '@/api/common/baseService';
import http, {RequestParam} from "@/api/common/http";

const path = '/sys/notice-receiver';

class ApiNoticeReceiver extends BaseService {
  addUserBatch(param: RequestParam) {
    http.post({
      url: `${path}/add-user-batch`,
      ...param
    });
  }

  deleteByNoticeIdAndUserIds(param: RequestParam) {
    http.delete(
      {
        url: `${path}`,
        ...param
      }
    );
  }
}

export default new ApiNoticeReceiver(path);
