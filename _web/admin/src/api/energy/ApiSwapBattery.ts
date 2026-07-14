import BaseService from '@/api/common/baseService';

const path = '/sys/swap-battery';

class ApiSwapBattery extends BaseService {}

export default new ApiSwapBattery(path);
