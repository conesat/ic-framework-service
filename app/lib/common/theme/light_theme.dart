// 3.亮色主题
import 'package:app/common/theme/app_theme.dart';
import 'package:flutter/material.dart';

class LightAppTheme {
  static const ThemeMode themeMode = ThemeMode.light;

  static const MaterialColor mainColor = MaterialColor(
    0xFF6552BD,
    <int, Color>{
      50: Color(0xFFFFFFFF),
      100: Color(0xFFDADADA),
      200: Color(0xC0BEBEBE),
      300: Color(0xC0ADADAD),
      400: Color(0xC0919191),
      500: Color(0xC0707070),
      600: Color(0xC0525252),
      700: Color(0xFF484848),
      800: Color(0xFF3F3F3F),
      900: Color(0xFF212121),
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
      side: const BorderSide(color: AppTheme.primaryColor, width: 2),
      checkColor: WidgetStateProperty.all<Color>(mainColor.shade900), // 选中时勾选颜色
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(4), // 内边距2
      ),
    ),
    dialogTheme: const DialogTheme(
        surfaceTintColor: Colors.white,
        backgroundColor: Colors.white,
        titleTextStyle: TextStyle(
          color: Colors.black87,
          fontWeight: FontWeight.bold,
        ),
        contentTextStyle: TextStyle(
          color: Colors.black54,
        )),
    //进度条
    progressIndicatorTheme:
        const ProgressIndicatorThemeData(color: AppTheme.primaryColor),
    primarySwatch: mainColor,
    //模式 亮-黑
    brightness: Brightness.light,

    //主要突出颜色
    primaryColor: Colors.white,
    //高亮颜色 - 水波纹
    highlightColor: mainColor.shade900,
    //卡片颜色
    cardColor: const Color.fromRGBO(187, 187, 187, 1.0),
    secondaryHeaderColor: Colors.black54,
    //按钮波纹
    splashColor: AppTheme.primaryColor.withOpacity(0.2),
    primaryColorLight: Colors.white,
    colorScheme: ColorScheme.light(
      secondary: Colors.white.withOpacity(0.8),
    ),
    textButtonTheme: TextButtonThemeData(
        style: ButtonStyle(
            foregroundColor: WidgetStateProperty.all(Colors.white),
            overlayColor: WidgetStateProperty.all(AppTheme.primaryColor),
            padding: WidgetStateProperty.all(EdgeInsets.zero),
            backgroundColor:
                WidgetStateProperty.all(AppTheme.primaryColor.withOpacity(0.8)),
            textStyle: WidgetStateProperty.all(
                const TextStyle(color: Colors.white, fontSize: 14)))),
    buttonTheme: ButtonThemeData(
        splashColor: Colors.black87.withOpacity(0.1),
        buttonColor: Colors.black12,
        highlightColor: Colors.black),
    iconTheme: const IconThemeData(
      color: Colors.black87,
    ),
    textTheme: const TextTheme(
        bodyLarge: TextStyle(
      fontSize: 14,
    )),
    hintColor: Colors.white.withOpacity(0.8),
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
    primaryIconTheme: const IconThemeData(color: Colors.black87),
    inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.grey.withAlpha(40),
        // isCollapsed: true,
        contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
        hintStyle:
            TextStyle(color: Colors.black87.withOpacity(0.8), fontSize: 14),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(40), // 设置圆角
          borderSide: BorderSide.none, // 去掉边框线
        ),
        labelStyle: const TextStyle(color: Colors.black87, fontSize: 16),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(40), // 设置圆角
          borderSide: const BorderSide(color: AppTheme.errorColor, width: 1),
        )),
  );
}
