import BaseService from '@/api/common/baseService';

const path = '/sys/ip-lock';

class ApiIpLock extends BaseService {}

export default new ApiIpLock(path);
