import 'dart:convert';

import '../../utils/app_http_request.dart';
import '../base_api.dart';
import '../query.dart';

Utf8Encoder encoder = const Utf8Encoder();

class ApiServerOrder extends BaseApi {
  static String url = "${BaseApi.url}/app/server-order";

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
