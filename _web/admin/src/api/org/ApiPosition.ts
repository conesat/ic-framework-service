import BaseService from '@/api/common/baseService';

const path = '/sys/position';

class ApiPosition extends BaseService {}

export default new ApiPosition(path);
