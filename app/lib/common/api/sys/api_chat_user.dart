import '../../utils/app_http_request.dart';
import '../base_api.dart';

class ApiChatUser extends BaseApi {
  static String url = "${BaseApi.url}/app/chat-user";

  static void all(String chatId,
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$url/all-user',
        data: {"chatId": chatId}, success: success, fail: fail);
  }
}
