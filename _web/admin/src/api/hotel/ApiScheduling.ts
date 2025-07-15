import BaseService from '@/api/common/baseService';
import http from '@/api/common/http';

const path = '/sys/scheduling';

class ApiScheduling extends BaseService {
  deleteByUserIds(ids: any[], success?: any) {
    http.delete({
        url: `${this.path}`,
        data: {
          userIds: ids,
        },
        success: success
      }
    );
  }
}

export default new ApiScheduling(path);
