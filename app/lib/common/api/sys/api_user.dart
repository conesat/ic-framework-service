import 'dart:convert';
import 'dart:ffi';

import '../../utils/app_http_request.dart';
import '../base_api.dart';
import '../query.dart';

Utf8Encoder encoder = const Utf8Encoder();

class ApiUser extends BaseApi {
  static String url = "${BaseApi.url}/app/user";

  static void page(
      {Query? query,
      List<Long>? depIds,
      List<Long>? posIds,
      void Function(dynamic r)? success,
      void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$url/page',
        data: {
          "depId": depIds,
          "posIds": posIds,
          ...query?.toJson() ?? {},
        },
        success: success,
        fail: fail);
  }
}
