import 'package:dio/dio.dart';

import '../../utils/app_http_request.dart';
import '../base_api.dart';

class ApiUserMine extends BaseApi {
  static String url = "${BaseApi.url}/app/mine/user";
  static String publicUrl = "${BaseApi.url}/public/user";

  static void login(String username, String password, String code,
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$publicUrl/app-login',
        data: {
          'username': username,
          'code': code,
          'passwd': password,
        },
        success: success,
        fail: fail);
  }

  static void key(String username,
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().get('$publicUrl/key',
        data: {
          'username': username,
        },
        success: success,
        fail: fail);
  }

  static void logout(
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance()
        .post('$url/logout', success: success, fail: fail);
  }

  static void updateInfo(
      {String? name,
      String? subTitle,
      String? pic,
      void Function(dynamic r)? success,
      void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().postForm('$url/update',
        data: {
          'subTitle': subTitle,
          'name': name,
          'pic': pic,
        },
        success: success,
        fail: fail);
  }

  static void myInfo(
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance()
        .get('$url/myInfo', success: success, fail: fail);
  }

  static void refreshToken(token,
      {void Function(dynamic r)? success, void Function(dynamic r)? fail}) {
    AppHttpRequest.getInstance().post('$url/refresh-token',
        options: Options(headers: {'Refresh-Token': token}),
        success: success,
        fail: fail);
  }
}
