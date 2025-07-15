import 'dart:convert';
import 'dart:ui';

import 'package:app/common/api/sys/api_mine.dart';
import 'package:app/common/global.dart';
import 'package:app/common/theme/app_theme.dart';
import 'package:app/common/utils/rsa_utils.dart';
import 'package:app/common/utils/ws.dart';
import 'package:app/pages/index.dart';
import 'package:crypto/crypto.dart';
import 'package:flutter/material.dart';
import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:oktoast/oktoast.dart';

import '../components/cache_video_player.dart';
import '../components/controller/video_payer_controller.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  late Animation<double> opacityAnim;
  bool checked = false;
  bool onLogin = false;
  bool onInputName = false;
  bool login = true;

  final VideoPlayController _videoPlayController = VideoPlayController();

  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwController = TextEditingController();
  String username = "";
  String passwd = "";
  bool _onLogin = false;
  bool _onInput = false;
  bool _beforeOnInput = false;
  bool _agreementChecked = false;
  String serviceAgreement = "";

  @override
  void initState() {
    super.initState();
    GlobalData.onLoginPage = true;
    ApiUserMine.logout(success: (r) {
      GlobalData.userId = "";
      GlobalData.tokenExp = 0;
      GlobalData.token = "";
      GlobalData.refreshToken = "";
      WS.close();
      GlobalData.pref!.remove('userId');
      GlobalData.pref!.setString('token', "");
      GlobalData.pref!.setString('refreshToken', "");
      GlobalData.pref!.setInt('tokenExp', 0);
      WidgetsBinding.instance.addObserver(this);
    });
  }

  @override
  void didChangeMetrics() {
    if (mounted) {
      final bottomInset = View.of(context).viewInsets.bottom;
      final isKeyboardVisible = bottomInset > 0.0;
      if (isKeyboardVisible != _beforeOnInput) {
        if (isKeyboardVisible) {
          _onInput = true;
        } else {
          _onInput = false;
        }
        _beforeOnInput = _onInput;
        setState(() {});
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: LayoutBuilder(builder: (context, cos) {
        return GestureDetector(
          onTap: () {
            _onInput = false;
            FocusScope.of(context).unfocus();
            setState(() {});
          },
          child: Center(
            child: Stack(
              children: [
                Positioned(
                    left: 0,
                    right: 0,
                    top: 0,
                    bottom: 0,
                    child: CachedVideoPlayer(
                      videoUrl:
                          "https://static.newcger.com//uploads/preview/short/2025/01/21/%E6%B5%B7%E8%BE%B9-%E5%A4%A7%E6%B5%B7-%E6%B5%B7%E6%B5%AA-%E6%B5%B7%E5%B2%B8-%E6%B5%AA%E8%8A%B1-%E8%87%AA%E7%84%B6%E9%A3%8E%E6%99%AF-%E7%AB%96%E5%B1%8F2.mp4",
                      onVideoChanged: (int position, int duration) {},
                      loop: true,
                      videoPlayController: _videoPlayController,
                    )),
                Positioned(
                  left: 0,
                  top: 0,
                  right: 0,
                  bottom: 0,
                  child: Container(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        colors: [
                          GlobalData.themeData!.primaryColor.withAlpha(0),
                          GlobalData.themeData!.primaryColor.withAlpha(255),
                        ],
                      ),
                    ),
                  ),
                ),
                Positioned(
                  left: 0,
                  top: 0,
                  right: 0,
                  bottom: 0,
                  child: SingleChildScrollView(
                    child: ConstrainedBox(
                      constraints: BoxConstraints(
                        minHeight: cos.maxHeight,
                      ),
                      child: IntrinsicHeight(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Column(
                              children: [
                                SizedBox(height: 120),
                                Text(
                                  "Welcome",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 30),
                                ),
                                SizedBox(height: 20),
                                Text("欢迎使用IcFramework"),
                                SizedBox(height: 20),
                              ],
                            ),
                            const Spacer(),
                            Builder(builder: (_) {
                              if (onLogin) {
                                return const Padding(
                                    padding: EdgeInsets.symmetric(
                                        horizontal: 30, vertical: 10),
                                    child: SizedBox(
                                        height: 20,
                                        width: 20,
                                        child: CircularProgressIndicator()));
                              }
                              return Container(
                                width: double.maxFinite,
                                constraints:
                                    const BoxConstraints(maxWidth: 300),
                                child: Form(
                                  child: Column(children: [
                                    buildFormInput(Row(
                                      children: [
                                        const Icon(
                                          Icons.person,
                                          size: 16,
                                        ),
                                        const SizedBox(
                                          width: 10,
                                        ),
                                        Expanded(
                                          flex: 1,
                                          child: TextFormField(
                                            controller: _usernameController,
                                            maxLength: 20,
                                            decoration: const InputDecoration(
                                                contentPadding:
                                                    EdgeInsets.zero, // 去掉边距
                                                filled: false,
                                                hintText: "请输入用户名",
                                                counterText: ""),
                                            onChanged: (value) {
                                              setState(() {
                                                username = value;
                                              });
                                            },
                                          ),
                                        ),
                                      ],
                                    )),
                                    const SizedBox(height: 30),
                                    buildFormInput(Row(
                                      children: [
                                        const Icon(
                                          Icons.lock,
                                          size: 16,
                                        ),
                                        const SizedBox(
                                          width: 10,
                                        ),
                                        Expanded(
                                          flex: 1,
                                          child: TextFormField(
                                            controller: _passwController,
                                            obscureText: true,
                                            maxLength: 18,
                                            decoration: const InputDecoration(
                                                contentPadding:
                                                    EdgeInsets.zero, // 去掉边距
                                                filled: false,
                                                hintText: "请输入密码",
                                                counterText: ""),
                                            onChanged: (value) {
                                              setState(() {
                                                passwd = value;
                                              });
                                            },
                                          ),
                                        ),
                                      ],
                                    )),
                                    const SizedBox(height: 50),
                                    Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Checkbox(
                                          value: _agreementChecked,
                                          onChanged: (value) {
                                            setState(() {
                                              _agreementChecked = value!;
                                            });
                                          },
                                        ),
                                        Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.center,
                                          children: [
                                            const Text("同意"),
                                            GestureDetector(
                                              onTap: () {
                                                // TODO: 打开用户协议页面
                                              },
                                              child: const Text(
                                                "《用户协议》",
                                                style: TextStyle(
                                                  color: AppTheme.primaryColor,
                                                ),
                                              ),
                                            ),
                                            const Text("与"),
                                            GestureDetector(
                                              onTap: () {
                                                // TODO: 打开隐私政策页面
                                              },
                                              child: const Text(
                                                "《隐私政策》",
                                                style: TextStyle(
                                                  color: AppTheme.primaryColor,
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: 20),
                                    AnimatedSwitcher(
                                      duration:
                                          const Duration(milliseconds: 300),
                                      child: loginButton(context),
                                    ),
                                  ]),
                                ),
                              );
                            }),
                            const SizedBox(height: 40),
                          ],
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        );
      }),
    );
  }

  ClipRRect buildFormInput(Row row) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(16),
      child: BackdropFilter(
        filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          decoration: BoxDecoration(
            color: AppTheme.activeTextColor.withAlpha(20),
            borderRadius: BorderRadius.circular(16),
          ),
          child: row,
        ),
      ),
    );
  }

  SizedBox loginButton(BuildContext context) {
    return username.isEmpty || passwd.isEmpty || _onLogin || !_agreementChecked
        ? SizedBox(
            key: const ValueKey(2),
            width: double.maxFinite,
            child: TextButton(
                onPressed: () {},
                style: ButtonStyle(
                  shape: WidgetStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16), // 设置圆角半径
                    ),
                  ),
                  backgroundColor: WidgetStateProperty.resolveWith<Color?>(
                      (Set<WidgetState> states) {
                    return AppTheme.cardColor.withAlpha((0.4 * 255).round());
                  }),
                  overlayColor: WidgetStateProperty.resolveWith<Color?>(
                      (Set<WidgetState> states) {
                    return AppTheme.cardColor.withAlpha((0.4 * 255).round());
                  }),
                ),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 40),
                  child: SizedBox(
                    height: 50,
                    child: Center(
                      child: _onLogin
                          ? const SizedBox(
                              height: 16,
                              width: 16,
                              child: CircularProgressIndicator())
                          : const Text("登  录",
                              style: TextStyle(
                                  fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                  ),
                )))
        : SizedBox(
            key: const ValueKey(1),
            width: double.maxFinite,
            child: TextButton(
                onPressed: () {
                  doLogin(context);
                },
                child: const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 40),
                  child: SizedBox(
                    height: 50,
                    child: Center(
                      child: Text("登  录",
                          style: TextStyle(
                              fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                  ),
                )),
          );
  }

  void doLogin(BuildContext context) {
    if (username.isEmpty || passwd.isEmpty || _onLogin || !_agreementChecked) {
      return;
    }
    setState(() {
      _onLogin = true;
    });
    // 处理表单提交逻辑
    ApiUserMine.key(username, success: (data) {
      String md5_1 = md5.convert(utf8.encode(username + passwd)).toString();
      String md5_2 = md5.convert(utf8.encode(username + md5_1)).toString();
      ApiUserMine.login(username, encryptRsa(data, md5_2), "", success: (r) {
        if (GlobalData.pref != null) {
          GlobalData.pref!.setString('userId', r['id']);
        }
        GlobalData.userId = r['id'];
        GlobalData.token = r['token'];
        GlobalData.refreshToken = r['refreshToken'];
        Map<String, dynamic> decodedToken = JwtDecoder.decode(GlobalData.token);
        GlobalData.pref!.setString('token', GlobalData.token);
        GlobalData.pref!.setString('refreshToken', GlobalData.refreshToken);
        GlobalData.pref!.setInt('tokenExp', decodedToken['exp']);
        GlobalData.tokenExp = decodedToken['exp'];
        GlobalData.onLoginPage = false;
        Navigator.pushReplacement(context,
            MaterialPageRoute(builder: (context) => const IndexPage()));
      }, fail: (r) {
        showToast(r['msg']);
        setState(() {
          _onLogin = false;
        });
      });
    }, fail: (r) {
      showToast(r['msg']);
      setState(() {
        _onLogin = false;
      });
    });
  }
}
