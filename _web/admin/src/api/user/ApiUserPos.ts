import BaseService from '@/api/common/baseService';

const path = '/sys/user-pos';

class ApiUserPos extends BaseService {}

export default new ApiUserPos(path);
