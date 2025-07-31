import BaseService from '@/api/common/baseService';

const path = '/sys/update-history';

class ApiUpdateHistory extends BaseService {}

export default new ApiUpdateHistory(path);
