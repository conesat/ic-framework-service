import '../../utils/app_http_request.dart';
import '../base_api.dart';
import '../query.dart';

class ApiChat extends BaseApi {
  static String url = "${BaseApi.url}/app/chat";

  static void createSysSingle(userId,
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance()
        .post('$url/create-sys-single/$userId', success: success, fail: fail);
  }

  static void page(
      {Query? query,
      void Function(dynamic r)? success,
      void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$url/page',
        data: {
          ...query?.toJson() ?? {},
        },
        success: success,
        fail: fail);
  }
}
