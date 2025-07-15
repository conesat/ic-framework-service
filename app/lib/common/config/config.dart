import 'package:flutter/foundation.dart';

import '../../_config.dart';

class AppConfig {
  /// 服务器地址
  String serverUrl = '';
  String serverDomain = '';

  AppConfig(this.serverUrl, this.serverDomain);

  static AppConfig getAppConfig() {
    if (kDebugMode) {
      return AppConfig("$DEV_URL_PRIFIX$DEV_SERVER_URL", DEV_SERVER_URL);
    }

    return AppConfig("$SERVER_URL_PRIFIX$SERVER_URL", SERVER_URL);
  }
}
