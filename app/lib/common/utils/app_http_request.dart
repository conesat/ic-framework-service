import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:app/common/api/response_codes.dart';
import 'package:app/common/api/sys/api_mine.dart';
import 'package:app/common/utils/ws.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:cookie_jar/cookie_jar.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:dio/dio.dart';
import 'package:dio/io.dart';
import 'package:dio_cookie_manager/dio_cookie_manager.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:oktoast/oktoast.dart';

import '../../main.dart';
import '../api/base_api.dart';
import '../config/config.dart';
import '../global.dart';

class AppHttpRequest {
  static final String _baseUrl = AppConfig.getAppConfig().serverUrl;
  final Duration connetTimeOut = const Duration(seconds: 10);
  final Duration receivetTimeOut = const Duration(seconds: 10);
  bool onShowDialog = false;

  //单例模式
  static AppHttpRequest? _instance;
  late Dio _dio;
  late BaseOptions _options;

  //单例模式，只创建一次实例
  static AppHttpRequest getInstance() {
    if (null == _instance) {
      _instance = AppHttpRequest();
      return _instance!;
    } else {
      return _instance!;
    }
  }

  //构造函数
  AppHttpRequest() {
    // 创建一个 HttpClient 实例并设置证书验证为 false
    _options = BaseOptions(
        //连接时间为5秒
        connectTimeout: connetTimeOut,
        //响应时间为3秒
        receiveTimeout: receivetTimeOut,
        //设置请求头
        headers: {
          "Platform": "APP ${GlobalData.appVersion}",
        },
        //默认值是"application/json; charset=utf-8",Headers.formUrlEncodedContentType会自动编码请求体.
        // contentType: Headers.jsonContentType,
        //共有三种方式json,bytes(响应字节),stream（响应流）,plain
        responseType: ResponseType.json);
    _setUserAgent();
    _dio = Dio(_options);
    (_dio.httpClientAdapter as IOHttpClientAdapter).createHttpClient = () {
      final client = HttpClient();
      client.badCertificateCallback =
          (X509Certificate cert, String host, int port) {
        return true;
      };
      return client;
    };
    //设置Cookie
    _dio.interceptors.add(CookieManager(CookieJar()));

    //添加拦截器
    _dio.interceptors.add(InterceptorsWrapper(
        onRequest: (RequestOptions options, RequestInterceptorHandler handler) {
      if (options.path.startsWith(_baseUrl) && GlobalData.token != "") {
        options.headers.putIfAbsent("Authorization", () => GlobalData.token);
        options.headers
            .putIfAbsent("app-version", () => GlobalData.nowVersionDate);
      }
      // 在请求之前的拦截信息
      return handler.next(options);
    }, onResponse:
            (Response<dynamic> response, ResponseInterceptorHandler handler) {
      // 在响应之前的拦截信息
      return handler.next(response);
    }, onError: (DioException e, ErrorInterceptorHandler handler) {
      //在错误之前的拦截信息
      return handler.next(e);
    }));
  }

  // 添加这些新的字段
  bool _isRefreshing = false;
  final List<Function> _refreshQueue = [];

  Future<void> _retryRequest(Function retryFunction) async {
    _refreshQueue.add(retryFunction);
    if (!_isRefreshing) {
      _isRefreshing = true;
      try {
        await refreshToken(
          success: (r) {
            for (var callback in _refreshQueue) {
              callback();
            }
            _refreshQueue.clear();
          },
          fail: () {
            _refreshQueue.clear();
            toLogin();
          },
        );
      } finally {
        _isRefreshing = false;
      }
    }
  }

  Future<void> _setUserAgent() async {
    final deviceInfo = DeviceInfoPlugin();
    String userAgent = "Unknown";
    if (Platform.isIOS) {
      final iosInfo = await deviceInfo.iosInfo;
      userAgent = "iOS ${iosInfo.systemVersion}";
    } else if (Platform.isAndroid) {
      final androidInfo = await deviceInfo.androidInfo;
      userAgent = "Android ${androidInfo.version.release}";
    }
    _options.headers["User-Agent"] = userAgent;
  }

  //get请求方法
  Future<void> get(String url,
      {data,
      options,
      void Function(dynamic response)? success,
      void Function(dynamic r)? fail}) async {
    try {
      Response response =
          await _dio.get(url, queryParameters: data, options: options);
      if (!handlerResponse(url, response, success, fail)) {
        await _retryRequest(() => get(url,
            data: data, options: options, success: success, fail: fail));
      }
    } on DioException catch (e) {
      if (e.response?.statusCode == Codes.codeTokenOutTime ||
          e.response?.statusCode == Codes.codeNeedLogin) {
        await _retryRequest(() => get(url,
            data: data, options: options, success: success, fail: fail));
        return;
      }
      handlerErr(url, e, fail);
    }
  }

  //post请求
  Future<void> post(String url,
      {params,
      Options? options,
      void Function(dynamic response)? success,
      void Function(dynamic r)? fail}) async {
    try {
      Response response =
          await _dio.post(url, queryParameters: params, options: options);
      if (!handlerResponse(url, response, success, fail)) {
        await _retryRequest(() => post(url,
            params: params, options: options, success: success, fail: fail));
      }
    } on DioException catch (e) {
      handlerErr(url, e, fail);
    }
  }

  //post Form请求
  Future<void> postForm(String url,
      {data,
      options,
      void Function(dynamic response)? success,
      void Function(dynamic r)? fail}) async {
    Response response;
    try {
      response =
          await _dio.post(url, options: options, data: FormData.fromMap(data));
      if (!handlerResponse(url, response, success, fail)) {
        await _retryRequest(() => postForm(url,
            data: data, options: options, success: success, fail: fail));
      }
    } on DioException catch (e) {
      handlerErr(url, e, fail);
    }
  }

  Future<void> refreshToken(
      {Function(Map<String, dynamic>)? success, Function? fail}) async {
    Completer<void> completer = Completer();

    ApiUserMine.refreshToken(GlobalData.refreshToken, success: (r) {
      GlobalData.token = r['token'];
      GlobalData.refreshToken = r['refreshToken'];
      Map<String, dynamic> decodedToken = JwtDecoder.decode(GlobalData.token);
      GlobalData.userId = decodedToken['sub'];
      GlobalData.pref!.setString('token', GlobalData.token);
      GlobalData.pref!.setString('refreshToken', GlobalData.refreshToken);
      GlobalData.pref!.setInt('tokenExp', decodedToken['exp']);
      GlobalData.pref!.setString('userId', GlobalData.userId);
      GlobalData.tokenExp = decodedToken['exp'];
      WS.connect();
      if (success != null) {
        success(r);
      }
      completer.complete();
    }, fail: (r) {
      if (fail != null) {
        fail();
      } else {
        showToast(r["msg"], position: ToastPosition.bottom);
        toLogin();
      }
      completer.complete();
    });

    return completer.future;
  }

  void toLogin() {
    if (GlobalData.onLoginPage) {
      return;
    }
    GlobalData.onLoginPage = true;
    GlobalData.userId = "";
    GlobalData.tokenExp = 0;
    GlobalData.token = "";
    WS.close();
    GlobalData.pref!.setString('userId', "");
    GlobalData.pref!.setString('token', "");
    GlobalData.pref!.setInt('tokenExp', 0);
    if (GlobalData.context != null) {
      try {
        showToast("登录凭证过期，请重新登录");
        Navigator.pushReplacement(GlobalData.context!,
            MaterialPageRoute(builder: (context) => const MainPage()));
      } catch (e) {
        debugPrint(e.toString());
      }
    }
  }

  void handlerErr(String url, DioException e, void Function(dynamic r)? fail) {
    if (e.response != null &&
        (e.response!.statusCode == Codes.codeNeedLogin ||
            e.response!.statusCode == Codes.codeTokenOutTime ||
            e.response!.statusCode == Codes.codeOtherLogin ||
            e.response!.statusCode == Codes.codeSystemUninitialized ||
            e.response!.statusCode == Codes.codeSystemNotActive ||
            e.response!.statusCode == Codes.codeSystemOutDate)) {
      showToast("登录过期，请重新登录", position: ToastPosition.bottom);
      refreshToken();
      // toLogin();
    } else {
      String err = formatError(url, e);
      if (fail != null) {
        fail({"code": 400, "msg": e.message});
      } else {
        showErr(err);
      }
    }
  }

  bool handlerResponse(
      String url,
      Response<dynamic> response,
      void Function(dynamic response)? success,
      void Function(dynamic r)? fail) {
    if (url.startsWith(BaseApi.url)) {
      var data = response.data;
      if (data is String) {
        data = jsonDecode(data);
      }
      var statusCode = data['code'];
      if (statusCode != null && statusCode is int && statusCode == 0) {
        if (success != null) {
          success(data['data']);
        }
        if (!url.endsWith("/user/login") &&
            !url.endsWith("/user/refresh-token") &&
            (GlobalData.userId != "" &&
                GlobalData.tokenExp <
                    DateTime.now().millisecondsSinceEpoch ~/ 1000 -
                        60 * 60 * 24)) {
          ApiUserMine.refreshToken(GlobalData.refreshToken, success: (r) {
            GlobalData.token = r['token'];
            GlobalData.refreshToken = r['refreshToken'];
            Map<String, dynamic> decodedToken =
                JwtDecoder.decode(GlobalData.token);
            GlobalData.userId = decodedToken['sub'];
            GlobalData.pref!.setString('token', GlobalData.token);
            GlobalData.pref!.setString('refreshToken', GlobalData.refreshToken);
            GlobalData.pref!.setInt('tokenExp', decodedToken['exp']);
            GlobalData.pref!.setString('userId', GlobalData.userId);
            GlobalData.tokenExp = decodedToken['exp'];
            if (success != null) {
              success(r);
            }
          });
        }
      } else if (statusCode != null &&
          !url.endsWith("/user/refresh-token") &&
          (statusCode == Codes.codeNeedLogin ||
              statusCode == Codes.codeTokenOutTime ||
              statusCode == Codes.codeOtherLogin ||
              statusCode == Codes.codeSystemUninitialized ||
              statusCode == Codes.codeSystemNotActive ||
              statusCode == Codes.codeSystemOutDate)) {
        // 返回false告诉外面要刷新Token了
        return false;
      } else if (fail != null) {
        fail(data);
      } else {
        showToast(data['msg'], position: ToastPosition.bottom);
      }
    } else if (success != null) {
      success(response);
    }
    return true;
  }

  //下载文件
  downLoadFile(String urlPath, String savePath) async {
    Response response;
    try {
      response = await _dio.download(urlPath, savePath,
          onReceiveProgress: (int count, int total) {
        printLog('$count $total');
      });
      printLog('downLoadFile response: $response');
      return response;
    } on DioException catch (e) {
      printLog('downLoadFile exception: $e');
      showToast(formatError(urlPath, e), position: ToastPosition.bottom);
    }
    return null;
  }

  String formatError(String url, DioException e) {
    if (e.type == DioExceptionType.connectionTimeout) {
      printLog("$url:连接超时");
      return "连接超时";
    } else if (e.type == DioExceptionType.sendTimeout) {
      printLog("$url:请求超时");
      return "请求超时";
    } else if (e.type == DioExceptionType.receiveTimeout) {
      printLog("$url:响应超时");
      return "响应超时";
    } else if (e.type == DioExceptionType.badResponse) {
      printLog("$url:出现异常:${e.message}");
      return "出现异常:${e.message}";
    } else if (e.type == DioExceptionType.cancel) {
      printLog("$url:请求取消");
      return "请求取消";
    } else if (e.type == DioExceptionType.connectionError) {
      printLog("$url:连接失败");
      testLink();
      return "连接失败";
    } else {
      printLog("$url:未知错误${e.error}");
      return "未知错误";
    }
  }

  void testLink({BuildContext? context}) async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (!connectivityResult.contains(ConnectivityResult.mobile) &&
        !connectivityResult.contains(ConnectivityResult.wifi) &&
        !connectivityResult.contains(ConnectivityResult.ethernet) &&
        !connectivityResult.contains(ConnectivityResult.vpn) &&
        !connectivityResult.contains(ConnectivityResult.other)) {
      if (context != null || GlobalData.context != null) {
        if (onShowDialog) {
          return;
        }
        onShowDialog = true;
        await showDialog(
            context: context ?? GlobalData.context!,
            builder: (BuildContext context) {
              return const AlertDialog(
                  content: Text('没有连接到互联网', textAlign: TextAlign.center));
            });
        onShowDialog = false;
      }
      return;
    }
    if (onShowDialog) {
      return;
    }
    try {
      await _dio.post('${BaseApi.url}/dxk/dic/item',
          data: FormData.fromMap({"key": ""}));
    } on DioException catch (e) {
      if (e.type == DioExceptionType.connectionError &&
          (context != null || GlobalData.context != null)) {
        if (onShowDialog) {
          return;
        }
        onShowDialog = true;
        showDialog(
            barrierDismissible: false,
            context: context ?? GlobalData.context!,
            builder: (BuildContext context) {
              return const PopScope(
                canPop: false,
                child: AlertDialog(
                    content: Text(
                  '系统维护中，请稍后再来吧',
                  textAlign: TextAlign.center,
                )),
              );
            });
      }
    }
  }

  printLog(String s) {
    if (kDebugMode) {
      print(s);
    }
  }

  void showErr(String? message) {
    showToast(message ?? '请求失败',
        textStyle: const TextStyle(color: Colors.red),
        position: ToastPosition.bottom);
  }
}
