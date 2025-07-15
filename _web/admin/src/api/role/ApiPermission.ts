import BaseService from '@/api/common/baseService';

const path = '/sys/permission';

class ApiPermission extends BaseService {}

export default new ApiPermission(path);
