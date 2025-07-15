import 'dart:math';

class NumberUtil {
  static List<String> formatChar(double n) {
    var fixed = n.toStringAsFixed(2);
    var split = fixed.split(".");
    var prefix = '';
    for (int i = split[0].length - 1, j = 1; i >= 0; i--, j++) {
      prefix = split[0][i] + prefix;
      if (i > 0 && j % 3 == 0) {
        prefix = ',$prefix';
      }
    }
    return [prefix, split[1]];
  }

  static String format(double n) {
    if (n >= 1000000000) {
      n /= 1000000000;
      return "${n.toStringAsFixed(2)}B";
    } else if (n >= 1000000) {
      n /= 1000000;
      return "${n.toStringAsFixed(2)}M";
    } else if (n >= 10000) {
      n /= 1000;
      return "${n.toStringAsFixed(2)}K";
    } else {
      return n.toStringAsFixed(4);
    }
  }

  static String formatChinese(var n, {normalFixed = 0}) {
    bool f = n < 0;
    if (f) n = -n;
    if (n >= 100000000) {
      n /= 100000000;
      return "${f ? '-' : ''}${_formatNum(n, 2)}亿";
    } else if (n >= 10000) {
      n /= 10000;
      return "${f ? '-' : ''}${_formatNum(n, 2)}万";
    } else {
      return (f ? '-' : '') + _formatNum(n, normalFixed);
    }
  }

  static String _formatNum(var num, int postion) {
    var string = num.toString();
    var indexOf = string.indexOf('.');
    if (indexOf > 0 && string.length >= indexOf + 1 + postion) {
      return string.substring(0, postion > 0 ? indexOf + 1 + postion : indexOf);
    } else {
      return num.toStringAsFixed(postion);
    }
  }

  static int getDecimalLength(double b) {
    String s = b.toString();
    int dotIndex = s.indexOf(".");
    if (dotIndex < 0) {
      return 0;
    } else {
      return s.length - dotIndex - 1;
    }
  }

  static int getMaxDecimalLength(double a, double b, double c, double d) {
    int result = max(getDecimalLength(a), getDecimalLength(b));
    result = max(result, getDecimalLength(c));
    result = max(result, getDecimalLength(d));
    return result;
  }

  static bool checkNotNullOrZero(double? a) {
    if (a == null || a == 0) {
      return false;
    } else if (a.abs().toStringAsFixed(4) == "0.0000") {
      return false;
    } else {
      return true;
    }
  }
}
