import BaseService from '@/api/common/baseService';

const path = '/sys/time-scope';

class ApiTimeScope extends BaseService {}

export default new ApiTimeScope(path);
