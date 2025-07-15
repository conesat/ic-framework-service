import BaseService from '@/api/common/baseService';

const path = '/sys/permission-group';

class ApiPermissionGroup extends BaseService {}

export default new ApiPermissionGroup(path);
