import BaseService from '@/api/common/baseService';

const path = '/sys/chat-msg';

class ApiChatMsg extends BaseService {}

export default new ApiChatMsg(path);
