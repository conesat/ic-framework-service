import 'dart:async';

import 'package:app/common/global.dart';
import 'package:app/common/theme/dark_theme.dart';
import 'package:app/common/theme/light_theme.dart';
import 'package:app/pages/index.dart';
import 'package:app/pages/login.dart';
import 'package:app/pages/welcome.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:oktoast/oktoast.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:package_info_plus/package_info_plus.dart';

import 'common/utils/app_http_request.dart';

Future main() async {
  WidgetsFlutterBinding.ensureInitialized(); //不加这个强制横/竖屏会报错
  SystemChrome.setPreferredOrientations([
    // 强制竖屏
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown
  ]);
  GlobalData.pref = await SharedPreferences.getInstance();
  GlobalData.userId = GlobalData.pref!.getString('userId') ?? '';
  GlobalData.token = GlobalData.pref!.getString('token') ?? '';
  GlobalData.refreshToken = GlobalData.pref!.getString('refreshToken') ?? '';
  PackageInfo packageInfo = await PackageInfo.fromPlatform();
  GlobalData.appVersion = packageInfo.version;
  var nowVersionDate = 0;
  try {
    // 从 assets 目录中读取文件内容
    String content = await rootBundle.loadString('assets/version_date');
    nowVersionDate = int.parse(content);
    GlobalData.nowVersionDate = nowVersionDate;
  } catch (_) {}

  runApp(const MainPage());
}

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  bool onAd = true;
  bool loginFinish = false;

  @override
  void initState() {
    super.initState();
    if (GlobalData.refreshToken.isNotEmpty) {
      AppHttpRequest.getInstance().refreshToken(
          success: (Map<String, dynamic> r) {
        setState(() {
          loginFinish = true;
        });
      }, fail: () {
        reLogin();
      });
    } else {
      reLogin();
    }
  }

  void reLogin() {
    GlobalData.userId = '';
    GlobalData.token = "";
    GlobalData.refreshToken = "";
    setState(() {
      onAd = false;
    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // 获取系统的亮度模式
    final Brightness brightness = MediaQuery.of(context).platformBrightness;

    // 根据亮度模式选择主题
    final ThemeData theme = brightness == Brightness.dark
        ? DarkAppTheme.theme
        : LightAppTheme.theme;
    GlobalData.themeData = theme;
    return OKToast(
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: theme,
        home: Builder(builder: (context) {
          if (GlobalData.token.isEmpty) {
            return const LoginPage();
          }
          return Stack(
            children: [
              if (loginFinish) const IndexPage(),
              AnimatedSwitcher(
                duration: const Duration(milliseconds: 250),
                child: onAd
                    ? WelcomePage(
                        () {
                          setState(() {
                            loginFinish = true;
                            onAd = false;
                          });
                        },
                        key: const ValueKey(1),
                      )
                    : Container(
                        key: const ValueKey(2),
                      ),
              )
            ],
          );
        }),
      ),
    );
  }
}
