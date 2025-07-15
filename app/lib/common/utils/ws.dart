import 'dart:async';
import 'dart:convert' as convert;

import 'package:app/main.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import '../config/config.dart';
import '../global.dart';
import '../theme/app_theme.dart';

class WS {
  static StreamSubscription? subscription;
  static WebSocketChannel? channel;
  static bool connectState = false;
  static bool autoReconnectState = false;
  static Timer? _timer;

  static connect({bool reconnect = false}) async {
    final wsUrl = Uri.parse(
        'ws://${AppConfig.getAppConfig().serverDomain}/ws?token=${GlobalData.token}');
    channel = WebSocketChannel.connect(wsUrl);
    try {
      await channel!.ready;
    } catch (e) {
      print(e);
      return;
    }
    try {
      subscription = channel!.stream.listen((event) {
        var jsonDecode = convert.jsonDecode(event);
        if (jsonDecode['type'] == "login") {
          if (jsonDecode['message'] != GlobalData.token) {
            showDialog(
              context: GlobalData.context!,
              barrierDismissible: false,
              builder: (BuildContext context) {
                return AlertDialog(
                  surfaceTintColor: Colors.black,
                  title: const Text(
                    '提示',
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                  content: const SingleChildScrollView(
                    child: Text('你的账号在别处登录。'),
                  ),
                  actions: <Widget>[
                    TextButton(
                      style: ButtonStyle(
                        backgroundColor:
                            WidgetStateProperty.all(AppTheme.errorColor),
                        shape: WidgetStateProperty.all(
                          RoundedRectangleBorder(
                              borderRadius:
                                  BorderRadius.circular(AppTheme.borderRadius)),
                        ),
                      ),
                      child: const Text(
                        '重新登录',
                        style: TextStyle(color: Colors.white),
                      ),
                      onPressed: () {
                        GlobalData.userId = "";
                        GlobalData.tokenExp = 0;
                        GlobalData.token = "";
                        WS.close();
                        GlobalData.pref!.setString('userId', "");
                        GlobalData.pref!.setString('token', "");
                        GlobalData.pref!.setInt('tokenExp', 0);
                        Navigator.pushReplacement(
                            context,
                            MaterialPageRoute(
                                builder: (context) => const MainPage()));
                      },
                    )
                  ],
                );
              },
            );
          }
        }
      }, onError: (error) {
        //连接错误时调用
        print("服务器连接错误");
        connectState = false;
        subscription!.cancel();
      }, onDone: () {
        //关闭时调用
        connectState = false;
        print("服务器已关闭");
        subscription!.cancel();
      }, cancelOnError: true);
      if (!reconnect) {
        if (_timer != null) {
          _timer!.cancel();
        }
        _timer = Timer.periodic(const Duration(seconds: 15), (timer) {
          if (connectState) {
            sendMessage("", "", "heart");
          } else {
            connect(reconnect: true);
          }
        });
      }
      connectState = true;

      if (!autoReconnectState) {
        autoReconnectState = true;
        autoReconnect();
      }
    } catch (e) {
      if (subscription != null) {
        subscription!.cancel();
      }
      connectState = false;
    }
  }

  static void close() {
    if (channel != null) {
      channel!.sink.close();
    }
  }

  static void sendMessage(String message, String toUserId, String type) {
    channel!.sink.add(convert.jsonEncode({
      'userId': GlobalData.userId,
      'toUserId': toUserId,
      'message': message,
      'time': DateTime.now().millisecondsSinceEpoch,
      'type': type
    }));
  }

  // 定时重连
  static void autoReconnect() async {
    Timer.periodic(const Duration(seconds: 30), (timer) async {
      if (!connectState) {
        connect(reconnect: true);
      }
    });
  }
}
