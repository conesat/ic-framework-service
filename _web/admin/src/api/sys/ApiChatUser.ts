import BaseService from '@/api/common/baseService';

const path = '/sys/chat-user';

class ApiChatUser extends BaseService {}

export default new ApiChatUser(path);
