// 3.暗黑主题
import 'package:app/common/theme/app_theme.dart';
import 'package:flutter/material.dart';

class DarkAppTheme {
  static const ThemeMode themeMode = ThemeMode.dark;

  static const MaterialColor mainColor = MaterialColor(
    0xFF6552BD,
    <int, Color>{
      50: Color(0xFF000000),
      100: Color(0xFF252525),
      200: Color(0xC0494949),
      300: Color(0xC07A7A7A),
      400: Color(0xC0888888),
      500: Color(0xC0989898),
      600: Color(0xC0BBBBBB),
      700: Color(0xFFBDBDBD),
      800: Color(0xFFD7D7D7),
      900: Color(0xFFF1F1F1),
    },
  );
  static final ThemeData theme = ThemeData(
    checkboxTheme: CheckboxThemeData(
      fillColor: WidgetStateProperty.resolveWith<Color?>(
        (Set<WidgetState> states) {
          if (states.contains(WidgetState.selected)) {
            return AppTheme.primaryColor; // 选中时背景白色
          }
          return Colors.transparent; // 未选中时背景黑色
        },
      ),
      materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
      side: const BorderSide(color: AppTheme.primaryColor, width: 2),
      checkColor: WidgetStateProperty.all<Color>(mainColor.shade900),
      // 选中时勾选颜色
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(4), // 内边距2
      ),
    ),
    dialogTheme: const DialogTheme(
        surfaceTintColor: Colors.black87,
        backgroundColor: Colors.black87,
        titleTextStyle: TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
        contentTextStyle: TextStyle(
          color: Colors.white70,
        )),
    //进度条
    progressIndicatorTheme:
        const ProgressIndicatorThemeData(color: AppTheme.primaryColor),
    primarySwatch: mainColor,
    //模式 亮-黑
    brightness: Brightness.dark,

    //主要突出颜色
    primaryColor: Colors.black,
    secondaryHeaderColor: Colors.white60,
    //高亮颜色 - 水波纹
    highlightColor: mainColor.shade900,
    //卡片颜色
    cardColor: const Color.fromRGBO(73, 73, 73, 1.0),

    //按钮波纹
    splashColor: AppTheme.primaryColor.withOpacity(0.2),

    primaryColorLight: Colors.black87,
    colorScheme: ColorScheme.dark(
      secondary: Colors.black87.withOpacity(0.8),
    ),
    textButtonTheme: TextButtonThemeData(
        style: ButtonStyle(
            foregroundColor: WidgetStateProperty.all(Colors.white),
            overlayColor: WidgetStateProperty.all(AppTheme.primaryColor),
            padding: WidgetStateProperty.all(EdgeInsets.zero),
            backgroundColor:
                WidgetStateProperty.all(AppTheme.primaryColor.withOpacity(0.8)),
            textStyle: WidgetStateProperty.all(
                const TextStyle(color: Colors.black, fontSize: 14)))),
    buttonTheme: ButtonThemeData(
        splashColor: Colors.white.withOpacity(0.1),
        buttonColor: Colors.white60,
        highlightColor: Colors.white),
    iconTheme: const IconThemeData(
      color: Colors.white,
    ),
    textTheme: const TextTheme(
        bodyLarge: TextStyle(
      fontSize: 14,
    )),
    hintColor: Colors.black87.withOpacity(0.8),
    scaffoldBackgroundColor: mainColor.shade50,
    appBarTheme: AppBarTheme(
        surfaceTintColor: mainColor.shade100,
        elevation: 0.0,
        shadowColor: mainColor.shade50.withAlpha(60),
        backgroundColor: mainColor.shade50,
        titleTextStyle: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: mainColor.shade900)),
    primaryIconTheme: const IconThemeData(color: Colors.white),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: Colors.grey.withAlpha(40),
      // isCollapsed: true,
      contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
      hintStyle: TextStyle(color: Colors.white.withOpacity(0.8), fontSize: 14),
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(40), // 设置圆角
        borderSide: BorderSide.none, // 去掉边框线
      ),
      labelStyle: const TextStyle(color: Colors.white, fontSize: 16),
    ),
  );
}
