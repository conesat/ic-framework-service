import 'dart:convert';

import '../../utils/app_http_request.dart';
import '../base_api.dart';

Utf8Encoder encoder = const Utf8Encoder();

class ApiAd extends BaseApi {
  static String url = "${BaseApi.url}/app/ad";
  static String publicUrl = "${BaseApi.url}/public/setting/app-ad";

  static void ad(
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().get(publicUrl, success: success, fail: fail);
  }
}
