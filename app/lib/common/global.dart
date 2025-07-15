import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'entity/user.dart';

class GlobalData {
  static User account = User(); // 账号信息

  static SharedPreferences? pref;
  static BuildContext? context;
  static String userId = "";
  static String token = "";
  static String refreshToken = "";
  static int tokenExp = 0;
  static ThemeData? themeData;
  static int nowVersionDate = 0;
  static String appVersion = '';
  static bool onLoginPage = false;
}
