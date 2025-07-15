import '../../utils/app_http_request.dart';
import '../base_api.dart';
import '../query.dart';

class ApiChatMsg extends BaseApi {
  static String url = "${BaseApi.url}/app/chat-msg";

  static void create(
      {required String chatId,
      required String message,
      required int msgType,
      void Function(dynamic r)? success,
      void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm(url,
        data: {
          "chatId": chatId,
          "message": message,
          "msgType": msgType,
        },
        success: success,
        fail: fail);
  }

  static void page(String chatId,
      {Query? query,
      void Function(dynamic r)? success,
      void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$url/page',
        data: {
          "chatId": chatId,
          ...query?.toJson() ?? {},
        },
        success: success,
        fail: fail);
  }
}
