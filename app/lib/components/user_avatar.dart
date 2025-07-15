import 'dart:math';

import 'package:app/common/constant.dart';
import 'package:app/common/global.dart';
import 'package:flutter/material.dart';

class UserAvatar extends StatefulWidget {
  final String? url;
  final String? name;
  final double? width;
  final double? height;
  final Color? borderColor;
  final double? borderWidth;

  const UserAvatar(
      {this.width,
      this.url,
      this.name,
      this.height,
      this.borderColor,
      this.borderWidth,
      super.key});

  @override
  State<UserAvatar> createState() => _UserAvatarState();
}

class _UserAvatarState extends State<UserAvatar> {
  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, cos) {
      if (widget.url != null && widget.url!.isNotEmpty) {
        return ClipOval(
          child: Container(
            width: widget.width ?? cos.maxWidth,
            height: widget.height ?? cos.maxHeight,
            decoration: BoxDecoration(
              color: GlobalData.themeData!.cardColor,
              border: widget.borderWidth == null
                  ? null
                  : Border.all(
                      color: widget.borderColor ?? Colors.white,
                      width: widget.borderWidth ?? 3),
              shape: BoxShape.circle, // 确保容器是圆形
              image: DecorationImage(
                image: NetworkImage(widget.url!),
                fit: BoxFit.cover,
              ),
            ),
          ),
        );
      } else if (widget.name != null && widget.name!.isNotEmpty) {
        return ClipOval(
          child: Container(
            width: widget.width ?? cos.maxWidth,
            height: widget.height ?? cos.maxHeight,
            decoration: BoxDecoration(
              color: GlobalData.themeData!.cardColor,
              border: widget.borderWidth == null
                  ? null
                  : Border.all(
                      color: widget.borderColor ?? Colors.white,
                      width: widget.borderWidth ?? 3),
              shape: BoxShape.circle, // 确保容器是圆形
            ),
            child: Center(
              child: Text(
                widget.name!.substring(0, 1),
                style: TextStyle(
                  color: Colors.white,
                  fontSize: min(widget.height ?? cos.maxWidth,
                          widget.width ?? cos.maxWidth) /
                      2,
                ),
              ),
            ),
          ),
        );
      }
      return ClipOval(
        child: Container(
          width: widget.width ?? cos.maxWidth,
          height: widget.height ?? cos.maxHeight,
          decoration: BoxDecoration(
            color: GlobalData.themeData!.cardColor,
            border: widget.borderWidth == null
                ? null
                : Border.all(
                    color: widget.borderColor ?? Colors.white,
                    width: widget.borderWidth ?? 3),
            shape: BoxShape.circle, // 确保容器是圆形
            image: DecorationImage(
              image: NetworkImage(Avatar.man),
              fit: BoxFit.cover,
            ),
          ),
        ),
      );
    });
  }
}
