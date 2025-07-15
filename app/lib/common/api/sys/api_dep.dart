import 'dart:convert';

import 'package:app/common/api/query.dart';

import '../../utils/app_http_request.dart';
import '../base_api.dart';

Utf8Encoder encoder = const Utf8Encoder();

class ApiDep extends BaseApi {
  static String url = "${BaseApi.url}/app/org";

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
