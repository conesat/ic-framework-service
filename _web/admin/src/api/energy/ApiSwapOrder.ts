import BaseService from '@/api/common/baseService';

const path = '/sys/swap-order';

class ApiSwapOrder extends BaseService {}

export default new ApiSwapOrder(path);
